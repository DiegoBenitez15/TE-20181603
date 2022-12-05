package com.pf.proyectofinal.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pf.proyectofinal.Entidades.Categoria;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Fragmentos.AgregarCategoriaFragment;
import com.pf.proyectofinal.Fragmentos.AgregarProductoFragment;
import com.pf.proyectofinal.Fragmentos.EditarCategoriaFragment;
import com.pf.proyectofinal.Fragmentos.EditarProductoFragment;
import com.pf.proyectofinal.Fragmentos.ListadoCategoriaFragment;
import com.pf.proyectofinal.Fragmentos.ListadoProductoFragment;
import com.pf.proyectofinal.Modelos.CategoriaViewModel;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class CategoriaActivity extends AppCompatActivity {
    private FirebaseServicios firebaseServicios;
    private CategoriaViewModel categoriaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        firebaseServicios = new FirebaseServicios();
        categoriaViewModel = new ViewModelProvider(this).get(CategoriaViewModel.class);
        this.listarCategoria(findViewById(android.R.id.content).getRootView());
    }

    public void listarCategoria(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, ListadoCategoriaFragment.class, null,"LISTADO CATEGORIA")
                .setReorderingAllowed(true)
                .addToBackStack("name") // name can be null
                .commit();
    }

    public void agregarCategoria(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, AgregarCategoriaFragment.class, null,"AGREGAR CATEGORIA")
                .setReorderingAllowed(true)
                .addToBackStack("name") // name can be null
                .commit();
    }

    public void crearProducto(View view){
        Bitmap img = null;
        String nombre = String.valueOf(((EditText) findViewById(R.id.nombre_categoria)).getText());
        try {
            img = ((BitmapDrawable) ((ImageView) findViewById(R.id.foto_categoria)).getDrawable()).getBitmap();
        } catch (ClassCastException e) {
            img = null;
        }


        if(img != null && !nombre.equals("")){
            String url_img = firebaseServicios.uploadFile(firebaseServicios.getImageUri(this,img),null);
            Categoria c = new Categoria(nombre,url_img);
            categoriaViewModel.insert(c);
            this.listarCategoria(view);
        } else {
            Toast.makeText(this, "No se pueden dejar espacios vacios", Toast.LENGTH_SHORT).show();
        }
    }

    public void editarProducto(View view){
        EditarCategoriaFragment editarCategoriaFragment = (EditarCategoriaFragment) getSupportFragmentManager().findFragmentByTag("EDITAR CATEGORIA");
        String nombre = String.valueOf(((EditText) findViewById(R.id.nombre_categoria2)).getText());
        String id = editarCategoriaFragment.getArguments().getString("id");
        Bitmap img = null;
        try {
            img = ((BitmapDrawable) ((ImageView) findViewById(R.id.foto_categoria2)).getDrawable()).getBitmap();
        } catch (ClassCastException e) {
            img = null;
        }

        if(img !=null && !nombre.equals("")){
            Bitmap finalImg = img;
            categoriaViewModel.getCategoria(Long.parseLong(id)).observe(editarCategoriaFragment.getViewLifecycleOwner(), categoria -> {
                String url_img = firebaseServicios.uploadFile(firebaseServicios.getImageUri(this, finalImg),categoria.getImagen());
                categoria.setNombre(nombre);
                categoria.setImagen(url_img);
                categoriaViewModel.update(categoria);
                this.listarCategoria(view);
            });
        } else {
            Toast.makeText(this, "No se pueden dejar espacios vacios", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickFoto(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar opcion");
        builder.setItems(R.array.opciones_foto, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, 200);
                }else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 201);
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView img = findViewById(R.id.foto_categoria);
        if(img == null){
            img = findViewById(R.id.foto_categoria2);
        }

        if(resultCode == RESULT_OK){
            if(requestCode == 200){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                img.setImageBitmap(imageBitmap);
            }
            else if(requestCode == 201){
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream((Uri) data.getData());
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    img.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}