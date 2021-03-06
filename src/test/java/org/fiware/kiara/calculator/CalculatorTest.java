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
import org.fiware.kiara.client.AsyncCallback;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CalculatorTest {

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

    public static class CalculatorSetup extends TestSetup<CalculatorClient> {

        public CalculatorSetup(int port, String transport, String protocol, String configPath, TypeFactory<ExecutorService> serverDispatchingExecutorFactory) {
            super(port, transport, protocol, configPath, serverDispatchingExecutorFactory);
        }

        @Override
        protected Server createServer(Context context, int port, String transport, String protocol, String configPath) throws Exception {
            Service service = context.createService();

            System.out.printf("Register server functions ....%n");

            CalculatorServant impl = new CalculatorServantImpl();
            service.register(impl);

            System.out.printf("Starting server, protocol = " + transport +" ...%n");

            Server server = context.createServer();
            //server.addService(service, makeServerTransportUri(transport, port), protocol);

            ServerTransport serverTransport = context.createServerTransport(makeServerTransportUri(transport, port));
            Serializer serializer = context.createSerializer(protocol);

            serverTransport.setDispatchingExecutor(this.getServerDispatchingExecutor());

            server.addService(service, serverTransport, serializer);

            return server;
        }

        @Override
        protected CalculatorClient createClient(Connection connection) throws Exception {
            return connection.getServiceProxy(CalculatorClient.class);
        }

    }

    private final CalculatorSetup calculatorSetup;
    private CalculatorClient calculator = null;
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
        //return TestUtils.createHttpTestConfig();
    }

    public CalculatorTest(String transport, String protocol, TypeFactory<ExecutorService> serverExecutorFactory) {
        calculatorSetup = new CalculatorSetup(9090, transport, protocol, "", serverExecutorFactory);
    }

    /**
     * Test of main method, of class CalcTestServer.
     */
    @Test
    public void testCalcSync() throws Exception {
        assertEquals(21 + 32, calculator.add(21, 32));
        assertEquals(32 - 21, calculator.subtract(32, 21));
    }

    @Test
    public void testCalcSyncParallel() throws Exception {
        // Synchronous parallel test
        Future<Integer>[] result = new Future[100];

        // Addition
        for (int i = 0; i < result.length; i++) {
            final int arg = i;
            result[arg] = executor.submit(new Callable<Integer>() {

                @Override
                public Integer call() throws Exception {
                    final int result = calculator.add(arg, arg);
                    System.err.println(arg + "+" + arg + "=" + result);
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
                    final int result = calculator.subtract(resultLength, arg);
                    System.err.println(resultLength + "-" + arg + "=" + result);
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
    public void testCalcAsync() throws Exception {
        // Synchronous parallel test
        Future<Integer>[] result = new Future[100];

        // Addition
        for (int i = 0; i < result.length; i++) {
            final SettableFuture<Integer> resultValue = SettableFuture.create();
            final int arg = i;
            calculator.add(i, i, new AsyncCallback<Integer>() {

                @Override
                public void onSuccess(Integer result) {
                    System.err.println(arg + "+" + arg + "=" + result);
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
            calculator.subtract(resultLength, arg, new AsyncCallback<Integer>() {

                @Override
                public void onSuccess(Integer result) {
                    System.err.println(resultLength + "-" + arg + "=" + result);
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
