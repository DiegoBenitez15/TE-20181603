package com.example.practica3.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.UUID;

public class AgregarProducto extends AppCompatActivity {
    private EditText nombre;
    private EditText marca;
    private EditText precio;
    private ImageView imgview;
    private Uri imageUri;
    private int REQUEST_IMAGE_CAPTURE = 200;
    private int RESULT_LOAD_IMG = 201;
    private int SELECT_PICTURE = 202;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        this.nombre = findViewById(R.id.nombre);
        this.marca = findViewById(R.id.marca);
        this.precio = findViewById(R.id.precio);
        imgview = (ImageView)findViewById( R.id.imgView );
        imgview.setOnClickListener(
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
        imgview.setImageBitmap(null);
    }

    public void agregar(View view){
        String nombre = String.valueOf(this.nombre.getText());
        String marca = String.valueOf(this.marca.getText());
        String precio = this.precio.getText().toString();

        if(!nombre.equals("") && !marca.equals("") && !precio.equals("") && imageUri !=null){
            Intent data = new Intent();
            setResult(MainActivity.RESULT_OK, data);
            data.putExtra("nombre",nombre);
            data.putExtra("marca",marca);
            data.putExtra("precio",precio);
            data.putExtra("uri",imageUri.toString());
            finish();
        } else{
            Toast.makeText(this,"Todos los campos deben ser llenados",Toast.LENGTH_LONG).show();
        }
    }

    private void popupMenu()
    {
        //Crea instancia a PopupMenu
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.imgView));
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
            imgview.setImageBitmap(imageBitmap);

        }
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imgview.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AgregarProducto.this, "Algo salio mal", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                final InputStream imageStream;
                imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imgview.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}