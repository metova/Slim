package com.metova.slim.provider;

public interface ExtraProvider {

    <T> T getExtra(Object source, String key);
}
