package org.fiware.kiara.ps.rtps.builtin.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.qos.QosList;
import org.fiware.kiara.ps.qos.policies.QosPolicy;
import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.messages.elements.Count;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.ProtocolVersion;
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
    
    private int m_availableBuiltinEndpoints;
    
    private LocatorList m_metatrafficUnicastLocatorList;
    
    private LocatorList m_metatrafficMulticastLocatorList;
    
    private Count m_manualLivelinessCount;
    
    private InstanceHandle m_key;
    
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
        this.m_manualLivelinessCount = new Count(0);
        this.m_hasChanged = true;
        this.m_isAlive = false;
        this.m_QosList = new QosList();
        this.m_readers = new ArrayList<ReaderProxyData>();
        this.m_writers = new ArrayList<WriterProxyData>();
        this.m_builtinReaders = new ArrayList<RemoteReaderAttributes>();
        this.m_builtinWriters = new ArrayList<RemoteWriterAttributes>();
    }

    public void initializeData(RTPSParticipant m_RTPSParticipant,
            PDPSimple pdpSimple) {
        // TODO Auto-generated method stub
        
    }

    public void incrementManualLivelinessCount() {
        this.m_manualLivelinessCount.increase();
    }

    public InstanceHandle getKey() {
        return this.m_key;
    }

    public boolean toParameterList() {
        // TODO Implement
        if (this.m_hasChanged) {
            
        }
        return false;
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

}
