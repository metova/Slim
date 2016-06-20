package com.metova.slim;

import com.metova.slim.binder.SlimBinder;
import com.metova.slim.internal.ActivityBinderHelper;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Slim {

    private static Map<Class<?>, SlimBinder> sBinderMap = new HashMap<>();
    private static ActivityBinderHelper sActivityBinderHelper = new ActivityBinderHelper();

    public static void bindLayout(@NonNull Activity target) {
        SlimBinder binder = getBinder(target);
        if (binder != null) {
            binder.bindLayout(target, sActivityBinderHelper);
        }
    }

    public static void bindExtras(@NonNull Activity target) {
        SlimBinder binder = getBinder(target);
        if (binder != null) {
            binder.bindExtras(target, sActivityBinderHelper);
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
        Class<?> targetClass = target.getClass();
        if (sBinderMap.containsKey(targetClass)) {
            return sBinderMap.get(targetClass);
        }

        Class<?> binderClass = findBinderForClass(target.getClass());
        if (binderClass == null) {
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
