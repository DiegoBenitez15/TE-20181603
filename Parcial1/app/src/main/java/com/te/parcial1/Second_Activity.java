package com.te.parcial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Second_Activity extends AppCompatActivity {
    private EditText articulo;
    private EditText descripcion;
    private EditText precio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        this.articulo = findViewById(R.id.articulo);
        this.descripcion = findViewById(R.id.descripcion);
        this.precio = findViewById(R.id.precio);
    }

    public void limpiar(View view){
        articulo.setText("");
        descripcion.setText("");
        precio.setText("");
    }

    public void agregar(View view){
        String articulo = String.valueOf(this.articulo.getText());
        String descripcion = String.valueOf(this.descripcion.getText());
        String precio = this.precio.getText().toString();

        if(!articulo.equals("") && !descripcion.equals("") && !precio.equals("")){
            Intent data = new Intent();
            Toast.makeText(this,"Se ha registrado el articulo adecuadamente",Toast.LENGTH_LONG).show();
            setResult(MainActivity.RESULT_OK, data);
            data.putExtra("articulo",articulo);
            data.putExtra("descripcion",descripcion);
            data.putExtra("precio",precio);
            finish();
        } else{
            Toast.makeText(this,"Todos los campos deben ser llenados",Toast.LENGTH_LONG).show();
        }
    }

    public void volver(View view){
        Intent data = new Intent();
        setResult(MainActivity.RESULT_CANCELED, data);
        finish();
    }
}