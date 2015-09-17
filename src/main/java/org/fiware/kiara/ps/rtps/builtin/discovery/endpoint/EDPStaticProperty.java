/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 German Research Center for Artificial Intelligence (DFKI)
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
package org.fiware.kiara.ps.rtps.builtin.discovery.endpoint;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.fiware.kiara.ps.rtps.messages.elements.EntityId;
import org.fiware.kiara.util.Pair;

/**
 * Class EDPStaticProperty, used to read and write the strings from the
 * properties used to transmit the EntityId_t.
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class EDPStaticProperty {

    /**
     * Endpoint type
     */
    public String endpointType;
    /**
     * Status
     */
    public String status;
    /**
     * User ID as string
     */
    public String userIdStr;
    /**
     * User ID
     */
    public short userId;
    /**
     * Entity ID
     */
    public final EntityId entityId;

    /**
     * Main Constructor
     */
    public EDPStaticProperty() {
        endpointType = "";
        status = "";
        userIdStr = "";
        userId = 0;
        entityId = new EntityId();
    }

    /**
     * Convert information to a property
     *
     * @param type Type of endpoint
     * @param status Status of the endpoint
     * @param id User Id
     * @param end EntityId
     * @return Pair of two strings.
     */
    static Pair<String, String> toProperty(String type, String status, short id, EntityId ent) {
        return new Pair<>(
                "eProsimaEDPStatic_" + type + "_" + status + "_ID_" + id,
                (int) ent.getValue(0) + "." + (int) ent.getValue(1) + "." + (int) ent.getValue(2) + "." + (int) ent.getValue(3)
        );
    }

    /**
     * @param prop Input property-
     * @return True if correctly read
     */
    public boolean fromProperty(Pair<String, String> prop) {
        if (prop.getFirst().startsWith("eProsimaEDPStatic")
                && prop.getFirst().length() > 34
                && prop.getFirst().substring(31, 31 + 2).equals("ID")) {
            endpointType = prop.getFirst().substring(18, 18 + 6);
            status = prop.getFirst().substring(25, 25 + 5);
            userIdStr = prop.getFirst().substring(34, Math.min(34 + 100, prop.getFirst().length()));
            try {
                userId = Short.parseShort(userIdStr);
            } catch (NumberFormatException ex) {
                return false;
            }
            Scanner s = new Scanner(prop.getSecond()).useDelimiter("\\s*[.]\\s*");
            try {
                int a = s.nextInt();
                int b = s.nextInt();
                int c = s.nextInt();
                int d = s.nextInt();
                entityId.setValue(0, (byte) a);
                entityId.setValue(1, (byte) b);
                entityId.setValue(2, (byte) c);
                entityId.setValue(3, (byte) d);
            } catch (InputMismatchException e) {
                return false;
            } catch (NoSuchElementException e) {
                return false;
            }
            return true;
        }
        return false;
    }

}
