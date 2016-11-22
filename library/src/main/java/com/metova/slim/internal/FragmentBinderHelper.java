package com.metova.slim.internal;

import com.metova.slim.provider.CallbackProvider;
import com.metova.slim.provider.ExtraProvider;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentBinderHelper implements ExtraProvider, CallbackProvider {

    public static FragmentBinderHelper create() {
        return new FragmentBinderHelper();
    }

    private FragmentBinderHelper() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getExtra(Object source, String key, boolean optional) {
        final Fragment fragment = (Fragment) source;
        T value = (T) (fragment).getArguments().get(key);
        if (value == null && !optional) {
            throw new NullPointerException(String.format("Extra with key '%s' is null and is required.", key));
        }

        return value;
    }

    @Override
    public <T> T getCallback(Object source, Class<T> clz) {
        final Fragment fragment = (Fragment) source;
        if (fragment.getActivity() == null) {
            throw new IllegalStateException(String.format("%s is not attached to an Activity", fragment.getClass().getSimpleName()));
        }

        try {
            return clz.cast(fragment.getActivity());
        } catch (ClassCastException e) {
            throw new RuntimeException(
                    String.format("Unable to cast %1$s to %2$s. Does %1$s implement the correct interface?", fragment.getActivity().getClass().getSimpleName(), clz.getSimpleName()));
        }
    }

    public View createLayout(LayoutInflater inflater, @Nullable ViewGroup container, int layoutId) {
        return inflater.inflate(layoutId, container, false);
    }
}
