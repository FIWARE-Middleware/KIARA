package com.kiara.client;

import com.kiara.serialization.impl.SerializerImpl;
import com.kiara.transport.impl.TransportMessage;

public interface AsyncCallback<T> {

	void process(TransportMessage message, SerializerImpl ser);

	public void onSuccess(T result);

	public void onFailure(java.lang.Throwable caught);

}
