package com.metova.slim;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import com.metova.slim.internal.AnnotationUtil;

public abstract class SlimFragment extends Fragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        AnnotationUtil.injectCallbacks(this, activity);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnnotationUtil.injectExtras(getArguments(), this);
    }
}
