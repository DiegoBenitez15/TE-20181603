package com.example.app1;

import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        TextView r = findViewById(R.id.resultado);
        String resultado = getIntent().getExtras().getString("resultado");
        r.setText(resultado);
    }

}