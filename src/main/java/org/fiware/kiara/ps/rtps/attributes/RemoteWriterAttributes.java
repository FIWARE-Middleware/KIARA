package org.fiware.kiara.ps.rtps.attributes;

import org.fiware.kiara.ps.rtps.common.EndpointKind;
import org.fiware.kiara.ps.rtps.messages.elements.GUID;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;

/**
 * Class RemoteWriterAttributes, to define the attributes of a Remote Writer.
 */
public class RemoteWriterAttributes {

    /**
     * Attributes of the associated endpoint.
     */
    public EndpointAttributes endpoint;

    /**
     * {@link GUID} of the writer, can be unknown if the reader is best effort.
     */
    public GUID guid;

    /**
     * Liveliness lease duration, default value is infinite time.
     */
    public Timestamp livelinessLeaseDuration;

    /**
     * Ownership Strength of the associated writer.
     */
    public short ownershipStrength;

    public RemoteWriterAttributes() {
        this.endpoint = new EndpointAttributes();
        this.guid = new GUID();

        endpoint.endpointKind = EndpointKind.WRITER;
        livelinessLeaseDuration = new Timestamp().timeInfinite();
        this.ownershipStrength = 0;
    }

    public void setGUID(GUID other) {
        this.guid.copy(other);
    }

    public void copy(RemoteWriterAttributes value) {
        endpoint.copy(value.endpoint);
        guid.copy(value.guid);
        livelinessLeaseDuration.copy(value.livelinessLeaseDuration);
        ownershipStrength = value.ownershipStrength;
    }

}
