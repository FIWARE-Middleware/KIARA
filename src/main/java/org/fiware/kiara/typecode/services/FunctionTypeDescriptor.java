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
package org.fiware.kiara.typecode.services;

import org.fiware.kiara.typecode.TypeDescriptor;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;
import org.fiware.kiara.typecode.data.ExceptionTypeDescriptor;

/**
 * This interface represents a function, providing methods to easily describe it
 * by setting its return type, parameters and exceptions that it might throw.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 *
 */
public interface FunctionTypeDescriptor extends TypeDescriptor {

    /**
     * This function returns the return {@link DataTypeDescriptor} of the
     * function.
     *
     * @return return type descriptor
     * @see DataTypeDescriptor
     */
    public DataTypeDescriptor getReturnType();

    /**
     * This function sets the return {@link DataTypeDescriptor} of the function.
     *
     * @param returnType return type of the function
     */
    public void setReturnType(DataTypeDescriptor returnType);

    /**
     * This function returns a {@link DataTypeDescriptor} representing a
     * parameter whose name is the same as the one indicated.
     *
     * @param name parameter name
     * @return parameter type descriptor
     */
    public DataTypeDescriptor getParameter(String name);

    //public List<DataTypeDescriptor> getParameters();
    /**
     * This function adds a new {@link DataTypeDescriptor} to the parameters
     * list with the name indicated.
     *
     * @param parameter parameter type descriptor
     * @param name parameter name
     */
    public void addParameter(DataTypeDescriptor parameter, String name);

    /**
     * This function returns an ExceptionTypeDescriptor whose name is the same
     * as the one specified as a parameter.
     *
     * @param name exception name
     * @return exception type descriptor
     * @see ExceptionTypeDescriptor
     */
    public ExceptionTypeDescriptor getException(String name);

    /**
     * This function adds a new {@link ExceptionTypeDescriptor} to the
     * exceptions list.
     *
     * @param exception exception type descriptor
     */
    public void addException(ExceptionTypeDescriptor exception);

    /**
     * This function returns the function name.
     *
     * @return function name
     */
    public String getName();

    /**
     * This function returns the name of the {@link ServiceTypeDescriptor} in
     * which the {@link FunctionTypeDescriptor} is defined.
     *
     * @return service name
     */
    public String getServiceName();

    /**
     * This function sets the name of the {@link ServiceTypeDescriptor} in which
     * the {@link FunctionTypeDescriptor} is defined.
     *
     * @param serviceName service name containing function
     * @return function type descriptor
     */
    public FunctionTypeDescriptor setServiceName(String serviceName);

}
