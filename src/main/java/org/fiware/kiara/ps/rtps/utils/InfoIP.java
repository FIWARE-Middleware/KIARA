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

import org.fiware.kiara.ps.rtps.common.Locator;

/**
 * This class is used to retrieve IP adresses information.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class InfoIP {

    /**
     * {@link IPTYPE} indicating the IP address type
     */
    public IPTYPE type;
    
    /**
     * Scope Identifier
     */
    public int scopeId;
    
    /**
     * String indicating the name
     */
    public String name;
    
    /**
     * Associated {@link Locator}
     */
    public final Locator locator;

    /**
     * Default {@link InfoIP} constructor (IPv4, 0, "", null)
     */
    public InfoIP() {
        this(IPTYPE.IPv4, 0, "", null);
    }

    /**
     * Alternative {@link InfoIP} constructor
     * 
     * @param type The IP type
     * @param scopeId The scope identifier
     * @param name The IP name
     * @param locator The {@link Locator} associastes to this IP
     */
    public InfoIP(IPTYPE type, int scopeId, String name, Locator locator) {
        this.type = type;
        this.scopeId = scopeId;
        this.name = name;
        this.locator = locator == null ? new Locator() : locator;
    }
}
