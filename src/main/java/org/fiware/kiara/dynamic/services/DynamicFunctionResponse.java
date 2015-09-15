/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 Proyectos y Sistemas de Mantenimiento S.L. (eProsima)
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
package org.fiware.kiara.dynamic.services;

import org.fiware.kiara.dynamic.DynamicValue;
import org.fiware.kiara.dynamic.data.DynamicData;

/**
 * This class represents a dynamic function response. This class is used to
 * retrieve the information sent from the server after a remote procedure call.
 *
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface DynamicFunctionResponse extends DynamicValue {

    /**
     * This function returns true if the server raised an exception when
     * executing the function.
     *
     * @return true if server raised an exception
     */
    public boolean isException();

    /**
     * This method sets the attribute indicating that an exception has been
     * thrown on the server side.
     *
     * @param isException exception flag
     */
    public void setException(boolean isException);

    /**
     * This function sets a {@link DynamicData} object as a return value for the remote
     * call.
     *
     * @param returnType return value
     */
    public void setReturnValue(DynamicData returnType);

    /**
     * This function returns the {@link DynamicData} representing the result of the
     * remote call.
     *
     * @return return value
     */
    public DynamicData getReturnValue();

}
