package com.metova.slim;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.metova.slim.annotation.Callback;
import org.junit.Test;

import static org.junit.Assert.*;

public class SlimTest {

    @Test
    public void testInject() throws Exception {

    }

    @Test
    public void testInjectExtras() throws Exception {

    }

    @Test
    public void testInjectCallbacksSuccess() throws Exception {
        AnnotationTestFragment fragment = new AnnotationTestFragment();
        AnnotationTestGoodActivity activity = new AnnotationTestGoodActivity();

        try {
            Slim.injectCallbacks(fragment, activity);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertNotNull(fragment.callback);
    }

    @Test
    public void testInjectCallbacksFailure() throws Exception {
        AnnotationTestFragment fragment = new AnnotationTestFragment();
        AnnotationTestBadActivity activity = new AnnotationTestBadActivity();

        try {
            Slim.injectCallbacks(fragment, activity);
        } catch (Exception e) {
            // pass
            return;
        }

        fail();
    }

    public static class AnnotationTestGoodActivity extends FragmentActivity
            implements AnnotationTestFragment.AnnotationTestCallback {

    }

    public static class AnnotationTestBadActivity extends FragmentActivity {

    }

    public static class AnnotationTestFragment extends Fragment {

        @Callback
        AnnotationTestCallback callback;

        public interface AnnotationTestCallback {

        }
    }
}