package com.metova.slim;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SlimListFragment extends ListFragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Slim.injectCallbacks(this, activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Slim.injectExtras(getArguments(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = Slim.createLayout(getActivity(), this, container);
        return layout != null ? layout : super.onCreateView(inflater, container, savedInstanceState);
    }
}
