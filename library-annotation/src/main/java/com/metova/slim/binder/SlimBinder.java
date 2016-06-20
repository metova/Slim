package com.metova.slim.binder;

import com.metova.slim.provider.ExtraProvider;

public interface SlimBinder {

    void bindLayout(Object target, LayoutBinder binder);

    void bindExtras(Object target, ExtraProvider provider);
}
