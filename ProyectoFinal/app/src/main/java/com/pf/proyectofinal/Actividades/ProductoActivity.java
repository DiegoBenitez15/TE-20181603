package com.pf.proyectofinal.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.pf.proyectofinal.Adaptadores.ProductoAdapter;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Fragmentos.AgregarProductoFragment;
import com.pf.proyectofinal.Fragmentos.EditarProductoFragment;
import com.pf.proyectofinal.Fragmentos.ListadoProductoFragment;
import com.pf.proyectofinal.Fragmentos.RecuperarContrasenaFragment;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class ProductoActivity extends AppCompatActivity {
    private ProductoViewModel productoViewModel;
    private FirebaseServicios firebaseServicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        this.listarProducto(findViewById(android.R.id.content).getRootView());
        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        firebaseServicios = new FirebaseServicios();
    }

    public void listarProducto(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, ListadoProductoFragment.class, null,"LISTADO PRODUCTOS")
                .setReorderingAllowed(true)
                .addToBackStack("name") // name can be null
                .commit();
    }

    public void agregarProducto(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, AgregarProductoFragment.class, null,"AGREGAR PRODUCTO")
                .setReorderingAllowed(true)
                .addToBackStack("name") // name can be null
                .commit();
    }

    public void crearProducto(View view){
        Bitmap img = null;
        String prec_producto = String.valueOf(((EditText) findViewById(R.id.precio_prod)).getText());
        String desc_producto = String.valueOf(((EditText) findViewById(R.id.descripcion_prod)).getText());
        try {
            img = ((BitmapDrawable) ((ImageView) findViewById(R.id.foto)).getDrawable()).getBitmap();
        } catch (ClassCastException e) {
            img = null;
        }


        if(img != null && !prec_producto.equals("") && !desc_producto.equals("")){
            String url_img = firebaseServicios.uploadFile(firebaseServicios.getImageUri(this,img));
            //Producto p = new Producto(Double.parseDouble(prec_producto),desc_producto,url_img);
            //productoViewModel.insert(p);
            this.listarProducto(view);
        } else {
            Toast.makeText(this, "No se pueden dejar espacios vacios", Toast.LENGTH_SHORT).show();
        }
    }

    public void editarProducto(View view){
        EditarProductoFragment editarProductoFragment = (EditarProductoFragment) getSupportFragmentManager().findFragmentByTag("EDITAR PRODUCTO");
        String prec_producto = String.valueOf(((EditText) findViewById(R.id.precio_prod2)).getText());
        String desc_producto = String.valueOf(((EditText) findViewById(R.id.descripcion_prod2)).getText());
        String id = editarProductoFragment.getArguments().getString("id");
        Bitmap img = null;
        try {
            img = ((BitmapDrawable) ((ImageView) findViewById(R.id.foto2)).getDrawable()).getBitmap();
        } catch (ClassCastException e) {
            img = null;
        }

        if(!prec_producto.equals("") && !desc_producto.equals("")){
            Bitmap finalImg = img;
            productoViewModel.getProducto(UUID.fromString(id)).observe(editarProductoFragment.getViewLifecycleOwner(), producto -> {
                String url_img = firebaseServicios.uploadFile(firebaseServicios.getImageUri(this, finalImg));
                producto.setImg(url_img);
                producto.setDescripcion(desc_producto);
                producto.setPrecio(Double.parseDouble(prec_producto));
                productoViewModel.update(producto);
                this.listarProducto(view);
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
        ImageView img = findViewById(R.id.foto);
        if(img == null){
            img = findViewById(R.id.foto2);
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