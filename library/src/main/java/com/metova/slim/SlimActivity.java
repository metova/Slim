package com.metova.slim;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class SlimActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View layout = Slim.createLayout(this, this);
        if (layout != null) {
            setContentView(layout);
        }

        Slim.injectExtras(getIntent().getExtras(), this);
    }
}
