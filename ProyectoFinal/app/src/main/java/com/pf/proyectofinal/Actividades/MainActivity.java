package com.pf.proyectofinal.Actividades;

import androidx.appcompat.app.AppCompatActivity;
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

import com.pf.proyectofinal.Entidades.Usuario;
import com.pf.proyectofinal.Fragmentos.IniciarSesionFragment;
import com.pf.proyectofinal.Fragmentos.RecuperarContrasenaFragment;
import com.pf.proyectofinal.Fragmentos.RegistrarseFragment;
import com.pf.proyectofinal.Modelos.UsuarioViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private UsuarioViewModel model_usuario;
    private FirebaseServicios firebaseServicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model_usuario =  new ViewModelProvider(this).get(UsuarioViewModel.class);
        firebaseServicios = new FirebaseServicios();
        this.iniciarSesion(findViewById(android.R.id.content).getRootView());
    }

    public void iniciarSesion(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, IniciarSesionFragment.class, null,"INICIAR")
                .setReorderingAllowed(true)
                .addToBackStack(null) // name can be null
                .commit();
    }

    public void olvidarContrasena(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, RecuperarContrasenaFragment.class, null,"RECUPERAR")
                .setReorderingAllowed(true)
                .addToBackStack(null) // name can be null
                .commit();
    }

    public void registrarse(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, RegistrarseFragment.class,null, "REGISTRAR")
                .setReorderingAllowed(true)
                .addToBackStack(null) // name can be null
                .commit();
    }

    public void registarUsuario(View view){
        String nombre = String.valueOf(((EditText) findViewById(R.id.nmbr_perfil2)).getText());
        String usuario = String.valueOf(((EditText) findViewById(R.id.usuario)).getText());
        String correo = String.valueOf(((EditText) findViewById(R.id.corr_perfil2)).getText());
        String contrasena= String.valueOf(((EditText) findViewById(R.id.contrasena)).getText());
        String contrasena2= String.valueOf(((EditText) findViewById(R.id.contrasena2)).getText());
        String contacto= String.valueOf(((EditText) findViewById(R.id.cnt_perfil2)).getText());
        Bitmap img = null;
        try {
            img = ((BitmapDrawable) ((ImageView) findViewById(R.id.img_perfil2)).getDrawable()).getBitmap();
        } catch (ClassCastException e) {
            img = null;
        }
        RegistrarseFragment registrarseFragment = (RegistrarseFragment)getSupportFragmentManager().findFragmentByTag("REGISTRAR");
        if (img != null && !nombre.equals("") && !usuario.equals("") && !correo.equals("") && !contrasena.equals("") && !contrasena2.equals("") && !contacto.equals("")) {
            assert registrarseFragment != null;
            Bitmap finalImg = img;
            model_usuario.getUsers(correo,usuario).observe(registrarseFragment.getViewLifecycleOwner(), usuarios -> {
                if (usuarios.size() != 0) {
                    Toast.makeText(this, "Usuario o correo ya existente", Toast.LENGTH_SHORT).show();
                }else{
                    if (contrasena.equals(contrasena2)) {
                        String url_img = firebaseServicios.uploadFile(firebaseServicios.getImageUri(this,finalImg));
                        Usuario u = new Usuario(usuario, nombre, correo, contrasena, contacto, url_img);
                        model_usuario.insert(u);
                        Toast.makeText(this, "Se registro el usuario correctamente", Toast.LENGTH_SHORT).show();
                        this.iniciarSesion(view);
                    }else {
                        Toast.makeText(this, "Las contrasenas no son identicas", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else{
            Toast.makeText(this, "Se deben complentar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void iniciarUsuario(View view){
        String id = String.valueOf(((EditText) findViewById(R.id.correo2)).getText()).replace(" ","");
        String contrasena= String.valueOf(((EditText) findViewById(R.id.contrasenalogin)).getText());
        IniciarSesionFragment iniciarSesionFragment = (IniciarSesionFragment) getSupportFragmentManager().findFragmentByTag("INICIAR");

        if (!id.equals("") && !contrasena.equals("")){
            assert iniciarSesionFragment != null;
            model_usuario.getUser(id,id).observe(iniciarSesionFragment.getViewLifecycleOwner(), usuario -> {
                if (usuario == null){
                    Toast.makeText(this, "El usuario solicitado no existe", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(usuario.getContrasena().equals(contrasena)){
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    intent.putExtra("user",usuario.getUsuario());
                    startActivity(intent);
                    return;
                }

                Toast.makeText(this, "La contrasena NO es valida", Toast.LENGTH_SHORT).show();
                return;
            });
        }else{
            Toast.makeText(this, "No se pueden dejar los campos vacios", Toast.LENGTH_SHORT).show();
        }
    }

    public void recuperarContrasena(View view){
        String correo = String.valueOf(((EditText) findViewById(R.id.correo2)).getText());
        RecuperarContrasenaFragment recuperarContrasenaFragment = (RecuperarContrasenaFragment) getSupportFragmentManager().findFragmentByTag("RECUPERAR");

        assert recuperarContrasenaFragment != null;
        model_usuario.getUser(correo,null).observe(recuperarContrasenaFragment.getViewLifecycleOwner(), usuario -> {
            if(usuario != null){
                Toast.makeText(this, "La contraseña es " + usuario.getContrasena(), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "El usuario solicitado no existe", Toast.LENGTH_SHORT).show();
            }
        });
        return;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 200){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ((ImageView) findViewById(R.id.img_perfil2)).setImageBitmap(imageBitmap);
            }
            else if(requestCode == 201){
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream((Uri) data.getData());
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ((ImageView) findViewById(R.id.img_perfil2)).setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}