package com.metova.slim.internal;

import android.os.Bundle;

public class BundleChecker {

    private BundleChecker() {
    }

    /**
     * Convenience method for retrieving an object (no primitives) from a {@link
     * android.os.Bundle}.
     * If more than one of the Bundles passed contains an entry with the given key, the first object
     * found is returned.
     * <p>An {@link IllegalArgumentException} is thrown if there is no entry with the
     * given key.
     *
     * <p><b>Sample use in a Fragment:</b>
     * <p>
     * <pre>
     * public void onActivityCreated( Bundle savedInstanceState ) {
     *
     * super.onActivityCreated( savedInstanceState );
     * News news = BundleChecker.getExtraOrThrow( "key", new Bundle[]{ getArguments(),
     * savedInstanceState } );
     * }
     * </pre>
     *
     * @param key     - The key with which to search for in the given <b>bundles</b>
     * @param bundles - an array of {@link android.os.Bundle} to look for an entry with the given
     *                <b>key</b>
     * @return The entry with the given key
     * @throws IllegalArgumentException if there is no entry with the given key
     */
    public static <T> T getExtraOrThrow(final String key, final Bundle... bundles) {

        T object = getExtra(key, bundles);

        if (object == null) {

            throw new IllegalArgumentException("Key '" + key + "' not contained in bundles.");
        }

        return object;
    }

    /**
     * Convenience method for retrieving an object (no primitives) from a {@link
     * android.os.Bundle}.
     * If more than one of the Bundles passed contains an entry with the given key, the first object
     * found is returned.
     *
     * <p><b>Sample use in a Fragment:</b>
     * <p>
     * <pre>
     * public void onActivityCreated( Bundle savedInstanceState ) {
     *
     * super.onActivityCreated( savedInstanceState );
     * String key = BundleChecker.getExtra( "key", new Bundle[]{ getArguments(), savedInstanceState
     * } );
     * if(key != null) {
     *
     * //...
     * }
     * }
     * </pre>
     *
     * @param key     - The key with which to search for in the given <b>bundles</b>
     * @param bundles - an array of {@link android.os.Bundle} to look for an entry with the given
     *                <b>key</b>
     * @return The entry with the given key, or {@code null} if there is no entry with the given key
     */
    public static <T> T getExtra(final String key, final Bundle... bundles) {

        return getExtra(key, null, bundles);
    }

    /**
     * Convenience method for retrieving an object (no primitives) from a
     * {@link android.os.Bundle}. If more than one of the Bundles passed
     * contains an entry with the given key, the first object found is returned.
     * <p>
     * <b>Sample use in a Fragment:</b>
     * <p>
     *
     * <pre>
     * public void onActivityCreated(Bundle savedInstanceState){
     *
     * 	super.onActivityCreated(savedInstanceState);
     * 	long id = BundleChecker.getExtra(&quot;id&quot;, -1L, getArguments(), savedInstanceState);
     * }
     * </pre>
     *
     * @param key          - The key with which to search for in the given <b>bundles</b>
     * @param defaultValue - The value returned if the <b>key</b> cannot be found in a
     *                     bundle
     * @param bundles      - an array of {@link android.os.Bundle} to look for an entry
     *                     with the given <b>key</b>
     * @return The entry with the given key, or {@code null} if there is no
     * entry with the given key
     */
    public static <T> T getExtra(final String key, T defaultValue, final Bundle... bundles) {

        Bundle bundle = null;

        for (Bundle b : bundles) {

            if (b != null && b.containsKey(key)) {

                bundle = b;
                break;
            }
        }

        @SuppressWarnings("unchecked")
        T object = (bundle == null) ? defaultValue : (T) bundle.get(key);

        return object;
    }
}
