package com.thohao.roomdb_2table_rxjava_mvvm.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.thohao.roomdb_2table_rxjava_mvvm.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

//SplashScreen
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}
