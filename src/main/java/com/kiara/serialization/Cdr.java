package com.kiara.serialization;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

public class Cdr implements Serializer {

    private final AtomicInteger nextId;

    public Cdr() {
        nextId = new AtomicInteger(1);
    }

    @Override
    public Object getNewMessageId() {
        return nextId.getAndIncrement();
    }

    @Override
    public void serializeMessageId(ByteBuffer buffer, Object messageId) {
        serializeInteger(buffer, (Integer)messageId);
    }

    @Override
    public Object deserializeMessageId(ByteBuffer buffer) {
        final int id = deserializeInteger(buffer);
        return id;
    }

    @Override
    public void serializeService(ByteBuffer buffer, String service) {
        this.serializeString(buffer, service);
    }

    @Override
    public String deserializeService(ByteBuffer buffer) {
        return this.deserializeString(buffer);
    }

    @Override
    public void serializeOperation(ByteBuffer buffer, String operation) {
        this.serializeString(buffer, operation);
    }

    @Override
    public String deserializeOperation(ByteBuffer buffer) {
        return this.deserializeString(buffer);
    }

    @Override
    public void serializeString(ByteBuffer buffer, String data) {
        byte[] bytes = data.getBytes();
        this.serializeInteger(buffer, bytes.length);
        buffer.put(bytes);
    }

    @Override
    public String deserializeString(ByteBuffer buffer) {
        int length = this.deserializeInteger(buffer);
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes);
    }

    @Override
    public void serializeInteger(ByteBuffer buffer, int data) {
        buffer.putInt(data);
    }

    @Override
    public int deserializeInteger(ByteBuffer buffer) {
        return buffer.getInt();
    }

    @Override
    public boolean equalMessageIds(Object id1, Object id2) {
        if (id1 == id2) {
            return true;
        }
        if (id1 == null || id2 == null) {
            return false;
        }
        return id1.equals(id2);
    }
}
