package com.metova.slim.sample;

import com.metova.slim.Slim;
import com.metova.slim.annotation.Layout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

@Layout(R.layout.activity_slim_sample)
public class SlimSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Slim.bindLayout(this);
    }
}
