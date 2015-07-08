package org.fiware.kiara.ps.rtps.builtin.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.qos.QosList;
import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.qos.policies.QosPolicy;
import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.messages.elements.Count;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.ProtocolVersion;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.messages.elements.VendorId;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;

public class ParticipantProxyData {
    
    public static final int DISCOVERY_PARTICIPANT_DATA_MAX_SIZE = 5000;
    public static final int DISCOVERY_TOPIC_DATA_MAX_SIZE = 500;
    public static final int DISCOVERY_PUBLICATION_DATA_MAX_SIZE = 5000;
    public static final int DISCOVERY_SUBSCRIPTION_DATA_MAX_SIZE = 5000;
    public static final int BUILTIN_PARTICIPANT_DATA_MAX_SIZE = 100;
    
    public static final int DISC_BUILTIN_ENDPOINT_PARTICIPANT_ANNOUNCER  = 0x00000001 << 0;
    public static final int DISC_BUILTIN_ENDPOINT_PARTICIPANT_DETECTOR = 0x00000001 << 1;
    public static final int DISC_BUILTIN_ENDPOINT_PUBLICATION_ANNOUNCER = 0x00000001 << 2;
    public static final int DISC_BUILTIN_ENDPOINT_PUBLICATION_DETECTOR = 0x00000001 << 3;
    public static final int DISC_BUILTIN_ENDPOINT_SUBSCRIPTION_ANNOUNCER = 0x00000001 << 4;
    public static final int DISC_BUILTIN_ENDPOINT_SUBSCRIPTION_DETECTOR = 0x00000001 << 5;
    public static final int DISC_BUILTIN_ENDPOINT_PARTICIPANT_PROXY_ANNOUNCER = 0x00000001 << 6;
    public static final int DISC_BUILTIN_ENDPOINT_PARTICIPANT_PROXY_DETECTOR = 0x00000001 << 7;
    public static final int DISC_BUILTIN_ENDPOINT_PARTICIPANT_STATE_ANNOUNCER = 0x00000001 << 8;
    public static final int DISC_BUILTIN_ENDPOINT_PARTICIPANT_STATE_DETECTOR = 0x00000001 << 9;
    public static final int BUILTIN_ENDPOINT_PARTICIPANT_MESSAGE_DATA_WRITER = 0x00000001 << 10;
    public static final int BUILTIN_ENDPOINT_PARTICIPANT_MESSAGE_DATA_READER = 0x00000001 << 11;
    
    private ProtocolVersion m_protocolVersion;
    
    private GUID m_guid;
    
    private VendorId m_vendorId;
    
    private boolean m_expectsInlineQos;
    
    private int m_availableBuiltinEndpoints;
    
    private LocatorList m_metatrafficUnicastLocatorList;
    
    private LocatorList m_metatrafficMulticastLocatorList;
    
    private LocatorList m_defaultUnicastLocatorList;
    
    private LocatorList m_defaultMulticastLocatorList;
    
    private Count m_manualLivelinessCount;
    
    private String m_participantName;
    
    private InstanceHandle m_key;
    
    private Timestamp m_leaseDuration;
    
    private boolean m_isAlive;
    
    private boolean m_hasChanged;
    
    private QosList m_QosList;
    
    private List<ReaderProxyData> m_readers;
    
    private List<WriterProxyData> m_writers;
    
    private List<RemoteReaderAttributes> m_builtinReaders;
    
    private List<RemoteWriterAttributes> m_builtinWriters;
    
    private final Lock m_mutex = new ReentrantLock(true);
    
    public ParticipantProxyData() {
        this.m_protocolVersion = new ProtocolVersion();
        this.m_guid = new GUID();
        this.m_vendorId = new VendorId();
        this.m_metatrafficUnicastLocatorList = new LocatorList();
        this.m_metatrafficMulticastLocatorList = new LocatorList();
        this.m_defaultUnicastLocatorList = new LocatorList();
        this.m_defaultMulticastLocatorList = new LocatorList();
        this.m_manualLivelinessCount = new Count(0);
        this.m_hasChanged = true;
        this.m_isAlive = false;
        this.m_expectsInlineQos = false;
        this.m_QosList = new QosList();
        this.m_readers = new ArrayList<ReaderProxyData>();
        this.m_writers = new ArrayList<WriterProxyData>();
        this.m_builtinReaders = new ArrayList<RemoteReaderAttributes>();
        this.m_builtinWriters = new ArrayList<RemoteWriterAttributes>();
        this.m_key = new InstanceHandle();
    }

    public void initializeData(RTPSParticipant participant, PDPSimple pdpSimple) {
        this.m_leaseDuration = participant.getAttributes().builtinAtt.leaseDuration;
        this.m_vendorId = new VendorId().setVendoreProsima();
        
        this.m_availableBuiltinEndpoints |= DISC_BUILTIN_ENDPOINT_PARTICIPANT_ANNOUNCER;
        this.m_availableBuiltinEndpoints |= DISC_BUILTIN_ENDPOINT_PARTICIPANT_DETECTOR;
        
        if (participant.getAttributes().builtinAtt.useWriterLP) {
            this.m_availableBuiltinEndpoints |= BUILTIN_ENDPOINT_PARTICIPANT_MESSAGE_DATA_WRITER;
            this.m_availableBuiltinEndpoints |= BUILTIN_ENDPOINT_PARTICIPANT_MESSAGE_DATA_READER;
        }
        
        if (participant.getAttributes().builtinAtt.useSimpleEDP) {
            if (participant.getAttributes().builtinAtt.simpleEDP.usePulicationWriterAndSubscriptionReader) {
                this.m_availableBuiltinEndpoints |= DISC_BUILTIN_ENDPOINT_PUBLICATION_ANNOUNCER;
                this.m_availableBuiltinEndpoints |= DISC_BUILTIN_ENDPOINT_SUBSCRIPTION_DETECTOR;
            }
            if (participant.getAttributes().builtinAtt.simpleEDP.usePulicationReaderAndSubscriptionWriter) {
                this.m_availableBuiltinEndpoints |= DISC_BUILTIN_ENDPOINT_PUBLICATION_DETECTOR;
                this.m_availableBuiltinEndpoints |= DISC_BUILTIN_ENDPOINT_SUBSCRIPTION_ANNOUNCER;
            }
        }
        
        this.m_defaultUnicastLocatorList = participant.getAttributes().defaultUnicastLocatorList;
        this.m_defaultMulticastLocatorList = participant.getAttributes().defaultMulticastLocatorList;
        this.m_expectsInlineQos = false;
        this.m_guid = participant.getGUID();
        for (int i = 0; i < 16; ++i) {
            if (i < 12) {
                this.m_key.setValue(i, this.m_guid.getGUIDPrefix().getValue(i));
            } /*else  if (i >= 16) {
                this.m_key.setValue(i, this.m_guid.getEntityId().getValue(i));
            }*/
        }
        
        this.m_metatrafficMulticastLocatorList = pdpSimple.getBuiltinProtocols().getMetatrafficMulticastLocatorList();
        this.m_metatrafficUnicastLocatorList = pdpSimple.getBuiltinProtocols().getMetatrafficUnicastLocatorList();
        
        this.m_participantName = participant.getAttributes().getName();
        //participant.getAttributes().
        // TODO User Data
        
    }

    public boolean toParameterList() {
        if (this.m_hasChanged) {
            this.m_QosList.getAllQos().deleteParams();
            this.m_QosList.getAllQos().resetList();
            this.m_QosList.getInlineQos().deleteParams();
            this.m_QosList.getInlineQos().resetList();
            
            boolean valid = this.m_QosList.addQos(ParameterId.PID_PROTOCOL_VERSION, this.m_protocolVersion);
        }
        return true;
    }

    public void increaseManualLivelinessCount() {
        this.m_manualLivelinessCount.increase();
    }

    public InstanceHandle getKey() {
        return this.m_key;
    }

    public QosList getQosList() {
        return this.m_QosList;
    }

    public List<ReaderProxyData> getReaders() {
        return this.m_readers;
    }

    public List<WriterProxyData> getWriters() {
        return this.m_writers;
    }
    
    public GUID getGUID() {
        return this.m_guid;
    }

    public void copy(ParticipantProxyData pit) {
        // TODO Implement
        
    }

    public int getAvailableBuiltinEndpoints() {
        // TODO Auto-generated method stub
        return 0;
    }

    public LocatorList getMetatrafficUnicastLocatorList() {
        return this.m_metatrafficUnicastLocatorList;
    }
    
    public LocatorList getMetatrafficMulticastLocatorList() {
        return this.m_metatrafficMulticastLocatorList;
    }
    
    public List<RemoteReaderAttributes> getBuiltinReaders() {
        return this.m_builtinReaders;
    }

    public List<RemoteWriterAttributes> getBuiltinWriters() {
        return this.m_builtinWriters;
    }

    public Lock getMutex() {
        return this.m_mutex;
    }

    public void setIsAlive(boolean isAlive) {
        this.m_isAlive = isAlive;
    }
    
    public boolean getIsAlive() {
        return this.m_isAlive;
    }
    
    public String getParticipantName() {
        return this.m_participantName;
    }

}
