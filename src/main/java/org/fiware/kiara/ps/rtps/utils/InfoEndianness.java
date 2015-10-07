/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fiware.kiara.ps.rtps.utils;

import java.nio.ByteOrder;
import org.fiware.kiara.ps.rtps.messages.common.types.RTPSEndian;

/**
 * This class is used to get information about the endianness of the computer
 * this library in running in
 * 
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public class InfoEndianness {

    /**
     * Chacks the machine's endianness
     * 
     * @return The {@link RTPSEndian} representing the machine's endianness
     */
    public static RTPSEndian checkMachineEndianness() {
        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)) {
            return RTPSEndian.BIG_ENDIAN;
        } else {
            return RTPSEndian.LITTLE_ENDIAN;
        }
    }

}
