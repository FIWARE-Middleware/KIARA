package com.kiara.client;

public interface AsyncCallback<T> {
    void onFailure(Throwable caught);
    void onSuccess(T result);
}
