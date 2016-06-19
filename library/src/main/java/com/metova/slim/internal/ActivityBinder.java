package com.metova.slim.internal;

import com.metova.slim.binder.LayoutIdProvider;

import android.app.Activity;

public class ActivityBinder {

    public static void bindLayout(Activity activity, Object binder) {
        LayoutIdProvider provider = (LayoutIdProvider) binder;
        activity.setContentView(provider.getLayoutId());
    }
}
