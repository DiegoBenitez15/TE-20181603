package com.example.practica3.Actividades;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.practica3.Entidades.Producto;
import com.example.practica3.ModelViews.ProductoViewModel;
import com.example.practica3.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class EditarProducto extends AppCompatActivity {
    private int id;
    private ProductoViewModel mv_productos;
    private EditText nombre;
    private EditText marca;
    private EditText precio;
    private ImageView img_view;
    private StorageReference storageReference;
    private Uri imageUri;
    private FirebaseStorage storage;
    private int REQUEST_IMAGE_CAPTURE = 200;
    private int RESULT_LOAD_IMG = 201;
    private int SELECT_PICTURE = 202;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);
        this.nombre = findViewById(R.id.nombre);
        this.marca = findViewById(R.id.marca);
        this.precio = findViewById(R.id.precio);
        this.img_view = (ImageView) findViewById(R.id.imageView2);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        id = getIntent().getIntExtra("id",-1);
        mv_productos = new ViewModelProvider(this).get(ProductoViewModel.class);
        mv_productos.getProducto(id);
        List<Producto> ps = new ArrayList<Producto>();

        mv_productos.getProducto(id).observe(this,producto -> {
            downloadImage(producto.getImage());
            nombre.setText(producto.getNombre());
            marca.setText(producto.getMarca());
            precio.setText(String.valueOf(producto.getPrecio()));
        });

        img_view.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        popupMenu();
                    }});

    }

    public void limpiar(View view){
        nombre.setText("");
        marca.setText("");
        precio.setText("");
        imageUri = null;
        img_view.setImageBitmap(null);
    }

    public void editar(View view){
        String nombre = String.valueOf(this.nombre.getText());
        String marca = String.valueOf(this.marca.getText());
        String precio = this.precio.getText().toString();


        if(!nombre.equals("") && !marca.equals("") && !precio.equals("") && imageUri!=null){
            mv_productos.getProducto(id).observe(this,producto -> {
                producto.setNombre(nombre);
                producto.setMarca(marca);
                producto.setPrecio(Float.parseFloat(precio));
                mv_productos.update(producto);
            });
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }else{
            Toast.makeText(this,"Todos los campos deben ser llenados",Toast.LENGTH_LONG).show();
        }
    }

    public void eliminar(View view){
        mv_productos.getProducto(id).observe(this,producto -> {
            producto.setEstado('D');
            mv_productos.update(producto);
        });
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void popupMenu()
    {
        //Crea instancia a PopupMenu
        PopupMenu popup = new PopupMenu(this, img_view);
        popup.getMenuInflater().inflate(R.menu.my_popupmenu , popup.getMenu());
        //registra los eventos click para cada item del menu
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_one)
                {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    } catch (ActivityNotFoundException e) {
                        // display error state to the user
                    }
                }
                else if (item.getItemId() == R.id.action_two)
                {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                }
                else if (item.getItemId() == R.id.action_three)
                {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
                }
                return true;
            }
        });
        popup.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, "Title", null);
            imageUri = Uri.parse(path);
            img_view.setImageBitmap(imageBitmap);
            mv_productos.getProducto(id).observe(EditarProducto.this,producto -> {
                uploadImage(imageUri,producto.getImage());
            });
        }
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                img_view.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(EditarProducto.this, "Algo salio mal", Toast.LENGTH_LONG).show();
            }
            mv_productos.getProducto(id).observe(EditarProducto.this,producto -> {
                uploadImage(imageUri,producto.getImage());
            });
        }
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                final InputStream imageStream;
                imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                img_view.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mv_productos.getProducto(id).observe(EditarProducto.this,producto -> {
                uploadImage(imageUri,producto.getImage());
            });
        }
    }

    public void downloadImage(String url){
        StorageReference pathReference = storageReference.child(url);

        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] b) {
                Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
                img_view.setImageBitmap(bmp);
                String s = null;
                try {
                    s = new String(b,"UTF-8");
                    imageUri = Uri.parse(s);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public String uploadImage(Uri imageUri,String id)
    {
        if (imageUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child(id);

            ref.putFile(imageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditarProducto.this,"Se ha registrado el articulo adecuadamente",Toast.LENGTH_LONG).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(EditarProducto.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                                }
                            });
            return id;
        }
        return null;
    }
}