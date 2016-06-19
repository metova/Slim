package com.metova.slim;

import com.metova.slim.internal.ActivityBinder;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

public class Slim {

    private static Map<Class<?>, Object> sBinderMap = new HashMap<>();

    public static void bindLayout(Activity target) {
        ActivityBinder.bindLayout(target, getBinder(target));
    }

    private static Class<?> findBinderForClass(Class<?> cls) {
        try {
            return Class.forName(cls.getCanonicalName() + "$$SlimBinder");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find binder for " + cls, e);
        }
    }

    private static Object getBinder(Object target) {
        Class<?> targetClass = target.getClass();
        if (sBinderMap.containsKey(targetClass)) {
            return sBinderMap.get(targetClass);
        }

        Class<?> binderClass = findBinderForClass(target.getClass());
        try {
            Object binder = binderClass.newInstance();
            sBinderMap.put(targetClass, binder);
            return binder;
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to instantiate binder " + binderClass.getCanonicalName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to access binder " + binderClass.getCanonicalName(), e);
        }
    }
}
