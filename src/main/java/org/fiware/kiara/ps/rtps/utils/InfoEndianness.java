package org.fiware.kiara.ps.rtps.utils;

import java.nio.ByteOrder;

import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;

public class InfoEndianness {

    public static RTPSEndian checkMachineEndianness() {
        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)) {
            return RTPSEndian.BIG_ENDIAN;
        } else {
            return RTPSEndian.LITTLE_ENDIAN;
        }
    }

}
