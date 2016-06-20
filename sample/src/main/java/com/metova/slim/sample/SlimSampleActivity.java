package com.metova.slim.sample;

import com.metova.slim.Slim;
import com.metova.slim.annotation.Layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

@Layout(R.layout.activity_slim_sample)
public class SlimSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Slim.bindLayout(this);

        final EditText paramEditText = (EditText) findViewById(R.id.param);
        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SlimSampleActivity.this, SlimSampleDetailsActivity.class);
                intent.putExtra(SlimSampleDetailsActivity.EXTRA_MESSAGE, paramEditText.getText().toString());
                startActivity(intent);
            }
        });
    }
}
