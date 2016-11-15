package com.metova.slim;

import com.metova.slim.binder.ActivityBinder;
import com.metova.slim.binder.FragmentBinder;
import com.metova.slim.binder.SlimBinder;
import com.metova.slim.internal.ActivityBinderHelper;
import com.metova.slim.internal.FragmentBinderHelper;
import com.metova.slim.provider.ExtraProvider;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

public class Slim {

    private static Map<Class<?>, SlimBinder> sBinderMap = new HashMap<>();

    private static ActivityBinderHelper sActivityBinderHelper = ActivityBinderHelper.create();
    private static FragmentBinderHelper sFragmentBinderHelper = FragmentBinderHelper.create();

    public static void bindLayout(@NonNull Activity target) {
        ActivityBinder binder = (ActivityBinder) getBinder(target);
        if (binder != null) {
            binder.bindLayout(target, sActivityBinderHelper);
        }
    }

    public static View createLayout(@NonNull Fragment target, LayoutInflater inflater, ViewGroup container) {
        FragmentBinder binder = (FragmentBinder) getBinder(target);
        if (binder != null) {
            return sFragmentBinderHelper.createLayout(inflater, container, binder.getLayoutId());
        }

        return null;
    }

    public static void bindExtras(@NonNull Activity target) {
        ActivityBinder binder = (ActivityBinder) getBinder(target);
        bindExtras(target, binder, sActivityBinderHelper);
    }

    public static void bindExtras(@NonNull Fragment target) {
        FragmentBinder binder = (FragmentBinder) getBinder(target);
        bindExtras(target, binder, sFragmentBinderHelper);
    }

    private static void bindExtras(@NonNull Object target, @Nullable SlimBinder binder, @NonNull ExtraProvider provider) {
        if (binder != null) {
            binder.bindExtras(target, provider);
        }
    }

    @Nullable
    private static Class<?> findBinderForClass(@NonNull Class<?> cls) {
        try {
            return Class.forName(cls.getCanonicalName() + "$$SlimBinder");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Nullable
    private static SlimBinder getBinder(@NonNull Object target) {
        final Class<?> targetClass = target.getClass();
        if (sBinderMap.containsKey(targetClass)) {
            return sBinderMap.get(targetClass);
        }

        Class<?> binderClass = findBinderForClass(target.getClass());
        if (binderClass == null) {
            sBinderMap.put(targetClass, null);
            return null;
        }

        try {
            SlimBinder binder = (SlimBinder) binderClass.newInstance();
            sBinderMap.put(targetClass, binder);
            return binder;
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to instantiate binder " + binderClass.getCanonicalName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to access binder " + binderClass.getCanonicalName(), e);
        }
    }
}
