package org.fiware.kiara.ps.rtps.builtin.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.qos.ParameterPropertyList;
import org.fiware.kiara.ps.qos.QosList;
import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.qos.policies.QosPolicy;
import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.timedevent.RemoteParticipantLeaseDuration;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.messages.elements.Count;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.ParameterList;
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
    
    private RemoteParticipantLeaseDuration m_leaseDurationTimer;
    
    private boolean m_isAlive;
    
    private boolean m_hasChanged;
    
    private QosList m_QosList;
    
    private List<Byte> m_userData;
    
    private ParameterPropertyList m_properties;
    
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
        this.m_properties = new ParameterPropertyList();
        this.m_userData = new ArrayList<Byte>();
    }

    public boolean initializeData(RTPSParticipant participant, PDPSimple pdpSimple) {
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
            } /*else  if (i >= 16) { // TODO Check this
                this.m_key.setValue(i, this.m_guid.getEntityId().getValue(i));
            }*/
        }
        
        this.m_metatrafficMulticastLocatorList = pdpSimple.getBuiltinProtocols().getMetatrafficMulticastLocatorList();
        this.m_metatrafficUnicastLocatorList = pdpSimple.getBuiltinProtocols().getMetatrafficUnicastLocatorList();
        
        this.m_participantName = participant.getAttributes().getName();

        this.m_userData = participant.getAttributes().userData;
        
        return true;
        
    }

    public ParameterList toParameterList() {
        if (this.m_hasChanged) {
            this.m_QosList.getAllQos().deleteParams();
            this.m_QosList.getAllQos().resetList();
            this.m_QosList.getInlineQos().deleteParams();
            this.m_QosList.getInlineQos().resetList();
            
            boolean valid = true;
            
            valid &= this.m_QosList.addQos(ParameterId.PID_PROTOCOL_VERSION, this.m_protocolVersion);
            valid &= this.m_QosList.addQos(ParameterId.PID_VENDORID,this.m_vendorId);
            
            if (this.m_expectsInlineQos) {
                valid &= this.m_QosList.addQos(ParameterId.PID_EXPECTS_INLINE_QOS, this.m_expectsInlineQos);
            }
            
            valid &= this.m_QosList.addQos(ParameterId.PID_VENDORID, this.m_vendorId);
            
            for (Locator lit : this.m_metatrafficMulticastLocatorList.getLocators()) {
                valid &= this.m_QosList.addQos(ParameterId.PID_METATRAFFIC_MULTICAST_LOCATOR, lit);
            }
            
            for (Locator lit : this.m_metatrafficUnicastLocatorList.getLocators()) {
                valid &= this.m_QosList.addQos(ParameterId.PID_METATRAFFIC_UNICAST_LOCATOR, lit);
            }
            
            for (Locator lit : this.m_defaultUnicastLocatorList.getLocators()) {
                valid &= this.m_QosList.addQos(ParameterId.PID_DEFAULT_UNICAST_LOCATOR, lit);
            }
            
            for (Locator lit : this.m_defaultMulticastLocatorList.getLocators()) {
                valid &= this.m_QosList.addQos(ParameterId.PID_DEFAULT_MULTICAST_LOCATOR, lit);
            }
            
            valid &= this.m_QosList.addQos(ParameterId.PID_PARTICIPANT_LEASE_DURATION, this.m_leaseDuration);
            valid &= this.m_QosList.addQos(ParameterId.PID_BUILTIN_ENDPOINT_SET, this.m_availableBuiltinEndpoints);
            valid &= this.m_QosList.addQos(ParameterId.PID_ENTITY_NAME, this.m_participantName);
            
            if (this.m_properties.m_properties.size() > 0) {
                valid &= this.m_QosList.addQos(ParameterId.PID_PROPERTY_LIST, this.m_properties);
            }
            
            if (valid) {
                this.m_hasChanged = false;
            }
            
            return this.m_QosList.getAllQos();
            
        }
        return null;
    }
    
    public void clear() {
        this.m_protocolVersion = new ProtocolVersion();
        this.m_guid = new GUID();
        this.m_vendorId = new VendorId().setVendorUnknown();
        this.m_expectsInlineQos = false;
        this.m_availableBuiltinEndpoints = 0;
        this.m_metatrafficMulticastLocatorList.clear();
        this.m_metatrafficUnicastLocatorList.clear();
        this.m_defaultMulticastLocatorList.clear();
        this.m_defaultUnicastLocatorList.clear();
        this.m_manualLivelinessCount = new Count(0);
        this.m_participantName = "";
        this.m_key = new InstanceHandle();
        this.m_leaseDuration = new Timestamp();
        this.m_isAlive = true;
        this.m_QosList.getAllQos().deleteParams();
        this.m_QosList.getAllQos().resetList();
        this.m_QosList.getInlineQos().resetList();
        this.m_properties.m_properties.clear();
        this.m_userData.clear();
    }

    public void copy(ParticipantProxyData pdata) {
        this.m_protocolVersion.copy(pdata.m_protocolVersion);
        this.m_guid.copy(pdata.m_guid);
        this.m_vendorId.copy(pdata.m_vendorId);
        this.m_availableBuiltinEndpoints = pdata.m_availableBuiltinEndpoints;
        this.m_metatrafficUnicastLocatorList.copy(pdata.m_metatrafficUnicastLocatorList);
        this.m_metatrafficMulticastLocatorList.copy(pdata.m_metatrafficMulticastLocatorList);
        this.m_defaultMulticastLocatorList.copy(pdata.m_defaultMulticastLocatorList);
        this.m_defaultUnicastLocatorList.copy(pdata.m_defaultUnicastLocatorList);
        this.m_manualLivelinessCount = pdata.m_manualLivelinessCount;
        this.m_participantName = pdata.m_participantName;
        this.m_leaseDuration.copy(pdata.m_leaseDuration);
        this.m_key.copy(pdata.m_key);
        this.m_isAlive = pdata.m_isAlive;
        this.m_properties.copy(pdata.m_properties);
        this.m_userData.clear();
        this.m_userData.addAll(pdata.m_userData);
    }
    
    public boolean updateData(ParticipantProxyData pdata) {
        this.m_metatrafficUnicastLocatorList.copy(pdata.m_metatrafficUnicastLocatorList);
        this.m_metatrafficMulticastLocatorList.copy(pdata.m_metatrafficMulticastLocatorList);
        this.m_defaultMulticastLocatorList.copy(pdata.m_defaultMulticastLocatorList);
        this.m_defaultUnicastLocatorList.copy(pdata.m_defaultUnicastLocatorList);
        this.m_manualLivelinessCount = pdata.m_manualLivelinessCount;
        this.m_properties.copy(pdata.m_properties);
        this.m_leaseDuration.copy(pdata.m_leaseDuration);
        this.m_userData.clear();
        this.m_userData.addAll(pdata.m_userData);
        this.m_isAlive = true;
        if (this.m_leaseDurationTimer != null && this.m_leaseDurationTimer.isWaiting()) {
            this.m_leaseDurationTimer.stopTimer();
            this.m_leaseDurationTimer.updateIntervalMillisec(this.m_leaseDuration.toMilliSecondsDouble());
            this.m_leaseDurationTimer.restartTimer();
        }
        return true;
    }

    public int getAvailableBuiltinEndpoints() {
        return this.m_availableBuiltinEndpoints;
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
