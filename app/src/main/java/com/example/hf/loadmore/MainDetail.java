package com.example.hf.loadmore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by HF on 2017/7/12.
 */

public class MainDetail extends AppCompatActivity{

    private TextView detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_detail);
        detail = (TextView)findViewById(R.id.tv_detail);
        Intent intent = getIntent();
        String s = intent.getStringExtra("detail");
        detail.setText(s);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
