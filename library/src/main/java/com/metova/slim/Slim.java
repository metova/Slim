package com.metova.slim;

import com.metova.slim.common.provider.LayoutProvider;

import android.app.Activity;

public class Slim {

    public static void bindLayout(Activity target) {
        Class<?> targetClass = target.getClass();
        Class<?> binderClass = findBinderForClass(targetClass);

        try {
            binderClass = Class.forName(binderClass.getCanonicalName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        LayoutProvider provider;
        try {
            provider = (LayoutProvider) binderClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to create binder for " + binderClass, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to access binder for " + binderClass, e);
        }
        target.setContentView(provider.getLayoutId());
    }

    private static Class<?> findBinderForClass(Class<?> cls) {
        try {
            return Class.forName(cls.getCanonicalName() + "$$SlimBinder");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find binder for " + cls, e);
        }
    }
}
