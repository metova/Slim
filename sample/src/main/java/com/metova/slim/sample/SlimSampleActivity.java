package com.metova.slim.sample;

import com.metova.slim.Slim;
import com.metova.slim.annotation.Layout;

import android.app.Activity;
import android.os.Bundle;

@Layout(R.layout.activity_slim_sample)
public class SlimSampleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Slim.bindLayout(this);
    }
}
