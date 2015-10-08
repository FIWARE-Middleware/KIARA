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
package org.fiware.kiara.ps.rtps.common;

import java.util.Arrays;

/**
 * Class Locator, uniquely identifies a address+port combination.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class Locator {

    /**
     * A {@link LocatorKind} object indicating the kind of the Locator
     */
    private LocatorKind m_kind;
    
    /**
     * Integer value representing the Locator port
     */
    private int m_port;
    
    /**
     * IP Address of the Locator
     */
    private byte[] m_address;

    /**
     * Initialized the Locator attributes to their default values
     */
    private void initAddr() {
        this.m_address = new byte[16];
        for (int i=0; i < 16; ++i) {
            this.m_address[i] = (byte) 0;
        }
    }

    /**
     * Default {@link Locator} constructor
     */
    public Locator() {
        this.m_kind = LocatorKind.LOCATOR_KIND_UDPv4;
        this.m_port = 0;
        this.initAddr();
    }

    /**
     * Alternative {@link Locator} constructor
     * 
     * @param port The {@link Locator} port
     */
    public Locator(int port) {
        this.m_kind = LocatorKind.LOCATOR_KIND_UDPv4;
        this.m_port = port;
        this.initAddr();
    }

    /**
     * Alternative {@link Locator} copy constructor
     * 
     * @param other The {@link Locator} to be copied
     */
    public Locator(Locator other) {
        this.m_kind = other.m_kind;
        this.m_port = other.m_port;
        this.m_address = new byte[other.m_address.length];
        System.arraycopy(other.m_address, 0, this.m_address, 0, other.m_address.length);
    }

    /**
     * Get the validation status of the locator
     * 
     * @return True if the Locator is valid
     */
    public boolean isValid() {
        return this.m_port >= 0;
    }

    /**
     * Get the LocatorKind value
     * 
     * @return The kind of the Locator
     */
    public LocatorKind getKind() {
        return m_kind;
    }

    /**
     * Set the LocatorKind value 
     * 
     * @param m_kind LocatorKind to be set
     */
    public void setKind(LocatorKind m_kind) {
        this.m_kind = m_kind;
    }

    /**
     * Get the Locator port
     * 
     * @return The Locator port
     */
    public int getPort() {
        return m_port;
    }

    /**
     * Set the Locator port
     * 
     * @param m_port The port to be set
     */
    public void setPort(int m_port) {
        this.m_port = m_port;
    }

    /**
     * Increases the value of the port in 1 unit
     */
    public void increasePort() {
        ++this.m_port;
    }

    /**
     * Get the Locator address
     * 
     * @return The Locator address
     */
    public byte[] getAddress() {
        return m_address;
    }

    /**
     * Set the Locator address
     * 
     * @param m_address The address to be set
     */
    public void setAddress(byte[] m_address) {
        this.m_address = m_address;
    }

    /**
     * Set the Locator address (in String format) as an IPv4 address
     * 
     * @param m_address The address to be set (in String format)
     */
    public void setIPv4Address(String m_address) {
        String[] splitted = m_address.split("\\.");
        if (splitted.length != 4) {
            return;
        }
        this.m_address[12] = (byte) Integer.parseInt(splitted[0]);
        this.m_address[13] = (byte) Integer.parseInt(splitted[1]);
        this.m_address[14] = (byte) Integer.parseInt(splitted[2]);
        this.m_address[15] = (byte) Integer.parseInt(splitted[3]);
    }

    /**
     * Compares two Locator instances 
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Locator) {
            Locator loc = (Locator) other;
            return Arrays.equals(this.m_address, loc.m_address) && this.m_kind == loc.m_kind && this.m_port == loc.m_port;
        }
        return false;
    }

    /**
     * Returns whether the address of the Locator has been defined or not.
     * 
     * @return true if the address has been defined; false otherwise.
     */
    public boolean isAddressDefined() {
        if (this.m_address.length == 16) {
            if (this.m_kind == LocatorKind.LOCATOR_KIND_UDPv4) {
                for (byte i = 12; i < 16; i++) {
                    if (this.m_address[i] != 0) {
                        return true;
                    }
                }
            } else if (this.m_kind == LocatorKind.LOCATOR_KIND_UDPv6) {
                for (byte i = 0; i < 16; i++) {
                    if (this.m_address[i] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Converts the address into its String IPv4 representation
     * 
     * @return String IPv4 representation of the address
     */
    public String toIPv4String() {
        return new String(
                (this.m_address[12] & 0xFF) + "." + 
                (this.m_address[13] & 0xFF) + "." + 
                (this.m_address[14] & 0xFF) + "." + 
                (this.m_address[15] & 0xFF));
    }

    /**
     * Converts the address into its String IPv5 representation
     * 
     * @return String IPv5 representation of the address
     */
    public String toIPv6String() {
        return new String(
                (this.m_address[0] & 0xFF) + "." + 
                (this.m_address[1] & 0xFF) + "." + 
                (this.m_address[2] & 0xFF) + "." + 
                (this.m_address[3] & 0xFF) + "." + 
                (this.m_address[4] & 0xFF) + "." + 
                (this.m_address[5] & 0xFF) + "." + 
                (this.m_address[6] & 0xFF) + "." + 
                (this.m_address[7] & 0xFF) + "." + 
                (this.m_address[8] & 0xFF) + "." + 
                (this.m_address[9] & 0xFF) + "." + 
                (this.m_address[10] & 0xFF) + "." + 
                (this.m_address[11] & 0xFF) + "." + 
                (this.m_address[12] & 0xFF) + "." + 
                (this.m_address[13] & 0xFF) + "." + 
                (this.m_address[14] & 0xFF) + "." + 
                (this.m_address[15] & 0xFF));
    }

    /**
     * Copies the content of the Locator reference
     * 
     * @param locator A reference to other Locator
     */
    public void copy(Locator locator) {
        m_kind = locator.m_kind;
        m_port = locator.m_port;
        System.arraycopy(locator.m_address, 0, this.m_address, 0, locator.m_address.length);
    }

    /**
     * Converts the Locator into its String representation
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (this.m_kind == LocatorKind.LOCATOR_KIND_UDPv4) {
            sb.append(this.toIPv4String());
            sb.append(":" + this.m_port);
            sb.append(" (UDPv4)");
        } else if (this.m_kind == LocatorKind.LOCATOR_KIND_UDPv6) {
            sb.append(this.toIPv6String());
            sb.append(":" + this.m_port);
            sb.append(" (UDPv6)");
        }

        return sb.toString();
    }

}
