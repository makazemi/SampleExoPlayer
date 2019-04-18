package com.example.fullnewmyexoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.exoplayer2.util.Log;

public class Main3Activity extends BaseActivity {

    private static final String TAG ="Main3Activity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Log.e(TAG,"value: "+value);
    }
}
