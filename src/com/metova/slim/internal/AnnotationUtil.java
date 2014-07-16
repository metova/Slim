package com.metova.slim.internal;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.metova.slim.annotation.Callback;
import com.metova.slim.annotation.Extra;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class AnnotationUtil {
    private AnnotationUtil() {

    }

    public static void injectExtras(Bundle extras, Object obj) {

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Extra.class)) {
                Extra annotation = field.getAnnotation(Extra.class);
                String key = annotation.key();
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

    public static void injectCallbacks(Fragment fragment, Activity activity) {
        Field[] fields = ((Object) fragment).getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Callback.class)) {
                try {
                    field.setAccessible(true);
                    field.set(fragment, activity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException e) {
                    throw new ClassCastException(
                            activity.getClass().getSimpleName() + " must implement " + field
                                    .getType().getSimpleName()
                    );
                }
            }
        }
    }
}
