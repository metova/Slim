package com.metova.slim;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class SlimActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Slim.injectExtras(getIntent().getExtras(), this);
    }
}
