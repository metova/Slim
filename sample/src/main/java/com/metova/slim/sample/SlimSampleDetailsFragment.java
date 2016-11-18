package com.metova.slim.sample;

import com.metova.slim.Slim;
import com.metova.slim.annotation.Callback;
import com.metova.slim.annotation.Extra;
import com.metova.slim.annotation.Layout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

@Layout(R.layout.fragment_slim_sample_details)
public class SlimSampleDetailsFragment extends Fragment {

    public static final String EXTRA_MESSAGE = "extra.message";

    @Callback
    ActivityCallback mCallback;

    @Extra(EXTRA_MESSAGE)
    String mMessage;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Slim.bindCallbacks(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return Slim.createLayout(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Slim.bindExtras(this);

        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(mMessage);

        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.showToast("You clicked the button! YAY!");
            }
        });
    }

    public static SlimSampleDetailsFragment createInstance(String message) {
        final SlimSampleDetailsFragment fragment = new SlimSampleDetailsFragment();

        Bundle args = new Bundle();
        args.putString(EXTRA_MESSAGE, message);
        fragment.setArguments(args);

        return fragment;
    }

    public interface ActivityCallback {

        void showToast(String message);
    }
}
