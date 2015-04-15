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
 *
 *
 * @file TestService.java
 * This file contains the main synchronous interface for the defined operations.
 *
 * This file was generated by using the tool Kiaragen.
 *
 */
 
 
package org.fiware.kiara.complextypes;

/**
 * Interface containing the synchronous method definition. 
 *
 * @author Kiaragen tool.
 *
 */
public interface TestService {

	public MyStruct return_param_func (/*in*/ MyStruct param1, /*in*/ int param2);

	public void only_param_func (/*in*/ MyStruct param1);

	public MyStruct only_return_func ();

	public void oneway_return_param_func (/*in*/ MyStruct param1, /*in*/ int param2);

	public void oneway_only_param_func (/*in*/ MyStruct param1);

	public void oneway_only_return_func ();
	
}
