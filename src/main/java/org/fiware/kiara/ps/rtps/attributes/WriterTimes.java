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
package org.fiware.kiara.ps.rtps.attributes;

import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;

/**
*
* @author Rafael Lara {@literal <rafaellara@eprosima.com>}
*/
public class WriterTimes {
	
	public Timestamp heartBeatPeriod;
	public Timestamp nackResponseDelay;
	public Timestamp nackSupressionDuration;

        public WriterTimes(WriterTimes value) {
            this.heartBeatPeriod = new Timestamp(value.heartBeatPeriod);
            this.nackResponseDelay = new Timestamp(value.nackResponseDelay);
            this.nackSupressionDuration = new Timestamp(value.nackSupressionDuration);
        }

	public WriterTimes() {
		this.heartBeatPeriod = new Timestamp(3, 0);
		this.nackResponseDelay = new Timestamp(0, 200*1000*1000);
		this.nackSupressionDuration = new Timestamp().timeZero();
	}

        public void copy(WriterTimes value) {
            this.heartBeatPeriod.copy(value.heartBeatPeriod);
            this.nackResponseDelay.copy(value.nackResponseDelay);
            this.nackSupressionDuration.copy(value.nackSupressionDuration);
        }

}
