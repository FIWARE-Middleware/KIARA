package org.fiware.kiara.ps.rtps.utils;

import java.nio.ByteOrder;

import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;

public class InfoEndianness {

    public static RTPSEndian checkMachineEndianness() {
        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)) {
            System.out.println("Big-endian");
            return RTPSEndian.BIG_ENDIAN;
        } else {
            System.out.println("Little-endian");
            return RTPSEndian.LITTLE_ENDIAN;
        }
    }

}
