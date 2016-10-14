package com.metova.slim.internal;

import com.metova.slim.binder.LayoutBinder;
import com.metova.slim.provider.ExtraProvider;

import android.support.v4.app.Fragment;

public class FragmentBinderHelper implements LayoutBinder, ExtraProvider {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getExtra(Object source, String key) {
        final Fragment fragment = (Fragment) source;
        return (T) (fragment).getArguments().get(key);
    }

    @Override
    public void bindLayout(Object target, int layoutId) {
        // TODO
    }
}
