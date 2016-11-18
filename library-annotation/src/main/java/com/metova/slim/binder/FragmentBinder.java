package com.metova.slim.binder;

import com.metova.slim.provider.CallbackProvider;

public interface FragmentBinder extends SlimBinder {

    int getLayoutId();

    void bindCallbacks(Object target, CallbackProvider provider);
}
