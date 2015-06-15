package org.fiware.kiara.ps.rtps.attributes;

import org.fiware.kiara.ps.rtps.common.DurabilityKind;
import org.fiware.kiara.ps.rtps.common.EndpointKind;
import org.fiware.kiara.ps.rtps.common.ReliabilityKind;

public class WriterAttributes {

    public EndpointAttributes endpointAtt;
    public WriterTimes times;

    public WriterAttributes() {
        this.endpointAtt = new EndpointAttributes();
        this.endpointAtt.endpointKind = EndpointKind.WRITER;
        this.endpointAtt.durabilityKind = DurabilityKind.TRANSIENT_LOCAL;
        this.endpointAtt.reliabilityKind = ReliabilityKind.RELIABLE;
    }

}
