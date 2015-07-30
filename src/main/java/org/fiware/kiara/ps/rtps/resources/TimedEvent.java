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
package org.fiware.kiara.ps.rtps.resources;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public abstract class TimedEvent {

    private static final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    private final EventTask task;
    private ScheduledFuture<?> event;
    private long intervalMicrosec;

    /**
     * Enum representing event statuses
     */
    public enum EventCode {

        EVENT_SUCCESS,
        EVENT_ABORT,
        EVENT_MSG
    }

    private class EventTask implements Runnable {

        @Override
        public void run() {
            event(EventCode.EVENT_SUCCESS, null);
        }

    }

    /**
     * @param milliseconds Interval of the timedEvent.
     */
    public TimedEvent(double milliseconds) {
        this.task = new EventTask();
        this.intervalMicrosec = TimeUnit.MICROSECONDS.convert((long) milliseconds, TimeUnit.MILLISECONDS);
        event = service.scheduleAtFixedRate(task, 0, intervalMicrosec, TimeUnit.MICROSECONDS);
    }

    /**
     * Method invoked when the event occurs. Abstract method.
     *
     * @param code Code representing the status of the event
     * @param msg Message associated to the event
     */
    public abstract void event(EventCode code, String msg);

    //!Method to restart the timer.
    public void restartTimer() {
        event.cancel(true);
        event = service.scheduleAtFixedRate(task, 0, intervalMicrosec, TimeUnit.MICROSECONDS);
    }

    //!Method to stop the timer.
    public void stopTimer() {
        event.cancel(false);
        service.shutdown();
    }

    /**
     * Update event interval. When updating the interval, the timer is not
     * restarted and the new interval will only be used the next time you call
     * restart_timer().
     *
     * @param inter New interval for the timedEvent
     * @param timeUnit time unit of the interval
     * @return true on success
     */
    public boolean updateInterval(long inter, TimeUnit timeUnit) {
        intervalMicrosec = TimeUnit.MICROSECONDS.convert(inter, timeUnit);
        return true;
    }

    /**
     * Update event interval. When updating the interval, the timer is not
     * restarted and the new interval will only be used the next time you call
     * restart_timer().
     *
     * @param timeMillisec New interval for the timedEvent
     * @return true on success
     */
    public boolean updateIntervalMillisec(double timeMillisec) {
        return updateInterval((long) timeMillisec, TimeUnit.MILLISECONDS);
    }

    /**
     * Get the milliseconds interval
     *
     * @return Mulliseconds interval
     */
    public double getIntervalMilliSec() {
        return TimeUnit.MILLISECONDS.convert(intervalMicrosec, TimeUnit.MICROSECONDS);
    }

    //!
    public void stopSemaphorePost() {

    }

    /**
     * Check if the instance is waiting
     *
     * @return true if the instace is waiting
     */
    public boolean isWaiting() {
        return false;
    }

    /**
     * Get the remaining milliseconds for the timer to expire
     *
     * @return Remaining milliseconds for the timer to expire
     */
    public double getRemainingTimeMilliSec() {
        return event.getDelay(TimeUnit.MILLISECONDS);
    }

}
