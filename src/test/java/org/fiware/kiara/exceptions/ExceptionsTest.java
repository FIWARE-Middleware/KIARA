package org.fiware.kiara.exceptions;

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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ExceptionsTest { 
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    static {
        //System.setProperty("java.util.logging.config.file", "/home/rubinste/.kiara/logging.properties");
    }

    public static class ExceptionsServantImpl extends ExceptionsServant {

        @Override
        public float divide(float param1, float param2) throws DividedByZeroException {
            System.out.println("Current thread: " + Thread.currentThread().toString());
            System.out.println("Dividing " + param1 + " and " + param2);
            if (param2 == 0) {
                throw new DividedByZeroException();
            }
            return param1 / param2;
            //return 0;
        }

        @Override
        public int function() throws FirstException, SecondException {
            System.out.println("Current thread: " + Thread.currentThread().toString());
            System.out.println("Executing function");
            throw new SecondException();
        }
    }

    public static class ExceptionsSetup extends TestSetup<ExceptionsClient> {

        private final ExecutorService serverDispatchingExecutor;

        public ExceptionsSetup(int port, String transport, String protocol, String configPath, TypeFactory<ExecutorService> serverDispatchingExecutorFactory) {
            super(port, transport, protocol, configPath);
            this.serverDispatchingExecutor = serverDispatchingExecutorFactory != null ? serverDispatchingExecutorFactory.create() : null;
            System.out.printf("Testing port=%d transport=%s protocol=%s configPath=%s serverDispatchingExecutor=%s%n", port, transport, protocol, configPath, serverDispatchingExecutor);
        }

        @Override
        protected Server createServer(Context context, int port, String transport, String protocol, String configPath) throws Exception {
            Service service = context.createService();

            System.out.printf("Register server functions ....%n");

            ExceptionsServant impl = new ExceptionsServantImpl();
            service.register(impl);

            System.out.printf("Starting server...%n");

            Server server = context.createServer();
            //server.addService(service, makeServerTransportUri(transport, port), protocol);

            ServerTransport serverTransport = context.createServerTransport(makeServerTransportUri(transport, port));
            Serializer serializer = context.createSerializer(protocol);

            serverTransport.setDispatchingExecutor(this.serverDispatchingExecutor);

            server.addService(service, serverTransport, serializer);

            return server;
        }

        @Override
        protected ExceptionsClient createClient(Connection connection) throws Exception {
            return connection.getServiceProxy(ExceptionsClient.class);
        }

        @Override
        protected String makeServerTransportUri(String transport, int port) {
            if ("tcp".equals(transport)) {
                return "tcp://0.0.0.0:" + port;
            }
            throw new IllegalArgumentException("Unknown transport " + transport);
        }

        @Override
        protected String makeClientTransportUri(String transport, int port, String protocol) {
            if ("tcp".equals(transport)) {
                return "tcp://0.0.0.0:" + port + "/?serialization=" + protocol;
            }
            throw new IllegalArgumentException("Unknown transport " + transport);
        }

        @Override
        public void shutdown() throws Exception {
            super.shutdown();
            if (serverDispatchingExecutor != null) {
                serverDispatchingExecutor.shutdown();
                serverDispatchingExecutor.awaitTermination(10, TimeUnit.MINUTES);
            }
        }

    }

    private final ExceptionsSetup calculatorSetup;
    private ExceptionsClient exceptions = null;
    private ExecutorService executor = null;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        exceptions = calculatorSetup.start(100);
        Assert.assertNotNull(exceptions);
        executor = Executors.newCachedThreadPool();
    }

    @After
    public void tearDown() throws Exception {
        calculatorSetup.shutdown();
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);
    }

    @Parameterized.Parameters
    public static Collection configs() {
        return TestUtils.createDefaultTestConfig();
    }

    public ExceptionsTest(String transport, String protocol, TypeFactory<ExecutorService> serverExecutorFactory) {
        calculatorSetup = new ExceptionsSetup(9090, transport, protocol, "", serverExecutorFactory);
    }

    /**
     * Test of main method, of class CalcTestServer.
     */
    @Test
    public void testExceptionsSync() throws Exception {
        
        try {
            exceptions.divide(21, 0);
            assertTrue(false);
        } catch (DividedByZeroException e) {
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        
        try {
            exceptions.function();
            assertTrue(false);
        } catch (SecondException e) {
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        
    }

    @Test
    public void testExceptionsSyncParallel() throws Exception {
        // Synchronous parallel test
        Future<Float>[] result = new Future[100];

        // Divide
        for (int i = 0; i < result.length; i++) {
            final int arg = i;
            result[arg] = executor.submit(new Callable<Float>() {

                @Override
                public Float call() throws Exception {
                    final float result = exceptions.divide(21, 0);
                    return result;
                }
            });
        }
        
        for (int i = 0; i < result.length; i++) {
            //assertTrue(true);
            try {
               result[i].get(); 
            } catch (ExecutionException ex) {
                if (ex.getCause() instanceof DividedByZeroException) {
                    assertTrue(true);
                } else {
                    assertTrue(false);
                }
            }
        }

        // Function
        Future<Integer>[] result2 = new Future[100];
        for (int i = 0; i < result.length; i++) {
            final int arg = i;
            result2[arg] = executor.submit(new Callable<Integer>() {

                @Override
                public Integer call() throws Exception {
                    final int result = exceptions.function();
                    assertTrue(false);
                    return result;
                }
            });
        }
        
        for (int i = 0; i < result2.length; i++) {
            try {
                result2[i].get(); 
             } catch (ExecutionException ex) {
                 if (ex.getCause() instanceof SecondException) {
                     assertTrue(true);
                 } else {
                     assertTrue(false);
                 }
             }
        }
    }

    @Test
    public void testExceptionsAsync() throws Exception {
        // Synchronous parallel test
        Future<Float>[] result = new Future[100];

        // Addition
        for (int i = 0; i < result.length; i++) {
            final SettableFuture<Float> resultValue = SettableFuture.create();
            final int arg = i;
            //expectedException.expect(DividedByZeroException.class);
            exceptions.divide((float) i, (float) 0, new ExceptionsAsync.divide_AsyncCallback() {

                @Override
                public void onSuccess(Float result) {
                    System.err.println(arg + "+" + arg + "=" + result);
                    assertTrue(false);
                }

                @Override
                public void onFailure(Throwable caught) {
                    resultValue.setException(caught);
                }
            });
            result[arg] = resultValue;
        }

        for (int i = 0; i < result.length; i++) {
            //assertEquals(i + i, result[i].get().intValue());
            try {
                result[i].get();
            }catch (ExecutionException ex) {
                if (ex.getCause() instanceof DividedByZeroException) {
                    assertTrue(true);
                } else {
                    assertTrue(false);
                }
            }
        }

        // Synchronous parallel test
        Future<Integer>[] result2 = new Future[100];
        
        // Addition
        
        for (int i = 0; i < result2.length; i++) {
            final SettableFuture<Integer> resultValue = SettableFuture.create();
            final int arg = i;
            
            exceptions.function(new ExceptionsAsync.function_AsyncCallback() {
                
                @Override
                public void onSuccess(Integer result) {
                    System.err.println(arg + "+" + arg + "=" + result);
                    assertTrue(false);
                }

                @Override
                public void onFailure(Throwable caught) {
                    resultValue.setException(caught);
                }
            });
            result2[arg] = resultValue;
        }

        for (int i = 0; i < result2.length; i++) {
            //assertEquals(i + i, result[i].get().intValue());
            try {
                result2[i].get();
            }catch (ExecutionException ex) {
                if (ex.getCause() instanceof SecondException) {
                    assertTrue(true);
                } else {
                    assertTrue(false);
                }
            }
        }

    }

}
