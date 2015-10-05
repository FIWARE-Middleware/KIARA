package org.fiware.kiara.ps.rtps.builtin.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.qos.QosList;
import org.fiware.kiara.ps.qos.parameter.ParameterId;
import org.fiware.kiara.ps.qos.policies.UserDataQosPolicy;
import org.fiware.kiara.ps.rtps.attributes.RemoteReaderAttributes;
import org.fiware.kiara.ps.rtps.attributes.RemoteWriterAttributes;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.PDPSimple;
import org.fiware.kiara.ps.rtps.builtin.discovery.participant.timedevent.RemoteParticipantLeaseDuration;
import org.fiware.kiara.ps.rtps.common.Locator;
import org.fiware.kiara.ps.rtps.common.LocatorList;
import org.fiware.kiara.ps.rtps.history.CacheChange;
import org.fiware.kiara.ps.rtps.messages.elements.Count;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.InstanceHandle;
import org.fiware.kiara.ps.rtps.messages.elements.Parameter;
import org.fiware.kiara.ps.rtps.messages.elements.ParameterList;
import org.fiware.kiara.ps.rtps.messages.elements.ProtocolVersion;
import org.fiware.kiara.ps.rtps.messages.elements.SerializedPayload;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;
import org.fiware.kiara.ps.rtps.messages.elements.VendorId;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterBool;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterBuiltinEndpointSet;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterGuid;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterKey;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterLocator;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterPropertyList;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterProtocolVersion;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterString;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterTime;
import org.fiware.kiara.ps.rtps.messages.elements.parameters.ParameterVendorId;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.serialization.impl.BinaryInputStream;

/**
 * ParticipantProxyData class is used to store and convert the information
 * Participants send to each other during the PDP phase.
 */
public class ParticipantProxyData {

    public static final int DISCOVERY_PARTICIPANT_DATA_MAX_SIZE = 5000;
    public static final int DISCOVERY_TOPIC_DATA_MAX_SIZE = 500;
    public static final int DISCOVERY_PUBLICATION_DATA_MAX_SIZE = 5000;
    public static final int DISCOVERY_SUBSCRIPTION_DATA_MAX_SIZE = 5000;
    public static final int BUILTIN_PARTICIPANT_DATA_MAX_SIZE = 100;

    public static final int DISC_BUILTIN_ENDPOINT_PARTICIPANT_ANNOUNCER = 0x00000001 << 0;
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

    /**
     * Protocol version
     */
    private ProtocolVersion m_protocolVersion;

    /**
     * GUID
     */
    private GUID m_guid;

    /**
     * Vendor ID
     */
    private VendorId m_vendorId;

    /**
     * Expects Inline QOS.
     */
    private boolean m_expectsInlineQos;

    /**
     * Available builtin endpoints
     */
    private int m_availableBuiltinEndpoints;

    /**
     * Metatraffic unicast locator list
     */
    private LocatorList m_metatrafficUnicastLocatorList;

    /**
     * Metatraffic multicast locator list
     */
    private LocatorList m_metatrafficMulticastLocatorList;

    /**
     * Default unicast locator list
     */
    private LocatorList m_defaultUnicastLocatorList;

    /**
     * Default multicast locator list
     */
    private LocatorList m_defaultMulticastLocatorList;

    /**
     * Manual liveliness count
     */
    private Count m_manualLivelinessCount;

    /**
     * Participant name
     */
    private String m_participantName;

    private InstanceHandle m_key;

    private Timestamp m_leaseDuration;

    private RemoteParticipantLeaseDuration m_leaseDurationTimer;

    private boolean m_isAlive;

    private boolean m_hasChanged;

    private final QosList m_QosList;

    private List<Byte> m_userData;

    private ParameterPropertyList m_properties;

    private final List<ReaderProxyData> m_readers;

    private final List<WriterProxyData> m_writers;

    private final List<RemoteReaderAttributes> m_builtinReaders;

    private final List<RemoteWriterAttributes> m_builtinWriters;

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
        this.m_readers = new ArrayList<>();
        this.m_writers = new ArrayList<>();
        this.m_builtinReaders = new ArrayList<>();
        this.m_builtinWriters = new ArrayList<>();
        this.m_key = new InstanceHandle();
        this.m_properties = new ParameterPropertyList();
        this.m_userData = new ArrayList<>();
        this.m_leaseDuration = new Timestamp();
    }

    /**
     * Initialize the object with the data of the lcoal RTPSParticipant.
     *
     * @param participant Reference to the RTPSParticipant.
     * @param pdpSimple Reference to the PDPSimple object.
     * @return True if correctly initialized.
     */
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

        this.m_defaultUnicastLocatorList = participant.getDefaultUnicastLocatorList();
        this.m_defaultMulticastLocatorList = participant.getDefaultMulticastLocatorList();
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

    /**
     * Convert information to parameter list.
     *
     * @return True on success
     */
    public ParameterList toParameterList() {
        this.m_mutex.lock();
        try {
            if (this.m_hasChanged) {
                this.m_QosList.getAllQos().deleteParams();
                this.m_QosList.getAllQos().resetList();
                this.m_QosList.getInlineQos().deleteParams();
                this.m_QosList.getInlineQos().resetList();

                boolean valid = true;

                valid &= this.m_QosList.addQos(ParameterId.PID_PROTOCOL_VERSION, this.m_protocolVersion);
                valid &= this.m_QosList.addQos(ParameterId.PID_VENDORID, this.m_vendorId);

                if (this.m_expectsInlineQos) {
                    valid &= this.m_QosList.addQos(ParameterId.PID_EXPECTS_INLINE_QOS, this.m_expectsInlineQos);
                }

                valid &= this.m_QosList.addQos(ParameterId.PID_PARTICIPANT_GUID, this.m_guid);

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

                if (this.m_properties.getProperties().size() > 0) {
                    valid &= this.m_QosList.addQos(ParameterId.PID_PROPERTY_LIST, this.m_properties);
                }

                this.m_QosList.getAllQos().addSentinel();

                if (valid) {
                    this.m_hasChanged = false;
                }

                return this.m_QosList.getAllQos();

            }
            return null;
        } finally {
            this.m_mutex.unlock();
        }
    }

    /**
     * Read the parameter list from a received CDRMessage
     *
     * @param change cache change
     * @return True on success
     */
    public synchronized boolean readFromCDRMessage(CacheChange change) {
        try {
            SerializedPayload payload = change.getSerializedPayload();
            payload.updateSerializer();
            BinaryInputStream bis = new BinaryInputStream(payload.getBuffer());
            ParameterList parameterList = new ParameterList();
            parameterList.deserialize(payload.getSerializer(), bis, "");
            for (Parameter param : parameterList.getParameters()) {
                switch (param.getParameterId()) {
                    case PID_KEY_HASH:
                        ParameterKey pKey = (ParameterKey) param;
                        GUID guid = pKey.getKey().toGUID();
                        this.m_guid = guid;
                        this.m_key = pKey.getKey();
                        break;
                    case PID_PROTOCOL_VERSION:
                        ParameterProtocolVersion pVersion = (ParameterProtocolVersion) param;
                        if (pVersion.getProtocolVersion().getMajor() < new ProtocolVersion().getMajor()) {
                            return false;
                        }
                        this.m_protocolVersion = pVersion.getProtocolVersion();
                        break;
                    case PID_VENDORID:
                        ParameterVendorId pVendorId = (ParameterVendorId) param;
                        this.m_vendorId = pVendorId.getVendorId();
                        break;
                    case PID_EXPECTS_INLINE_QOS:
                        ParameterBool pBool = (ParameterBool) param;
                        this.m_expectsInlineQos = pBool.getBool();
                        break;
                    case PID_PARTICIPANT_GUID:
                        ParameterGuid pGuid = (ParameterGuid) param;
                        this.m_guid = pGuid.getGUID();
                        this.m_key = pGuid.getGUID().toInstanceHandle();
                        break;
                    case PID_METATRAFFIC_MULTICAST_LOCATOR:
                        ParameterLocator pMetaMulticastLoc = (ParameterLocator) param;
                        this.m_metatrafficMulticastLocatorList.pushBack(pMetaMulticastLoc.getLocator());
                        break;
                    case PID_METATRAFFIC_UNICAST_LOCATOR:
                        ParameterLocator pMetaUnicastLoc = (ParameterLocator) param;
                        this.m_metatrafficUnicastLocatorList.pushBack(pMetaUnicastLoc.getLocator());
                        break;
                    case PID_DEFAULT_MULTICAST_LOCATOR:
                        ParameterLocator pDefaultUnicastLoc = (ParameterLocator) param;
                        this.m_defaultUnicastLocatorList.pushBack(pDefaultUnicastLoc.getLocator());
                        break;
                    case PID_DEFAULT_UNICAST_LOCATOR:
                        ParameterLocator pDefaultMulticastLoc = (ParameterLocator) param;
                        this.m_defaultUnicastLocatorList.pushBack(pDefaultMulticastLoc.getLocator());
                        break;
                    case PID_PARTICIPANT_LEASE_DURATION:
                        ParameterTime pTime = (ParameterTime) param;
                        this.m_leaseDuration = pTime.getTime();
                        break;
                    case PID_BUILTIN_ENDPOINT_SET:
                        ParameterBuiltinEndpointSet pBuiltin = (ParameterBuiltinEndpointSet) param;
                        this.m_availableBuiltinEndpoints = pBuiltin.getEndpointSet();
                        break;
                    case PID_ENTITY_NAME:
                        ParameterString pStr = (ParameterString) param;
                        this.m_participantName = pStr.getString();
                        break;
                    case PID_PROPERTY_LIST:
                        ParameterPropertyList pPropList = (ParameterPropertyList) param;
                        if (pPropList.getPropertyList() != null) {
                            this.m_properties = pPropList.getPropertyList();
                        }
                        break;
                    case PID_USER_DATA:
                        UserDataQosPolicy pQosPolicy = (UserDataQosPolicy) param;
                        this.m_userData = pQosPolicy.getDataBuf();
                        break;
                    default:
                        break;
                }
            }
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Clear the data (restore to default state.)
     */
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
        this.m_properties.getProperties().clear();
        this.m_userData.clear();
    }

    /**
     * Copy the data from another object.
     *
     * @param pdata Object to copy the data from
     */
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
        this.m_readers.addAll(pdata.getReaders());
        this.m_writers.addAll(pdata.getWriters());
    }

    /**
     * Update the data.
     *
     * @param pdata Object to copy the data from
     * @return True on success
     */
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

    /**
     * Returns number of available builtin endpoints
     *
     * @return number of available builtin endpoints
     */
    public int getAvailableBuiltinEndpoints() {
        return this.m_availableBuiltinEndpoints;
    }

    /**
     * Increases count of manual liveliness
     */
    public void increaseManualLivelinessCount() {
        this.m_manualLivelinessCount.increase();
    }

    /**
     * Returns key
     *
     * @return key
     * @see InstanceHandle
     */
    public InstanceHandle getKey() {
        return this.m_key;
    }

    /**
     * Returns QoS list
     *
     * @return QoS list
     * @see QosList
     */
    public QosList getQosList() {
        return this.m_QosList;
    }

    /**
     * Returns readers list
     *
     * @return readers list
     * @see ReaderProxyData
     */
    public List<ReaderProxyData> getReaders() {
        return this.m_readers;
    }

    /**
     * Returns writers list
     *
     * @return writers list
     * @see WriterProxyData
     */
    public List<WriterProxyData> getWriters() {
        return this.m_writers;
    }

    /**
     * Returns GUID
     *
     * @return GUID
     * @see GUID
     */
    public GUID getGUID() {
        return this.m_guid;
    }

    /**
     * Get metatraffic unicast locator list
     *
     * @return metatraffic unicast locator list
     */
    public LocatorList getMetatrafficUnicastLocatorList() {
        return this.m_metatrafficUnicastLocatorList;
    }

    /**
     * Get metatraffic multicast locator list
     *
     * @return metatraffic multicast locator list
     */
    public LocatorList getMetatrafficMulticastLocatorList() {
        return this.m_metatrafficMulticastLocatorList;
    }

    /**
     * Get default unicast locator list
     *
     * @return default unicast locator list
     */
    public LocatorList getDefaultUnicastLocatorList() {
        return m_defaultUnicastLocatorList;
    }

    /**
     * Get default multicast locator list
     *
     * @return default multicast locator list
     */
    public LocatorList getDefaultMulticastLocatorList() {
        return m_defaultMulticastLocatorList;
    }

    /**
     * Get list of builtin readers
     *
     * @return builtin readers list
     */
    public List<RemoteReaderAttributes> getBuiltinReaders() {
        return this.m_builtinReaders;
    }

    /**
     * Get list of builtin writers
     *
     * @return builtin writers list
     */
    public List<RemoteWriterAttributes> getBuiltinWriters() {
        return this.m_builtinWriters;
    }

    /**
     * Get mutex
     *
     * @return mutex
     */
    public Lock getMutex() {
        return this.m_mutex;
    }

    /**
     * Set isAlive flag
     *
     * @param isAlive flag
     */
    public void setIsAlive(boolean isAlive) {
        this.m_isAlive = isAlive;
    }

    /**
     * Get isAlive flag
     *
     * @return isAlive flag
     */
    public boolean getIsAlive() {
        return this.m_isAlive;
    }

    /**
     * Get participant name
     *
     * @return participant name
     */
    public String getParticipantName() {
        return this.m_participantName;
    }

    /**
     * Get hasChanged flag
     *
     * @return hasChanged flag
     */
    public boolean getHasChanged() {
        return m_hasChanged;
    }

    /**
     * Set hasChanged flag
     *
     * @param value hasChanged flag
     */
    public void setHasChanged(boolean value) {
        m_hasChanged = value;
    }

    /**
     * Get properties
     *
     * @return properties
     * @see ParameterPropertyList
     */
    public ParameterPropertyList getProperties() {
        return m_properties;
    }

    /**
     * Get user data
     *
     * @return user data
     */
    public List<Byte> getUserData() {
        return this.m_userData;
    }

    /**
     * Get lease duration timer
     *
     * @return lease duration timer
     * @see RemoteParticipantLeaseDuration
     */
    public RemoteParticipantLeaseDuration getLeaseDurationTimer() {
        return this.m_leaseDurationTimer;
    }

    /**
     * Set lease duration timer
     *
     * @param leaseDurationTimer lease duration timer
     */
    public void setLeaseDurationTimer(RemoteParticipantLeaseDuration leaseDurationTimer) {
        this.m_leaseDurationTimer = leaseDurationTimer;
    }

    /**
     * Get lease duration
     *
     * @return lease duration
     */
    public Timestamp getLeaseDuration() {
        return this.m_leaseDuration;
    }

    /**
     * Set lease duration
     *
     * @param leaseDuration lease duration
     */
    public void setLeaseDuration(Timestamp leaseDuration) {
        this.m_leaseDuration = leaseDuration;
    }

    public void destroy() {
        if (this.m_leaseDurationTimer != null) {
            this.m_leaseDurationTimer.delete();
        }
    }
}
