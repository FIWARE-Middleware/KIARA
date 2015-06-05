package org.fiware.kiara.ps.participant;

import org.fiware.kiara.ps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;

public class ParticipantTest {
    
    public static void main(String[] args) {
        ParticipantAttributes pAtt = new ParticipantAttributes();
        pAtt.rtps.defaultSendPort = 11511;
        pAtt.rtps.useIPv4ToSend = true;
        pAtt.rtps.builtinAtt.useSimplePDP = true;
        pAtt.rtps.builtinAtt.useSimpleEDP = true;
        pAtt.rtps.builtinAtt.simpleEDP.usePulicationReaderAndSubscriptionWriter = true;
        pAtt.rtps.builtinAtt.simpleEDP.usePulicationWriterAndSubscriptionReader = true;
        pAtt.rtps.builtinAtt.domainID = 80;
        pAtt.rtps.builtinAtt.leaseDuration = new Timestamp().timeInfinite();
        pAtt.rtps.sendSocketBufferSize = 8712;
        pAtt.rtps.listenSocketBufferSize = 17424;
        pAtt.rtps.setName("Participant_pub");
        
        Participant participant = null;
    }

}
