package com.kiara.calculator;

import com.kiara.Context;
import com.kiara.client.Connection;
import com.kiara.serialization.Serializer;
import com.kiara.server.Server;
import com.kiara.server.Service;
import com.kiara.transport.ServerTransport;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
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

        public int add(int param1, int param2) {
            System.out.println("Current thread: " + Thread.currentThread().toString());
            System.out.println("Adding " + param1 + " and " + param2);
            return param1 + param2;
        }

        public int subtract(int param1, int param2) {
            System.out.println("Current thread: " + Thread.currentThread().toString());
            System.out.println("Subtracting " + param1 + " and " + param2);
            return param1 - param2;
        }
    }

    public static class CalculatorSetup extends TestSetup<Calculator> {

        public CalculatorSetup(int port, String transport, String protocol, String configPath) {
            super(port, transport, protocol, configPath);
        }

        @Override
        protected Server createServer(Context context, int port, String transport, String protocol, String configPath) throws Exception {
            Service service = context.createService();

            System.out.printf("Register server functions ....%n");

            CalculatorServant impl = new CalculatorServantImpl();
            service.register(impl);

            System.out.printf("Starting server...%n");

            Server server = context.createServer();
            //server.addService(service, makeServerTransportUri(transport, port), protocol);

            ServerTransport serverTransport = context.createServerTransport(makeServerTransportUri(transport, port));
            Serializer serializer = context.createSerializer(protocol);

            //serverTransport.setDispatchingExecutor(null);
            //serverTransport.setDispatchingExecutor(Executors.newSingleThreadExecutor());
            //serverTransport.setDispatchingExecutor(Executors.newFixedThreadPool(2));
            serverTransport.setDispatchingExecutor(Executors.newCachedThreadPool());

            server.addService(service, serverTransport, serializer);

            return server;
        }

        @Override
        protected Calculator createClient(Connection connection) throws Exception {
            return connection.getServiceProxy(CalculatorClient.class);
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
    }

    private final CalculatorSetup calculatorSetup;
    private Calculator calculator = null;

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
    }

    @After
    public void tearDown() throws Exception {
        calculatorSetup.shutdown();
    }

    @Parameterized.Parameters
    public static Collection configs() {
        Object[][] data = new Object[][]{
            {"tcp", "cdr"}
        };
        return Arrays.asList(data);
    }

    public CalculatorTest(String transport, String protocol) {
        calculatorSetup = new CalculatorSetup(9090, transport, protocol, "");
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
        ExecutorService executor = Executors.newCachedThreadPool();

        // Synchronous parallel test
        Future<Integer>[] result = new Future[100];

        // Addition
        for (int i = 0; i < result.length; i++) {
            final int arg = i;
            result[arg] = executor.submit(new Callable<Integer>() {

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

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);
    }

}
