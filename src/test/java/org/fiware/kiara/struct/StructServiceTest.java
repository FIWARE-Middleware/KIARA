package org.fiware.kiara.struct;

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
public class StructServiceTest {

    static {
        //System.setProperty("java.util.logging.config.file", "/home/rubinste/.kiara/logging.properties");
    }

    public static class StructServiceServantImpl extends StructServiceServant {

        public static PrimitiveTypesStruct createPrimitiveTypesStruct() {
            PrimitiveTypesStruct s = new PrimitiveTypesStruct();
            s.setMyChar('a');
            s.setMyByte((byte)42);
            s.setMyUShort((short)65534);
            s.setMyShort((short)-40001);
            s.setMyBoolean(true);
            s.setMyUInt(80000);
            s.setMyInt(-80000);
            s.setMyULong(90000);
            s.setMyLong(-90000);
            s.setMyString("Test");
            s.setMyFloat((float) -2.0);
            s.setMyDouble(-100000.0);
            s.setMyBoolean(true);
            s.setMyString("Test123456789");
            s.setMyString5("12345");
            return s;
        }

        @Override
        public PrimitiveTypesStruct sendReceivePrimitives(PrimitiveTypesStruct value) {
            return value;
        }

        @Override
        public OuterStruct sendReceiveStruct(OuterStruct value) {
            return value;
        }
    }

    public static class StructServiceSetup extends TestSetup<StructServiceClient> {

        public StructServiceSetup(int port, String transport, String protocol, String configPath, TypeFactory<ExecutorService> serverDispatchingExecutorFactory) {
            super(port, transport, protocol, configPath, serverDispatchingExecutorFactory);
        }

        @Override
        protected Server createServer(Context context, int port, String transport, String protocol, String configPath) throws Exception {
            Service service = context.createService();

            System.out.printf("Register server functions ....%n");

            StructServiceServant impl = new StructServiceServantImpl();
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
        protected StructServiceClient createClient(Connection connection) throws Exception {
            return connection.getServiceProxy(StructServiceClient.class);
        }

    }

    private final StructServiceSetup structServiceSetup;
    private StructServiceClient structService = null;
    private ExecutorService executor = null;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        if (structServiceSetup.getServerDispatchingExecutor() != null) {
            Assert.assertFalse(structServiceSetup.getServerDispatchingExecutor().isShutdown());
        }
        structService = structServiceSetup.start(100);
        Assert.assertNotNull(structService);
        executor = Executors.newCachedThreadPool();
    }

    @After
    public void tearDown() throws Exception {
        structServiceSetup.shutdown();
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);
    }

    @Parameterized.Parameters
    public static Collection configs() {
        return TestUtils.createDefaultTestConfig();
    }

    public StructServiceTest(String transport, String protocol, TypeFactory<ExecutorService> serverExecutorFactory) {
        structServiceSetup = new StructServiceSetup(9090, transport, protocol, "", serverExecutorFactory);
    }

    @Test
    public void testStructTypesSync() throws Exception {
        PrimitiveTypesStruct value = StructServiceServantImpl.createPrimitiveTypesStruct();
        for (int i = 0; i < 10; i++) {
            value.setMyInt(i);
            PrimitiveTypesStruct result = structService.sendReceivePrimitives(value);

            Assert.assertNotNull(result);
            Assert.assertEquals(value, result);
        }
    }

    @Test
    public void testStructTypesParallel() throws Exception {
        Assert.assertFalse(executor.isShutdown());
        // Synchronous parallel test
        Future<PrimitiveTypesStruct>[] result = new Future[100];

        // Addition
        for (int i = 0; i < result.length; i++) {
            final int arg = i;
            result[arg] = executor.submit(new Callable<PrimitiveTypesStruct>() {

                @Override
                public PrimitiveTypesStruct call() throws Exception {
                    PrimitiveTypesStruct value = StructServiceServantImpl.createPrimitiveTypesStruct();
                    value.setMyInt(arg);
                    PrimitiveTypesStruct s = structService.sendReceivePrimitives(value);
                    Assert.assertNotNull(s);
                    return s;
                }
            });
        }

        for (int i = 0; i < result.length; i++) {
            assertEquals(i, result[i].get().getMyInt());
            PrimitiveTypesStruct value = StructServiceServantImpl.createPrimitiveTypesStruct();
            value.setMyInt(i);
            assertEquals(value, result[i].get());
        }
    }

    @Test
    public void testStructTypesAsync() throws Exception {
        Assert.assertFalse(executor.isShutdown());
        Future<PrimitiveTypesStruct>[] result = new Future[100];

        for (int i = 0; i < result.length; i++) {
            final SettableFuture<PrimitiveTypesStruct> resultValue = SettableFuture.create();
            final int arg = i;
            PrimitiveTypesStruct value = StructServiceServantImpl.createPrimitiveTypesStruct();
            value.setMyInt(arg);
            structService.sendReceivePrimitives(value, new AsyncCallback<PrimitiveTypesStruct>() {

                @Override
                public void onSuccess(PrimitiveTypesStruct result) {
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
            Assert.assertEquals(i, result[i].get().getMyInt());
        }
    }

}
