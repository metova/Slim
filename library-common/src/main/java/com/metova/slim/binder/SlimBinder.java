package com.metova.slim.binder;

import com.metova.slim.provider.ExtraProvider;

public interface SlimBinder {

    void bindExtras(Object target, ExtraProvider provider);
}
