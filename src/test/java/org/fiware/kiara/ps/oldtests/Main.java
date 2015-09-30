package org.fiware.kiara.ps.oldtests;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.fiware.kiara.ps.rtps.attributes.HistoryCacheAttributes;
import org.fiware.kiara.ps.rtps.common.EncapsulationKind;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.common.TopicKind;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.history.HistoryCache;
import org.fiware.kiara.ps.rtps.history.ReaderHistoryCache;
import org.fiware.kiara.ps.rtps.history.WriterHistoryCache;
import org.fiware.kiara.ps.rtps.messages.MessageReceiver;
import org.fiware.kiara.ps.rtps.messages.RTPSMessage;
import org.fiware.kiara.ps.rtps.messages.RTPSMessageBuilder;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;
import org.fiware.kiara.ps.rtps.messages.elements.Count;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.GUIDPrefix;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.ParameterList;
import org.fiware.kiara.ps.rtps.messages.elements.ProtocolVersion;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumber;
import org.fiware.kiara.ps.rtps.messages.elements.SequenceNumberSet;
import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.rtps.messages.elements.VendorId;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId.EntityIdEnum;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterKey;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterSentinel;

public class Main {

    public static void new_main(String[] args) {
        RTPSMessage msg = initMessage();
        dataMsgTest(msg);
        msg.serialize();

        boolean sendTest = true;

        if (sendTest) {
            
            EventLoopGroup group = new NioEventLoopGroup();
            
            try {
            
                Bootstrap b = new Bootstrap();
                b.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                        
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
                            String responseDump = ByteBufUtil.hexDump(msg.content());
                            
                            System.out.format("Received response:%n%s%n", responseDump);
                            ctx.close();
                        }
                        
                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                            cause.printStackTrace();
                            ctx.close();
                        }
                        
                    }
                );
        
                Channel ch = b.bind(0).sync().channel();
                
                ch.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(msg.getBuffer()), new InetSocketAddress("239.255.0.1", 7400))).sync();
                
                /*if (!ch.closeFuture().await(5000)) {
                    System.err.println("Request timed out.");
                }*/
                
            
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        RTPSMessage msg = initMessage();
        dataMsgTest(msg);
        msg.serialize();

        boolean sendTest = true;

        if (sendTest) {

            try {
                InetAddress addr = InetAddress.getByName("239.255.0.1");
                java.net.DatagramPacket packet = new java.net.DatagramPacket(msg.getBuffer(), msg.getBuffer().length, addr, 0);
                packet.setPort(7401);
                DatagramSocket socket = new DatagramSocket();
                

                socket.send(packet);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        //System.out.println("");
    }

    public static void old_old_main(String [] args) {
        //RTPSMessage msg = new RTPSMessage(128);
        RTPSMessage msg = initMessage();




        //infoTSMsgTest(msg);
        //infoDSTMsgTest(msg);
        //infoSRCMsgTest(msg);
        //dataMsgTest(msg);
        //heartbeatMsgTest(msg);
        //acknackMsgTest(msg);
        //gapMsgTest(msg);
        //padMsgTest(msg);


        //testHistoryCacheCreation();
        //testHistoryCacheReserveAndRelease();

        //locatorListReserve();
        //locatorListResize();

        /*msg = initMessage();
		heartbeatMsgTest_Receiver(msg);

		msg = initMessage();
		gapMsgTest_Receiver(msg);

		msg = initMessage();
		acknackMsgTest_Receiver(msg);

		msg = initMessage();
		infoDSTMsgTest_Receiver(msg);

		msg = initMessage();
		infoSRCMsgTest_Receiver(msg);

		msg = initMessage();
		infoTSMsgTest_Receiver(msg);

		msg = initMessage();
		padMsgTest_Receiver(msg);*/

        msg = initMessage();
        dataMsgTest(msg);

        //msg = initMessage();
        //dataInlineQosMsgTest(msg);

        //msg = initMessage();
        //dataInlineQosMsgTest_Receiver(msg);

        msg.serialize();

        boolean sendTest = true;

        /*if (sendTest) {

            try {
                InetAddress addr = InetAddress.getByName("239.255.0.1");
                DatagramPacket packet = new DatagramPacket(msg.getBuffer(), msg.getBuffer().length, addr, 9000);
                packet.setPort(1);
                DatagramSocket socket = new DatagramSocket();
                

                socket.send(packet);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }*/

        //System.out.println("");

    }

    public static RTPSMessage initMessage() {
        RTPSMessage msg = RTPSMessageBuilder.createMessage(128, RTPSEndian.LITTLE_ENDIAN);
        GUIDPrefix prefix = new GUIDPrefix();
        RTPSMessageBuilder.addHeader(msg, prefix);
        return msg;
    }

    public static void infoTSMsgTest(RTPSMessage msg) {

        RTPSMessageBuilder.addSubmessageInfoTSNow(msg, false);

    }

    public static void infoTSMsgTest_Receiver(RTPSMessage msg) {

        RTPSMessageBuilder.addSubmessageInfoTSNow(msg, false);
        msg.serialize();

        MessageReceiver receiver = new MessageReceiver(128);
        RTPSMessage outMsg = new RTPSMessage(0, null);
        outMsg.setBuffer(msg.getBuffer());
        outMsg.initBinaryOutputStream();

        receiver.processCDRMessage(new GUIDPrefix(), new Locator(), outMsg);

        if (outMsg.equals(msg)) {
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
        }

    }

    public static void infoDSTMsgTest(RTPSMessage msg) {

        GUIDPrefix prefix = new GUIDPrefix();

        RTPSMessageBuilder.addSubmessageInfoDST(msg, prefix);

    }

    public static void infoDSTMsgTest_Receiver(RTPSMessage msg) {

        GUIDPrefix prefix = new GUIDPrefix();

        RTPSMessageBuilder.addSubmessageInfoDST(msg, prefix);

        msg.serialize();

        MessageReceiver receiver = new MessageReceiver(128);
        RTPSMessage outMsg = new RTPSMessage(0, null);
        outMsg.setBuffer(msg.getBuffer());
        outMsg.initBinaryOutputStream();

        receiver.processCDRMessage(new GUIDPrefix(), new Locator(), outMsg);

        if (outMsg.equals(msg)) {
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
        }

    }

    public static void infoSRCMsgTest(RTPSMessage msg) {

        GUIDPrefix prefix = new GUIDPrefix();

        RTPSMessageBuilder.addSubmessageInfoSRC(msg, new ProtocolVersion((byte) 2, (byte) 1), new VendorId((byte) 1, (byte) 15), prefix);

    }

    public static void infoSRCMsgTest_Receiver(RTPSMessage msg) {

        GUIDPrefix prefix = new GUIDPrefix();

        RTPSMessageBuilder.addSubmessageInfoSRC(msg, new ProtocolVersion((byte) 2, (byte) 1), new VendorId((byte) 1, (byte) 15), prefix);

        msg.serialize();

        MessageReceiver receiver = new MessageReceiver(128);
        RTPSMessage outMsg = new RTPSMessage(0, null);
        outMsg.setBuffer(msg.getBuffer());
        outMsg.initBinaryOutputStream();

        receiver.processCDRMessage(new GUIDPrefix(), new Locator(), outMsg);

        if (outMsg.equals(msg)) {
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
        }

    }

    public static void dataMsgTest(RTPSMessage msg) {

        RTPSMessageBuilder.addSubmessageInfoTSNow(msg, false);

        CacheChange cc = new CacheChange();
        GUID wguid = new GUID();
        wguid.setEntityId(new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER));
        cc.setWriterGUID(wguid);
        cc.setSequenceNumber(new SequenceNumber(0, 1));

        SerializedPayload payload = new SerializedPayload();
        InnerStruct st = new InnerStruct();
        st.setInnerLongAtt(5);
        st.setInnerStringAtt("Hello World");
        payload.setData(st, (short) 19); // TODO Return max CDR size
        payload.setEncapsulationKind(EncapsulationKind.CDR_BE);
        cc.setSerializedPayload(payload);

        // Key
        //RTPSMessageBuilder.addSubmessageData(msg, cc, TopicKind.WITH_KEY, new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), false, null);
        // Data
        RTPSMessageBuilder.addSubmessageData(msg, cc, TopicKind.NO_KEY, new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), false, null);
        /*RTPSMessageBuilder.addSubmessageData(msg, cc, TopicKind.NO_KEY, new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), false, null);
		RTPSMessageBuilder.addSubmessageData(msg, cc, TopicKind.NO_KEY, new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), false, null);
		RTPSMessageBuilder.addSubmessageData(msg, cc, TopicKind.NO_KEY, new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), false, null);
		RTPSMessageBuilder.addSubmessageData(msg, cc, TopicKind.NO_KEY, new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), false, null);
		RTPSMessageBuilder.addSubmessageData(msg, cc, TopicKind.NO_KEY, new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), false, null);*/

    }

    public static void dataInlineQosMsgTest(RTPSMessage msg) {

        RTPSMessageBuilder.addSubmessageInfoTSNow(msg, false);

        CacheChange cc = new CacheChange();
        GUID wguid = new GUID();
        wguid.setEntityId(new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER));
        cc.setWriterGUID(wguid);
        cc.setSequenceNumber(new SequenceNumber(0, 1));

        /*SerializedPayload payload = new SerializedPayload();
		InnerStruct st = new InnerStruct();
		st.setInnerLongAtt(5);
		st.setInnerStringAtt("Hello World");
		payload.setData(st, (short) 19); // TODO Return max CDR size
		payload.setEncapsulationKind(EncapsulationKind.CDR_BE);
		cc.setSerializedPayload(payload);*/

        ParameterList inlineQos = new ParameterList();
        inlineQos.addParameter(new ParameterKey(new InstanceHandle()));
        inlineQos.addParameter(new ParameterSentinel());


        // Key
        //RTPSMessageBuilder.addSubmessageData(msg, cc, TopicKind.WITH_KEY, new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), false, null);
        // Data
        RTPSMessageBuilder.addSubmessageData(msg, cc, TopicKind.WITH_KEY, new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), false, inlineQos);
        /*RTPSMessageBuilder.addSubmessageData(msg, cc, TopicKind.NO_KEY, new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), false, null);
		RTPSMessageBuilder.addSubmessageData(msg, cc, TopicKind.NO_KEY, new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), false, null);
		RTPSMessageBuilder.addSubmessageData(msg, cc, TopicKind.NO_KEY, new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), false, null);
		RTPSMessageBuilder.addSubmessageData(msg, cc, TopicKind.NO_KEY, new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), false, null);
		RTPSMessageBuilder.addSubmessageData(msg, cc, TopicKind.NO_KEY, new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), false, null);*/

    }

    public static void dataInlineQosMsgTest_Receiver(RTPSMessage msg) {

        RTPSMessageBuilder.addSubmessageInfoTSNow(msg, false);

        CacheChange cc = new CacheChange();
        GUID wguid = new GUID();
        wguid.setEntityId(new EntityId(EntityIdEnum.ENTITYID_SPDP_BUILTIN_RTPSPARTICIPANT_WRITER));
        cc.setWriterGUID(wguid);

        cc.setSequenceNumber(new SequenceNumber(0, 1));

        SerializedPayload payload = new SerializedPayload();
        InnerStruct st = new InnerStruct();
        st.setInnerLongAtt(5);
        st.setInnerStringAtt("Hello World");
        payload.setData(st, (short) 19); // TODO Return max CDR size
        payload.setEncapsulationKind(EncapsulationKind.CDR_BE);
        cc.setSerializedPayload(payload);

        /*ParameterList inlineQos = new ParameterList();
		inlineQos.addParameter(new ParameterKey(new InstanceHandle()));
		inlineQos.addParameter(new ParameterSentinel());*/


        RTPSMessageBuilder.addSubmessageData(msg, cc, TopicKind.NO_KEY, new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), false, /*inlineQos*/ null);

        msg.serialize();

        MessageReceiver receiver = new MessageReceiver(128);

        RTPSMessage outMsg = new RTPSMessage(0, null);
        outMsg.setBuffer(msg.getBuffer());
        outMsg.initBinaryOutputStream();

        receiver.processCDRMessage(new GUIDPrefix(), new Locator(), outMsg);

        cc.getSerializedPayload().setData(st);

        try {
            cc.getSerializedPayload().deserializeData();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (outMsg.equals(msg)) {
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
        }


    }

    public static void heartbeatMsgTest(RTPSMessage msg) {

        RTPSMessageBuilder.addSubmessageHeartbeat(
                msg, 
                new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), 
                //new EntityId(3074), 
                new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_TOPIC_WRITER),
                new SequenceNumber(0, 1), 
                new SequenceNumber(0, 4), 
                new Count(1), 
                false, 
                false
                );

    }

    public static void heartbeatMsgTest_Receiver(RTPSMessage msg) {

        RTPSMessageBuilder.addSubmessageHeartbeat(
                msg, 
                new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), 
                //new EntityId(3074), 
                new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_TOPIC_WRITER),
                new SequenceNumber(0, 1), 
                new SequenceNumber(0, 4), 
                new Count(1), 
                false, 
                false
                );

        System.out.println("");

        msg.serialize();

        MessageReceiver receiver = new MessageReceiver(128);

        RTPSMessage outMsg = new RTPSMessage(0, null);
        outMsg.setBuffer(msg.getBuffer());
        outMsg.initBinaryOutputStream();

        receiver.processCDRMessage(new GUIDPrefix(), new Locator(), outMsg);

        if (outMsg.equals(msg)) {
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
        }

    }

    public static void acknackMsgTest(RTPSMessage msg) {

        SequenceNumberSet snSet = new SequenceNumberSet();
        snSet.setBase(new SequenceNumber(0, 1));
        snSet.add(new SequenceNumber(0, 5));
        snSet.add(new SequenceNumber(0, 8));
        snSet.add(new SequenceNumber(0, 35));
        /*snSet.add(new SequenceNumber(0, 8));
		snSet.add(new SequenceNumber(0, 12));
		snSet.add(new SequenceNumber(0, 20));*/


        RTPSMessageBuilder.addSubmessageAckNack(
                msg, 
                //new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), 
                //new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_TOPIC_WRITER),
                new EntityId(3074),
                new EntityId(3074),
                snSet, 
                new Count(1), 
                false
                );

    }

    public static void acknackMsgTest_Receiver(RTPSMessage msg) {

        SequenceNumberSet snSet = new SequenceNumberSet();
        snSet.setBase(new SequenceNumber(0, 1));
        snSet.add(new SequenceNumber(0, 5));
        snSet.add(new SequenceNumber(0, 8));
        snSet.add(new SequenceNumber(0, 35));
        /*snSet.add(new SequenceNumber(0, 8));
		snSet.add(new SequenceNumber(0, 12));
		snSet.add(new SequenceNumber(0, 20));*/


        RTPSMessageBuilder.addSubmessageAckNack(
                msg, 
                //new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), 
                //new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_TOPIC_WRITER),
                new EntityId(3074),
                new EntityId(3074),
                snSet, 
                new Count(1), 
                false
                );

        msg.serialize();

        MessageReceiver receiver = new MessageReceiver(128);
        RTPSMessage outMsg = new RTPSMessage(0, null);
        outMsg.setBuffer(msg.getBuffer());
        outMsg.initBinaryOutputStream();

        receiver.processCDRMessage(new GUIDPrefix(), new Locator(), outMsg);

        if (outMsg.equals(msg)) {
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
        }

    }

    public static void gapMsgTest(RTPSMessage msg) {

        SequenceNumberSet snSet = new SequenceNumberSet();
        snSet.setBase(new SequenceNumber(0, 1));
        snSet.add(new SequenceNumber(0, 5));
        snSet.add(new SequenceNumber(0, 8));
        snSet.add(new SequenceNumber(0, 35));
        /*snSet.add(new SequenceNumber(0, 8));
		snSet.add(new SequenceNumber(0, 12));
		snSet.add(new SequenceNumber(0, 20));*/


        RTPSMessageBuilder.addSubmessageGap(
                msg, 
                //new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), 
                //new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_TOPIC_WRITER),
                new SequenceNumber(0, 1),
                snSet,
                new EntityId(3074),
                new EntityId(3074)

                );

    }

    public static void gapMsgTest_Receiver(RTPSMessage msg) {

        SequenceNumberSet snSet = new SequenceNumberSet();
        snSet.setBase(new SequenceNumber(0, 1));
        snSet.add(new SequenceNumber(0, 5));
        snSet.add(new SequenceNumber(0, 8));
        snSet.add(new SequenceNumber(0, 35));
        /*snSet.add(new SequenceNumber(0, 8));
		snSet.add(new SequenceNumber(0, 12));
		snSet.add(new SequenceNumber(0, 20));*/


        RTPSMessageBuilder.addSubmessageGap(
                msg, 
                //new EntityId(EntityIdEnum.ENTITYID_UNKNOWN), 
                //new EntityId(EntityIdEnum.ENTITYID_SEDP_BUILTIN_TOPIC_WRITER),
                new SequenceNumber(0, 1),
                snSet,
                new EntityId(3074),
                new EntityId(3074)

                );

        msg.serialize();

        MessageReceiver receiver = new MessageReceiver(128);
        RTPSMessage outMsg = new RTPSMessage(0, null);
        outMsg.setBuffer(msg.getBuffer());
        outMsg.initBinaryOutputStream();

        receiver.processCDRMessage(new GUIDPrefix(), new Locator(), outMsg);

        if (outMsg.equals(msg)) {
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
        }

    }

    public static void padMsgTest(RTPSMessage msg) {
        RTPSMessageBuilder.addSubmessagePad(msg, (short) 5);
        RTPSMessageBuilder.addSubmessagePad(msg, (short) 5);
        msg.serialize();
        System.out.println("");
    }

    public static void padMsgTest_Receiver(RTPSMessage msg) {
        RTPSMessageBuilder.addSubmessagePad(msg, (short) 5);
        RTPSMessageBuilder.addSubmessagePad(msg, (short) 5);
        msg.serialize();

        MessageReceiver receiver = new MessageReceiver(128);

        RTPSMessage outMsg = new RTPSMessage(0, null);
        outMsg.setBuffer(msg.getBuffer());
        outMsg.initBinaryOutputStream();

        receiver.processCDRMessage(new GUIDPrefix(), new Locator(), outMsg);

        if (outMsg.equals(msg)) {
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
        }
    }

    public static void testHistoryCacheCreation() {
        HistoryCacheAttributes attr = new HistoryCacheAttributes();
        attr.payloadMaxSize = 50;
        attr.initialReservedCaches = 100;
        attr.maximumReservedCaches = 500;
        HistoryCache hCache = new ReaderHistoryCache(attr);
        System.out.println("HistorySize: " + hCache.getHistorySize());
        System.out.println("History.isFull: " + hCache.isFull());
    }

    public static void testHistoryCacheReserveAndRelease() {
        HistoryCacheAttributes attr = new HistoryCacheAttributes();
        attr.payloadMaxSize = 50;
        attr.initialReservedCaches = 100;
        attr.maximumReservedCaches = 500;
        HistoryCache history = new WriterHistoryCache(attr);
        CacheChange change = history.reserveCache();
        history.releaseCache(change);
        System.out.println("HistorySize: " + history.getHistorySize());
        System.out.println("History.isFull: " + history.isFull());
    }

    public static void locatorListReserve() {
        LocatorList list = new LocatorList();
        list.pushBack(new Locator());
        list.pushBack(new Locator());
        list.pushBack(new Locator());
        list.pushBack(new Locator());
        list.pushBack(new Locator());

        list.reserve(20);
    }

    public static void locatorListResize() {
        LocatorList list = new LocatorList();
        list.pushBack(new Locator());
        list.pushBack(new Locator());
        list.pushBack(new Locator());
        list.pushBack(new Locator());
        list.pushBack(new Locator());

        list.reserve(20);
        list.reserve(3);
        list.resize(3);
        list.resize(10);
    }

}
