package com.te.parcial1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Producto> productos;
    RecyclerView recyclerView;
    private int requestCode = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productos = new ArrayList<Producto>();
        productos.add(new Producto("Prueba","Prueba",555));

        recyclerView = (RecyclerView) findViewById(R.id.lista);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ItemAdapter(productos,this));
    }

    public void crearProducto(View view){
        Intent i = new Intent(this, Second_Activity.class);
        startActivityForResult(i,requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            String articulo = data.getExtras().getString("articulo");
            String descripcion = data.getExtras().getString("descripcion");
            String precio = data.getExtras().getString("precio");

            Producto p = new Producto(articulo,descripcion,Integer.parseInt(precio));
            productos.add(p);
            recyclerView.setAdapter(new ItemAdapter(productos,this));
        }
    }

    public void iniciarActividad(Intent i){
        startActivity(i);
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}