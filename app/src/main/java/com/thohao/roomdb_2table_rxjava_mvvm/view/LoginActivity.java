package com.thohao.roomdb_2table_rxjava_mvvm.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.thohao.roomdb_2table_rxjava_mvvm.R;

public class LoginActivity extends AppCompatActivity {
    Button mButton_logn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mButton_logn=findViewById(R.id.btn_login);
        mButton_logn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ClassActivity.class));
                Toast.makeText(LoginActivity.this,"Login successfull",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        getSupportActionBar().hide();

    }
}
