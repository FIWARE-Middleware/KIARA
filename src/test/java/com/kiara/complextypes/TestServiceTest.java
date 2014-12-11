package com.kiara.complextypes;

import com.google.common.util.concurrent.SettableFuture;
import com.kiara.Context;
import com.kiara.client.Connection;
import com.kiara.serialization.Serializer;
import com.kiara.server.Server;
import com.kiara.server.Service;
import com.kiara.test.TestSetup;
import com.kiara.transport.ServerTransport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
public class TestServiceTest {

    static {
        //System.setProperty("java.util.logging.config.file", "/home/rubinste/.kiara/logging.properties");
    }

    public static class TestServiceServantImpl extends TestServiceServant {

        public static MyStruct createMyStruct(int value, String str) {
            MyStruct s = new MyStruct();
            s.setmyInt(value);
            s.setmyString(str);
            List<List<Integer>> array = new ArrayList<>();
            for (int i = 0; i < 10; ++i) {
                ArrayList<Integer> inner = new ArrayList<>();
                for (int j = 0; j < 5; ++j) {
                    inner.add(j);
                }
                array.add(inner);
            }
            s.setarrayInt(array);

            List<String> arrayString = new ArrayList<>();
            for (int i = 0; i < 10; ++i) {
                arrayString.add(str + " " + i);
            }
            s.setarrayString(arrayString);

            List<Integer> sequenceLong = new ArrayList<>();
            for (int i = 0; i < 8; ++i) {
                sequenceLong.add(i);
            }
            s.setsequenceInt(sequenceLong);
            return s;
        }

        @Override
        public MyStruct return_param_func(MyStruct param1, int param2) {
            System.out.println("Current thread: " + Thread.currentThread().toString());
            param1.setmyString("return_param_func");
            param1.setmyInt(param2);
            return param1;
        }

        @Override
        public void only_param_func(MyStruct param1) {
            System.out.println("Current thread: " + Thread.currentThread().toString());
            Assert.assertEquals(createMyStruct(1, "only_param_func"), param1);
        }

        @Override
        public MyStruct only_return_func() {
            MyStruct result = createMyStruct(42, "only_return_func");
            return result;
        }

        @Override
        public void oneway_return_param_func(MyStruct param1, int param2) {
            System.out.println("Current thread: " + Thread.currentThread().toString());
            Assert.assertEquals(createMyStruct(2, "oneway_return_param_func"), param1);
            Assert.assertEquals(3, param2);
        }

        @Override
        public void oneway_only_param_func(MyStruct param1) {
            Assert.assertEquals(createMyStruct(4, "oneway_only_param_func"), param1);
        }

        @Override
        public void oneway_only_return_func() {

        }
    }

    public static class TestServiceSetup extends TestSetup<TestServiceClient> {

        public TestServiceSetup(int port, String transport, String protocol, String configPath) {
            super(port, transport, protocol, configPath);
        }

        @Override
        protected Server createServer(Context context, int port, String transport, String protocol, String configPath) throws Exception {
            Service service = context.createService();

            System.out.printf("Register server functions ....%n");

            TestServiceServant impl = new TestServiceServantImpl();
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
        protected TestServiceClient createClient(Connection connection) throws Exception {
            return connection.getServiceProxy(TestServiceClient.class);
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

    private final TestServiceSetup testServiceSetup;
    private TestServiceClient testService = null;
    private ExecutorService executor = null;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        testService = testServiceSetup.start(100);
        Assert.assertNotNull(testService);
        executor = Executors.newCachedThreadPool();
    }

    @After
    public void tearDown() throws Exception {
        testServiceSetup.shutdown();
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);
    }

    @Parameterized.Parameters
    public static Collection configs() {
        Object[][] data = new Object[][]{
            {"tcp", "cdr"}
        };
        return Arrays.asList(data);
    }

    public TestServiceTest(String transport, String protocol) {
        testServiceSetup = new TestServiceSetup(9090, transport, protocol, "");
    }

    /**
     * Test of main method, of class CalcTestServer.
     */
    @Test
    public void testComplexTypesSync() throws Exception {
        MyStruct value = testService.only_return_func();

        Assert.assertNotNull(value);
        Assert.assertEquals(42, value.getmyInt());
        Assert.assertEquals("only_return_func", value.getmyString());
        Assert.assertEquals(TestServiceServantImpl.createMyStruct(42, "only_return_func"), value);

        for (int i = 0; i < 10; ++i) {
            value = testService.return_param_func(value, i);
            Assert.assertNotNull(value);
            Assert.assertEquals(i, value.getmyInt());
            Assert.assertEquals("return_param_func", value.getmyString());
        }

        value = TestServiceServantImpl.createMyStruct(1, "only_param_func");
        testService.only_param_func(value);

        value = TestServiceServantImpl.createMyStruct(2, "oneway_return_param_func");
        testService.oneway_return_param_func(value, 3);

        value = TestServiceServantImpl.createMyStruct(4, "oneway_only_param_func");
        testService.oneway_only_param_func(value);

        testService.oneway_only_return_func();
    }

    @Test
    public void testComplexTypesParallel() throws Exception {
        final Future<MyStruct> value = executor.submit(new Callable<MyStruct>() {

            @Override
            public MyStruct call() throws Exception {
                return testService.only_return_func();
            }
        });

        Assert.assertEquals(42, value.get().getmyInt());
        Assert.assertEquals("only_return_func", value.get().getmyString());
        Assert.assertEquals(value.get(), TestServiceServantImpl.createMyStruct(42, "only_return_func"));

        // Synchronous parallel test
        Future<MyStruct>[] result = new Future[100];

        // Addition
        for (int i = 0; i < result.length; i++) {
            final int arg = i;
            result[arg] = executor.submit(new Callable<MyStruct>() {

                public MyStruct call() throws Exception {
                    MyStruct s = testService.return_param_func(value.get(), arg);
                    Assert.assertNotNull(s);
                    Assert.assertEquals("return_param_func", s.getmyString());
                    return s;
                }
            });
        }

        for (int i = 0; i < result.length; i++) {
            assertEquals(i, result[i].get().getmyInt());
        }

        executor.submit(new Runnable() {
            @Override
            public void run() {
                testService.only_param_func(
                        TestServiceServantImpl.createMyStruct(1, "only_param_func"));
            }
        }).get();

        executor.submit(new Runnable() {
            @Override
            public void run() {
                testService.oneway_return_param_func(TestServiceServantImpl.createMyStruct(2, "oneway_return_param_func"), 3);
            }
        }).get();

        executor.submit(new Runnable() {
            @Override
            public void run() {
                testService.oneway_only_param_func(TestServiceServantImpl.createMyStruct(4, "oneway_only_param_func"));
            }
        }).get();
    }

    @Test
    public void testComplexTypesAsync() throws Exception {
        final SettableFuture<MyStruct> value = SettableFuture.create();
        testService.only_return_func(new TestServiceAsync.only_return_func_AsyncCallback() {

            @Override
            public void onSuccess(MyStruct result) {
                value.set(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                value.setException(caught);
            }
        });

        Assert.assertEquals(42, value.get().getmyInt());
        Assert.assertEquals("only_return_func", value.get().getmyString());
        Assert.assertEquals(TestServiceServantImpl.createMyStruct(42, "only_return_func"), value.get());

        Future<MyStruct>[] result = new Future[100];

        for (int i = 0; i < result.length; i++) {
            final SettableFuture<MyStruct> resultValue = SettableFuture.create();
            final int arg = i;
            testService.return_param_func(value.get(), arg, new TestServiceAsync.return_param_func_AsyncCallback() {

                public void onSuccess(MyStruct result) {
                    resultValue.set(result);
                }


                public void onFailure(Throwable caught) {
                    resultValue.setException(caught);
                }

            });
            result[arg] = resultValue;
        }

        for (int i = 0; i < result.length; i++) {
            Assert.assertNotNull(result[i].get());
            Assert.assertEquals("return_param_func", result[i].get().getmyString());
        }

     }

}
