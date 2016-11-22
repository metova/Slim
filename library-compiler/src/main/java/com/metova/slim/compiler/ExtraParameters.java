package com.metova.slim.compiler;


class ExtraParameters {

    private String key;
    private boolean optional;

    ExtraParameters(String key, boolean optional) {

        this.key = key;
        this.optional = optional;
    }

    String getKey() {
        return key;
    }

    boolean isOptional() {
        return optional;
    }
}
