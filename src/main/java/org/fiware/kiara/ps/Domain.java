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
package org.fiware.kiara.ps;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.fiware.kiara.ps.attributes.ParticipantAttributes;
import org.fiware.kiara.ps.attributes.PublisherAttributes;
import org.fiware.kiara.ps.attributes.SubscriberAttributes;
import org.fiware.kiara.ps.participant.Participant;
import org.fiware.kiara.ps.participant.ParticipantListener;
import org.fiware.kiara.ps.publisher.Publisher;
import org.fiware.kiara.ps.publisher.PublisherListener;
import org.fiware.kiara.ps.rtps.RTPSDomain;
import org.fiware.kiara.ps.rtps.participant.RTPSParticipant;
import org.fiware.kiara.ps.subscriber.Subscriber;
import org.fiware.kiara.ps.subscriber.SubscriberListener;
import org.fiware.kiara.ps.topic.TopicDataType;
import org.fiware.kiara.serialization.impl.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class Domain, use to interact with the Publisher Subscriber API of the Fast
 * RTPS implementation.
 *
 * @author Rafael Lara {@literal <rafaellara@eprosima.com>}
 */
public class Domain {

    /**
     * List of {@link Participant} objects
     */
    private static final List<Participant> m_participants = new ArrayList<>();

    /**
     * Logging object
     */
    private static final Logger logger = LoggerFactory.getLogger(Domain.class);

    /**
     * Mutex
     */
    private static final Lock m_mutex = new ReentrantLock(true);

    /**
     * Stop and remove all participants and publishers and subscribers in this
     * Domain.
     */
    public static void stopAll() {
        m_mutex.lock();
        try {
            System.out.println("----LOCK: " + Thread.currentThread().getId() + "  stopAll");
            while (m_participants.size() > 0) {
                Domain.removeParticipant(m_participants.get(0));
            }
        } finally {
            System.out.println("----UNLOCK: " + Thread.currentThread().getId() + "  stopAll");
            m_mutex.unlock();
        }
    }

    /**
     * Remove a Participant and all associated publishers and subscribers.
     *
     * @param part reference to the participant.
     * @return true if correctly removed.
     */
    public static boolean removeParticipant(Participant part) {
        m_mutex.lock();
        try {
            System.out.println("----LOCK: " + Thread.currentThread().getId() + "  removeParticipant");
            if (part != null) {
                for (Participant current : m_participants) {
                    if (current.getGuid().equals(part.getGuid())) {
                        // Found
                        current.destroy();
                        m_participants.remove(current);
                        logger.info("Participant successfully removed"); // TODO Not in C++ implementation. Log every removal.
                        return true;
                    }
                }
            }
        } finally {
            System.out.println("----UNLOCK: " + Thread.currentThread().getId() + "  removeParticipant");
            m_mutex.unlock();
        }
        return false;
    }

    /**
     * Remove a Publisher.
     *
     * @param pub reference to the {@link Publisher}.
     * @return true if correctly removed.
     */
    public static boolean removePublisher(Publisher<?> pub) {
        System.out.println("Pub Getting lock: " + Thread.currentThread().getId());
        boolean result = false;
        m_mutex.lock();
        try {
            System.out.println("----LOCK: " + Thread.currentThread().getId() + "  removePublisher");
            System.out.println("Starting to remove Pub: " + Thread.currentThread().getId());
            if (pub != null) {
                for (Participant it : m_participants) {
                    if (it.getGuid().getGUIDPrefix().equals(pub.getGuid().getGUIDPrefix())) {
                        // Found
                        System.out.println("Removing Pub: " + Thread.currentThread().getId());
                        result = it.removePublisher(pub);
                        return result;
                    }
                }
            }
        } finally {
            System.out.println("----UNLOCK: " + Thread.currentThread().getId() + "  removePublisher");
            System.out.println("Pub Unlocking: " + Thread.currentThread().getId());
            m_mutex.unlock();
        }
        return result;
    }

    /**
     * Remove a Subscriber.
     *
     * @param sub reference to the {@link Subscriber}.
     * @return true if correctly removed.
     */
    public static boolean removeSubscriber(Subscriber<?> sub) {
        System.out.println("Sub Getting lock: " + Thread.currentThread().getId());
        boolean result = false;
        System.out.println("Locked: " + ((ReentrantLock) m_mutex).isLocked() + " - " + ((ReentrantLock) m_mutex).hasQueuedThreads());
        System.out.println("By me?: " + ((ReentrantLock) m_mutex).isHeldByCurrentThread());
        m_mutex.lock();
        try {
            System.out.println("----LOCK: " + Thread.currentThread().getId() + " removeSubscriber");
            System.out.println("Starting to remove Sub: " + Thread.currentThread().getId());
            if (sub != null) {
                for (Participant it : m_participants) {
                    if (it.getGuid().getGUIDPrefix().equals(sub.getGuid().getGUIDPrefix())) {
                        // Found
                        System.out.println("Removing Sub: " + Thread.currentThread().getId());
                        result = it.removeSubscriber(sub);
                        return result;
                    }
                }
            }
        } finally {
            System.out.println("----UNLOCK: " + Thread.currentThread().getId() + "  removeSubscriber");
            System.out.println("Sub Unlocking: " + Thread.currentThread().getId());
            m_mutex.unlock();
        }
        return result;
    }

    /**
     * Create a Participant.
     *
     * @param att Participant attributes.
     * @param listener reference to {@link ParticipantListener}.
     * @return reference to {@link Participant}. (null if not created.)
     */
    public static Participant createParticipant(ParticipantAttributes att, ParticipantListener listener) {
        m_mutex.lock();
        try {
            System.out.println("----LOCK: " + Thread.currentThread().getId() + "  createParticipant");
            Participant pubSubParticipant = new Participant(att, listener);

            RTPSParticipant part = RTPSDomain.createParticipant(att.rtps, pubSubParticipant.getListener());

            if (part == null) {
                logger.error("Problem creating RTPSParticipant");
                return null;
            }

            pubSubParticipant.setRTPSParticipant(part);
            m_participants.add(pubSubParticipant);
            return pubSubParticipant;
        } finally {
            System.out.println("----UNLOCK: " + Thread.currentThread().getId() + "  createParticipant");
            m_mutex.unlock();
        }
    }

    /**
     * Create a Publisher in a Participant.
     *
     * @param <T> A generic user data type
     * @param part reference to the participant where you want to create the
     * {@link Publisher}.
     * @param att PublisherAttributes.
     * @param listener reference to the {@link PublisherListener}.
     * @return reference to the created {@link Publisher} (null if not created).
     */
    public static <T extends Serializable> Publisher<T> createPublisher(Participant part, PublisherAttributes att, PublisherListener listener) {
        m_mutex.lock();
        try {
            System.out.println("----LOCK: " + Thread.currentThread().getId() + "  createPublisher");
            for (Participant it : m_participants) {
                if (it.getGuid().equals(part.getGuid())) {
                    return part.createPublisher(att, listener);
                }
            }
        } finally {
            System.out.println("----UNLOCK: " + Thread.currentThread().getId() + "  createPublisher");
            m_mutex.unlock();
        }
        return null;
    }

    /**
     * Create a Subscriber in a Participant.
     *
     * @param <T> A generic user data type
     * @param part reference to the participant where you want to create the
     * {@link Publisher}.
     * @param att subscriber attributes.
     * @param listener reference to the {@link SubscriberListener}.
     * @return reference to the created {@link Subscriber} (null if not created).
     */
    public static <T extends Serializable> Subscriber<T> createSubscriber(Participant part, SubscriberAttributes att, SubscriberListener listener) {
        m_mutex.lock();
        try {
            System.out.println("----LOCK: " + Thread.currentThread().getId() + "  createSubscriber");
            for (Participant it : m_participants) {
                if (it.getGuid().equals(part.getGuid())) {
                    return part.createSubscriber(att, listener);
                }
            }
        } finally {
            System.out.println("----UNLOCK: " + Thread.currentThread().getId() + "  createSubscriber");
            m_mutex.unlock();
        }
        return null;
    }

    /**
     * Register a type in a participant.
     *
     * @param part reference to the {@link Participant}.
     * @param type reference to the {@link TopicDataType}.
     * @return true if correctly registered.
     */
    public static boolean registerType(Participant part, TopicDataType<?> type) {
        m_mutex.lock();
        try {
            System.out.println("----LOCK: " + Thread.currentThread().getId() + "  registerType");
            for (Participant it : m_participants) {
                if (it.getGuid().equals(part.getGuid())) {
                    return part.registerType(type);
                }
            }
        } finally {
            System.out.println("----UNLOCK: " + Thread.currentThread().getId() + "  registerType");
            m_mutex.unlock();
        }
        return false;
    }

}
