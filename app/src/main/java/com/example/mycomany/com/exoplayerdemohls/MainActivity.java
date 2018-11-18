package com.example.mycomany.com.exoplayerdemohls;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.content.Intent intent = new android.content.Intent(this, PlayerActivity.class);
        startActivity(intent);
    }
}
