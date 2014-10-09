package com.metova.slim;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.metova.slim.annotation.Callback;
import com.metova.slim.annotation.Extra;
import com.metova.slim.annotation.Layout;
import com.metova.slim.internal.BundleChecker;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class Slim {

    public static void inject(Fragment fragment) {
        injectCallbacks(fragment);
        injectExtras(fragment.getArguments(), fragment);
    }

    public static void injectExtras(Bundle extras, Object obj) {

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Extra.class)) {
                Extra annotation = field.getAnnotation(Extra.class);
                String key = annotation.value();
                Object value = BundleChecker.getExtra(key, extras);

                if (value == null) {
                    if (annotation.optional()) {
                        continue;
                    } else {
                        throw new IllegalStateException("Extra '" + key + "' was not found and it is not optional.");
                    }
                }

                if (value instanceof Object[] && !(field.getType().equals(Object[].class))) {
                    // Try to cast it to the array content type
                    Object[] valueArray = (Object[]) value;
                    if (valueArray.length > 0) {
                        Object newValue = Array
                                .newInstance(valueArray[0].getClass(), valueArray.length);
                        for (int i = 0; i < valueArray.length; i++) {
                            Array.set(newValue, i, valueArray[i]);
                        }

                        value = newValue;
                    }
                }

                try {
                    field.setAccessible(true);
                    field.set(obj, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(
                            "invalid value " + value + " for field " + field.getName(), e);
                }
            }
        }
    }

    public static void injectCallbacks(Fragment fragment) {
        injectCallbacks(fragment, fragment.getActivity());
    }

    public static void injectCallbacks(Object child, Object parent) {
        Field[] fields = ((Object) child).getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Callback.class)) {
                try {
                    field.setAccessible(true);
                    field.set(child, parent);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException e) {
                    throw new ClassCastException(
                            parent.getClass().getSimpleName() + " must implement " + field
                                    .getType().getSimpleName()
                    );
                }
            }
        }
    }

    public static View createLayout(Context context, Object obj) {
        return createLayout(context, obj, null);
    }

    public static View createLayout(Context context, Object obj, ViewGroup parent) {
        Layout layout = obj.getClass().getAnnotation(Layout.class);
        if (layout == null) {
            return null;
        }

        return LayoutInflater.from(context).inflate(layout.value(), parent, false);
    }
}
