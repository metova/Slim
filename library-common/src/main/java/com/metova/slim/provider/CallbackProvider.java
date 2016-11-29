package com.metova.slim.provider;


public interface CallbackProvider {

    <T> T getCallback(Object source, Class<T> clz);
}
