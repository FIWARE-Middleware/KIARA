package org.fiware.kiara.calculator;

import org.fiware.kiara.test.TestSetup;

import com.google.common.util.concurrent.SettableFuture;

import org.fiware.kiara.Context;
import org.fiware.kiara.client.Connection;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.server.Server;
import org.fiware.kiara.server.Service;
import org.fiware.kiara.test.TestUtils;
import org.fiware.kiara.test.TypeFactory;
import org.fiware.kiara.transport.ServerTransport;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.fiware.kiara.Kiara;
import org.fiware.kiara.client.AsyncCallback;
import org.fiware.kiara.dynamic.DynamicValueBuilder;
import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.dynamic.data.DynamicPrimitive;
import org.fiware.kiara.dynamic.services.DynamicFunctionHandler;
import org.fiware.kiara.dynamic.services.DynamicFunctionRequest;
import org.fiware.kiara.dynamic.services.DynamicFunctionResponse;
import org.fiware.kiara.dynamic.services.DynamicProxy;
import org.fiware.kiara.typecode.TypeDescriptorBuilder;
import org.fiware.kiara.typecode.TypeKind;
import org.fiware.kiara.typecode.data.PrimitiveTypeDescriptor;
import org.fiware.kiara.typecode.services.FunctionTypeDescriptor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CalculatorDynamicTest {
    
    static {
        //System.setProperty("java.util.logging.config.file", "/home/rubinste/.kiara/logging.properties");
    }

    public static class CalculatorServantImpl extends CalculatorServant {

        @Override
        public int add(int param1, int param2) {
            //System.out.println("Current thread: " + Thread.currentThread().toString());
            //System.out.println("Adding " + param1 + " and " + param2);
            return param1 + param2;
        }

        @Override
        public int subtract(int param1, int param2) {
            //System.out.println("Current thread: " + Thread.currentThread().toString());
            //System.out.println("Subtracting " + param1 + " and " + param2);
            return param1 - param2;
        }
    }

    public static class CalculatorSetup extends TestSetup<DynamicProxy> {

        public CalculatorSetup(int port, String transport, String protocol, String configPath, TypeFactory<ExecutorService> serverDispatchingExecutorFactory) {
            super(port, transport, protocol, configPath, serverDispatchingExecutorFactory);
        }

        @Override
        protected Server createServer(Context context, int port, String transport, String protocol, String configPath) throws Exception {
            Service service = context.createService();

            System.out.printf("Register server functions ....%n");

            service.loadServiceIDLFromString(IDLText.contents);

            final TypeDescriptorBuilder tdbuilder = Kiara.getTypeDescriptorBuilder();
            final DynamicValueBuilder builder = Kiara.getDynamicValueBuilder();
            final PrimitiveTypeDescriptor intTy = tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);

            DynamicFunctionHandler handler = new DynamicFunctionHandler() {

                @Override
                public void process(DynamicFunctionRequest request, DynamicFunctionResponse response) {
                    //System.out.println("Current thread: " + Thread.currentThread().toString());

                    int a = (Integer) ((DynamicPrimitive) request.getParameterAt(0)).get();
                    int b = (Integer) ((DynamicPrimitive) request.getParameterAt(1)).get();

                    final DynamicPrimitive intVal = (DynamicPrimitive) builder.createData(intTy);

                    if ("add".equals(((FunctionTypeDescriptor) request.getTypeDescriptor()).getName())) {
                        //System.out.println("Adding " + a + " and " + b);

                        intVal.set(a + b);
                    } else if ("subtract".equals(((FunctionTypeDescriptor) request.getTypeDescriptor()).getName())) {
                        //System.out.println("Subtracting " + a + " and " + b);

                        intVal.set(a - b);
                    }
                    response.setReturnValue(intVal);
                }
            };

            service.register("Calculator.add", handler);
            service.register("Calculator.subtract", handler);

            System.out.printf("Starting server...%n");

            Server server = context.createServer();
            server.enableNegotiationService("0.0.0.0", port, "/service");

            //server.addService(service, makeServerTransportUri(transport, port), protocol);
            ServerTransport serverTransport = context.createServerTransport(makeServerTransportUri(transport, port + 1));
            Serializer serializer = context.createSerializer(protocol);

            serverTransport.setDispatchingExecutor(this.getServerDispatchingExecutor());

            server.addService(service, serverTransport, serializer);

            return server;
        }

        @Override
        protected DynamicProxy createClient(Connection connection) throws Exception {
            return connection.getDynamicProxy("Calculator");
        }

        @Override
        protected String makeClientTransportUri(String transport, int port, String protocol) {
            return "kiara://127.0.0.1:" + port + "/service";
        }

    }

    private final CalculatorSetup calculatorSetup;
    private DynamicProxy calculator = null;
    private ExecutorService executor = null;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        calculator = calculatorSetup.start(100);
        Assert.assertNotNull(calculator);
        executor = Executors.newCachedThreadPool();
    }

    @After
    public void tearDown() throws Exception {
        calculatorSetup.shutdown();
        if (executor != null) {
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.MINUTES);
        }
    }

    @Parameterized.Parameters
    public static Collection configs() {
        return TestUtils.createDefaultTestConfig();
    }

    public CalculatorDynamicTest(String transport, String protocol, TypeFactory<ExecutorService> serverExecutorFactory) {
        calculatorSetup = new CalculatorSetup(9090, transport, protocol, "", serverExecutorFactory);
    }

    public static class DynamicExecutionException extends Exception {

        private final DynamicData errorValue;

        public DynamicExecutionException(DynamicData errorValue) {
            this.errorValue = errorValue;
        }

        public DynamicExecutionException(String message, DynamicData errorValue) {
            super(message);
            this.errorValue = errorValue;
        }

        public DynamicExecutionException(String message, Throwable cause, DynamicData errorValue) {
            super(message, cause);
            this.errorValue = errorValue;
        }

        public DynamicExecutionException(Throwable cause, DynamicData errorValue) {
            super(cause);
            this.errorValue = errorValue;
        }

        public DynamicData getErrorValue() {
            return errorValue;
        }
    }

    public int call_add(int a, int b) {
        DynamicFunctionRequest drequest = calculator.createFunctionRequest("add");
        ((DynamicPrimitive) drequest.getParameterAt(0)).set(a);
        ((DynamicPrimitive) drequest.getParameterAt(1)).set(b);

        DynamicFunctionResponse dresponse = drequest.execute();
        assertFalse(dresponse.isException());
        DynamicData dresult = dresponse.getReturnValue();
        return (Integer) ((DynamicPrimitive) dresult).get();
    }

    public int call_subtract(int a, int b) {
        DynamicFunctionRequest drequest = calculator.createFunctionRequest("subtract");
        ((DynamicPrimitive) drequest.getParameterAt(0)).set(a);
        ((DynamicPrimitive) drequest.getParameterAt(1)).set(b);

        DynamicFunctionResponse dresponse = drequest.execute();
        assertFalse(dresponse.isException());
        DynamicData dresult = dresponse.getReturnValue();
        return (Integer) ((DynamicPrimitive) dresult).get();
    }

    public void call_add(int a, int b, final AsyncCallback<Integer> cb) {
        DynamicFunctionRequest drequest = calculator.createFunctionRequest("add");
        ((DynamicPrimitive) drequest.getParameterAt(0)).set(a);
        ((DynamicPrimitive) drequest.getParameterAt(1)).set(b);

        drequest.executeAsync(new AsyncCallback<DynamicFunctionResponse>() {

            @Override
            public void onSuccess(DynamicFunctionResponse response) {
                if (response.isException()) {
                    DynamicData result = response.getReturnValue();
                    cb.onFailure(new DynamicExecutionException(result));
                } else {
                    try {
                        final DynamicData result = response.getReturnValue();
                        cb.onSuccess((Integer) ((DynamicPrimitive) result).get());
                    } catch (Exception ex) {
                        cb.onFailure(ex);
                    }
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                cb.onFailure(caught);
            }
        }
        );
    }

    public void call_subtract(int a, int b, final AsyncCallback<Integer> cb) {
        DynamicFunctionRequest drequest = calculator.createFunctionRequest("subtract");
        ((DynamicPrimitive) drequest.getParameterAt(0)).set(a);
        ((DynamicPrimitive) drequest.getParameterAt(1)).set(b);

        drequest.executeAsync(new AsyncCallback<DynamicFunctionResponse>() {

            @Override
            public void onSuccess(DynamicFunctionResponse response) {
                if (response.isException()) {
                    DynamicData result = response.getReturnValue();
                    cb.onFailure(new DynamicExecutionException(result));
                } else {
                    try {
                        final DynamicData result = response.getReturnValue();
                        cb.onSuccess((Integer) ((DynamicPrimitive) result).get());
                    } catch (Exception ex) {
                        cb.onFailure(ex);
                    }
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                cb.onFailure(caught);
            }
        }
        );
    }

    /**
     * Test of main method, of class CalcTestServer.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testDCalcSync() throws Exception {
        for (int i = 0; i < 100; ++i) {
            assertEquals(21 + (32 + i), call_add(21, 32 + i));
            assertEquals(32 - (21 + i), call_subtract(32, 21 + i));
        }
    }

    @Test
    public void testDCalcSyncParallel() throws Exception {
        // Synchronous parallel test
        Future<Integer>[] result = new Future[100];

        // Addition
        for (int i = 0; i < result.length; i++) {
            final int arg = i;
            result[arg] = executor.submit(new Callable<Integer>() {

                @Override
                public Integer call() throws Exception {
                    final int result = call_add(arg, arg);
//                    System.out.println(arg + "+" + arg + "=" + result);
                    assertEquals(arg + arg, result);
                    return result;
                }
            });
        }

        for (int i = 0; i < result.length; i++) {
            assertEquals(i + i, result[i].get().intValue());
        }

        // Subtraction
        final int resultLength = result.length;
        for (int i = 0; i < result.length; i++) {
            final int arg = i;
            result[arg] = executor.submit(new Callable<Integer>() {

                @Override
                public Integer call() throws Exception {
                    final int result = call_subtract(resultLength, arg);
//                    System.out.println(resultLength + "-" + arg + "=" + result);
                    assertEquals(resultLength - arg, result);
                    return result;
                }
            });
        }

        for (int i = 0; i < result.length; i++) {
            assertEquals(resultLength - i, result[i].get().intValue());
        }
    }

    @Test
    public void testDCalcAsync() throws Exception {
        // Synchronous parallel test
        Future<Integer>[] result = new Future[100];

        // Addition
        for (int i = 0; i < result.length; i++) {
            final SettableFuture<Integer> resultValue = SettableFuture.create();
            final int arg = i;
            call_add(i, i, new AsyncCallback<Integer>() {

                @Override
                public void onSuccess(Integer result) {
//                    System.out.println(arg + "+" + arg + "=" + result);
                    assertEquals(arg + arg, result.intValue());
                    resultValue.set(result);
                }

                @Override
                public void onFailure(Throwable caught) {
                    resultValue.setException(caught);
                }
            });
            result[arg] = resultValue;
        }

        for (int i = 0; i < result.length; i++) {
            assertEquals(i + i, result[i].get().intValue());
        }

        // Subtraction
        final int resultLength = result.length;
        for (int i = 0; i < result.length; i++) {
            final SettableFuture<Integer> resultValue = SettableFuture.create();
            final int arg = i;
            call_subtract(resultLength, arg, new AsyncCallback<Integer>() {

                @Override
                public void onSuccess(Integer result) {
//                    System.out.println(resultLength + "-" + arg + "=" + result);
                    assertEquals(resultLength - arg, result.intValue());
                    resultValue.set(result);
                }

                @Override
                public void onFailure(Throwable caught) {
                    resultValue.setException(caught);
                }
            });
            result[arg] = resultValue;
        }

        for (int i = 0; i < result.length; i++) {
            assertEquals(resultLength - i, result[i].get().intValue());
        }

    }

}
