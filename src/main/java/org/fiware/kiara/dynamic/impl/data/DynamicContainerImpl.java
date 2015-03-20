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
package org.fiware.kiara.dynamic.impl.data;

import java.util.ArrayList;

import org.fiware.kiara.dynamic.data.DynamicData;
import org.fiware.kiara.typecode.data.DataTypeDescriptor;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*
*/
public class DynamicContainerImpl extends DynamicDataImpl {
    
    protected DataTypeDescriptor m_contentType;
    protected ArrayList<DynamicData> m_members;
    
    public DynamicContainerImpl(DataTypeDescriptor dataDescriptor, String className) {
        super(dataDescriptor, className);
    }
    
    public DataTypeDescriptor getContentType() {
        return this.m_contentType;
    }
    
    public void setContentType(DataTypeDescriptor contentType) {
        this.m_contentType = contentType;
    }
    
    

}
