package com.metova.slim;

import com.metova.slim.annotation.Callback;

import junit.framework.TestCase;

public class SlimTest extends TestCase {

    public void testInject() throws Exception {
    }

    public void testInjectExtras() throws Exception {
    }

    public void testInjectCallbacksSuccess() throws Exception {
        AnnotationTestChild fragment = new AnnotationTestChild();
        AnnotationTestGoodParent activity = new AnnotationTestGoodParent();

        try {
            Slim.injectCallbacks(fragment, activity);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertNotNull(fragment.callback);
    }

    public void testInjectCallbacksFailure() throws Exception {
        AnnotationTestChild fragment = new AnnotationTestChild();
        AnnotationTestBadParent activity = new AnnotationTestBadParent();

        try {
            Slim.injectCallbacks(fragment, activity);
        } catch (Exception e) {
            // pass
            return;
        }

        fail();
    }

    public static class AnnotationTestGoodParent
            implements AnnotationTestChild.AnnotationTestCallback {

    }

    public static class AnnotationTestBadParent {

    }

    public static class AnnotationTestChild {

        @Callback
        AnnotationTestCallback callback;

        public interface AnnotationTestCallback {

        }
    }
}