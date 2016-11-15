package com.metova.slim.internal;

import com.metova.slim.provider.ExtraProvider;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentBinderHelper implements ExtraProvider {

    public static FragmentBinderHelper create() {
        return new FragmentBinderHelper();
    }

    private FragmentBinderHelper() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getExtra(Object source, String key) {
        final Fragment fragment = (Fragment) source;
        return (T) (fragment).getArguments().get(key);
    }

    public View createLayout(LayoutInflater inflater, @Nullable ViewGroup container, int layoutId) {
        return inflater.inflate(layoutId, container, false);
    }
}
