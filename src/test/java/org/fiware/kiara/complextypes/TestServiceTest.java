package org.fiware.kiara.complextypes;

import com.google.common.util.concurrent.SettableFuture;
import org.fiware.kiara.Context;
import org.fiware.kiara.client.Connection;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.server.Server;
import org.fiware.kiara.server.Service;
import org.fiware.kiara.test.TestSetup;
import org.fiware.kiara.test.TestUtils;
import org.fiware.kiara.test.TypeFactory;
import org.fiware.kiara.transport.ServerTransport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
public class TestServiceTest {

    static {
        //System.setProperty("java.util.logging.config.file", "/home/rubinste/.kiara/logging.properties");
    }

    public static class TestServiceServantImpl extends TestServiceServant {

        public static MyStruct createMyStruct(int value, String str) {
            MyStruct s = new MyStruct();
            s.setMyInt(value);
            s.setMyString(str);
            List<List<Integer>> array = new ArrayList<>();
            for (int i = 0; i < 10; ++i) {
                ArrayList<Integer> inner = new ArrayList<>();
                for (int j = 0; j < 5; ++j) {
                    inner.add(j);
                }
                array.add(inner);
            }
            s.setArrayInt(array);

            List<String> arrayString = new ArrayList<>();
            for (int i = 0; i < 10; ++i) {
                arrayString.add(str + " " + i);
            }
            s.setArrayString(arrayString);

            List<Integer> sequenceLong = new ArrayList<>();
            for (int i = 0; i < 8; ++i) {
                sequenceLong.add(i);
            }
            s.setSequenceInt(sequenceLong);
            return s;
        }

        @Override
        public MyStruct return_param_func(MyStruct param1, int param2) {
            System.out.println("Current thread: " + Thread.currentThread().toString());
            param1.setMyString("return_param_func");
            param1.setMyInt(param2);
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

        public TestServiceSetup(int port, String transport, String protocol, String configPath, TypeFactory<ExecutorService> serverDispatchingExecutorFactory) {
            super(port, transport, protocol, configPath, serverDispatchingExecutorFactory);
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

            serverTransport.setDispatchingExecutor(this.getServerDispatchingExecutor());

            server.addService(service, serverTransport, serializer);

            return server;
        }

        @Override
        protected TestServiceClient createClient(Connection connection) throws Exception {
            return connection.getServiceProxy(TestServiceClient.class);
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
        return TestUtils.createDefaultTestConfig();
    }

    public TestServiceTest(String transport, String protocol, TypeFactory<ExecutorService> serverExecutorFactory) {
        testServiceSetup = new TestServiceSetup(9090, transport, protocol, "", serverExecutorFactory);
    }

    @Test
    public void testComplexTypesSync() throws Exception {
        MyStruct value = testService.only_return_func();

        Assert.assertNotNull(value);
        Assert.assertEquals(42, value.getMyInt());
        Assert.assertEquals("only_return_func", value.getMyString());
        Assert.assertEquals(TestServiceServantImpl.createMyStruct(42, "only_return_func"), value);

        for (int i = 0; i < 10; ++i) {
            value = testService.return_param_func(value, i);
            Assert.assertNotNull(value);
            Assert.assertEquals(i, value.getMyInt());
            Assert.assertEquals("return_param_func", value.getMyString());
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

        Assert.assertEquals(42, value.get().getMyInt());
        Assert.assertEquals("only_return_func", value.get().getMyString());
        Assert.assertEquals(value.get(), TestServiceServantImpl.createMyStruct(42, "only_return_func"));

        // Synchronous parallel test
        Future<MyStruct>[] result = new Future[100];

        // Addition
        for (int i = 0; i < result.length; i++) {
            final int arg = i;
            result[arg] = executor.submit(new Callable<MyStruct>() {

                @Override
                public MyStruct call() throws Exception {
                    MyStruct s = testService.return_param_func(value.get(), arg);
                    Assert.assertNotNull(s);
                    Assert.assertEquals("return_param_func", s.getMyString());
                    return s;
                }
            });
        }

        for (int i = 0; i < result.length; i++) {
            assertEquals(i, result[i].get().getMyInt());
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

    /**
     *
     * @throws Exception
     */
    @Test
    public void testComplexTypesAsync() throws Exception {
        final SettableFuture<MyStruct> value = SettableFuture.create();
        testService.only_return_func(new AsyncCallback<MyStruct>() {

            @Override
            public void onSuccess(MyStruct result) {
                value.set(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                value.setException(caught);
            }
        });

        Assert.assertEquals(42, value.get().getMyInt());
        Assert.assertEquals("only_return_func", value.get().getMyString());
        Assert.assertEquals(TestServiceServantImpl.createMyStruct(42, "only_return_func"), value.get());

        Future<MyStruct>[] result = new Future[100];

        for (int i = 0; i < result.length; i++) {
            final SettableFuture<MyStruct> resultValue = SettableFuture.create();
            final int arg = i;
            testService.return_param_func(value.get(), arg, new AsyncCallback<MyStruct>() {

                @Override
                public void onSuccess(MyStruct result) {
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
            Assert.assertNotNull(result[i].get());
            Assert.assertEquals("return_param_func", result[i].get().getMyString());
        }

    }

}
