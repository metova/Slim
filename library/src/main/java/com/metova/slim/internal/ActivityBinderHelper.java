package com.metova.slim.internal;

import com.metova.slim.binder.LayoutBinder;
import com.metova.slim.provider.ExtraProvider;

import android.app.Activity;

public class ActivityBinderHelper implements LayoutBinder, ExtraProvider {

    public static ActivityBinderHelper create() {
        return new ActivityBinderHelper();
    }

    private ActivityBinderHelper() {
    }

    @Override
    public void bindLayout(Object target, int layoutId) {
        if (layoutId > 0) {
            ((Activity) target).setContentView(layoutId);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getExtra(Object source, String key) {
        Activity activity = (Activity) source;
        return (T) activity.getIntent().getExtras().get(key);
    }
}
