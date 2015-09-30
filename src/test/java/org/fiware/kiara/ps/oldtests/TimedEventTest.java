/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
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
package org.fiware.kiara.ps.oldtests;

import java.util.concurrent.TimeUnit;

import org.fiware.kiara.ps.rtps.resources.TimedEvent;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class TimedEventTest {

    static class MyTimedEvent extends TimedEvent {

        public MyTimedEvent(long duration) {
            super(duration);
        }

        @Override
        public void event(EventCode code, String msg) {
            System.out.printf("Interval: %s Remaining %s Event Code: %s msg: %s%n", getIntervalMilliSec(), getRemainingTimeMilliSec(), code, msg);
        }

    }

    public static void main(String[] args) throws InterruptedException {
        Object obj = new Object();

        MyTimedEvent t = new MyTimedEvent(10);

        synchronized (obj) {
            TimeUnit.MILLISECONDS.timedWait(obj, 4);
        }

        System.out.println(t.getRemainingTimeMilliSec());

        synchronized (obj) {
            TimeUnit.MILLISECONDS.timedWait(obj, 50);
        }

        t.updateIntervalMillisec(30);

        t.restartTimer();

        synchronized (obj) {
            TimeUnit.MILLISECONDS.timedWait(obj, 50);
        }

        t.stopTimer();

        System.err.println("CANCELLED");
    }
}
