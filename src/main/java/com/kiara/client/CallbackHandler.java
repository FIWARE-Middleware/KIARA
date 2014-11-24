package com.kiara.client;

public interface CallbackHandler<T> {
    void onFailure(Throwable caught);
    void onSuccess(T result);
}
