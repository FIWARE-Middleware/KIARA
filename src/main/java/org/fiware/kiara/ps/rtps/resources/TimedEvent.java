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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.Kiara;
import org.fiware.kiara.RunningService;
import org.fiware.kiara.ps.rtps.messages.elements.Timestamp;

/**
 * This class represent an event that may occur in a certain
 * number of microseconds.
 * 
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public abstract class TimedEvent {

    /**
     * Executor service
     */
    private static final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    /**
     * Event task to be executed
     */
    private final EventTask task;

    /**
     * Scheduled furute object to execute the task
     */
    private ScheduledFuture<?> event;

    /**
     * Time interval to execute the event
     */
    private long intervalMicrosec;

    /**
     * Mutex
     */
    protected final Lock m_mutex = new ReentrantLock(true);

    static {
        Kiara.addRunningService(new RunningService() {
            public void shutdownService() {
                service.shutdown();
            }
        });
    }

    /**
     * Enum representing event statuses
     */
    public enum EventCode {

        /**
         * Event is a Success
         */
        EVENT_SUCCESS,
        /**
         * Event is Aborted
         */
        EVENT_ABORT,
        /**
         * Event carries a message
         */
        EVENT_MSG
    }

    /**
     * Event to be executed
     * 
     * @author @author Rafael Lara {@literal <rafaellara@eprosima.com>}
     *
     */
    private class EventTask implements Runnable {

        /**
         * Main method
         */
        @Override
        public void run() {
            event(EventCode.EVENT_SUCCESS, null);
        }

    }

    /**
     * @param milliseconds Interval of the timedEvent.
     */
    public TimedEvent(double milliseconds) {
        //this.m_mutex = new ReentrantLock(true);
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

    /**
     * Method to restart the timer.
     */
    public void restartTimer() {
        event.cancel(true);
        event = service.scheduleAtFixedRate(task, 0, intervalMicrosec, TimeUnit.MICROSECONDS);
    }

    /**
     * Method to stop the timer.
     */
    public void stopTimer() {
        event.cancel(false);
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
     * Changes the microseconds interval
     * 
     * @param inter New interval
     * @return true on success; false otherwise
     */
    public boolean updateInterval(Timestamp inter) {
        return updateIntervalMillisec(inter.toMilliSecondsDouble());
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

    /**
     * Stops the post of the semaphore
     */
    public void stopSemaphorePost() {
        // Do nothing
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
