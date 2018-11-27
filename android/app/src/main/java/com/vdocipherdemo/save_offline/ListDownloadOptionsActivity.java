package com.vdocipherdemo.save_offline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vdocipherdemo.Constants;
import com.vdocipherdemo.R;

public class ListDownloadOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_download_options);

        Intent intent = getIntent();
        String title = intent.getStringExtra(Constants.TITLE);
    }
}
