package com.example.practica3.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.practica3.Entidades.Producto;
import com.example.practica3.ModelViews.ProductoViewModel;
import com.example.practica3.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ProductoViewModel mv_productos;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ItemAdapter adapter = new ItemAdapter(this);

        ItemAdapter p = new ItemAdapter(this);
        mv_productos = new ViewModelProvider(this).get(ProductoViewModel.class);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        recyclerView = (RecyclerView) findViewById(R.id.lista);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        mv_productos.getAll().observe(this, productos -> {
            p.setList(productos);
            recyclerView.setAdapter(p);
        });
    }

    public void crearProducto(View view){
        Intent i = new Intent(this, AgregarProducto.class);
        startActivityForResult(i,200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if(requestCode == 200) {
                String nombre = data.getExtras().getString("nombre");
                String marca = data.getExtras().getString("marca");
                String precio = data.getExtras().getString("precio");
                Uri uri = Uri.parse(data.getExtras().getString("uri"));

                String ref = uploadImage(uri);
                Producto p = new Producto(nombre, marca, Integer.parseInt(precio),ref);
                mv_productos.insert(p);
            }
        }
    }


    public String uploadImage(Uri imageUri)
    {
        if (imageUri != null) {
            String id ="images/" + UUID.randomUUID().toString();
            final StorageReference imageRef = storageReference.child(id);

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot snapshot) {
                            Task<Uri> downloadUri = imageRef.getDownloadUrl();
                            while (!downloadUri.isSuccessful());
                            Uri downloadUrl = downloadUri.getResult();
                            downloadUrl.toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // show message on failure may be network/disk ?
                        }
                    });
            return id;
        }
        return null;
    }

    public void iniciarActividad(int id){
        Intent i = new Intent(this, EditarProducto.class);
        i.putExtra("id",id);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

}