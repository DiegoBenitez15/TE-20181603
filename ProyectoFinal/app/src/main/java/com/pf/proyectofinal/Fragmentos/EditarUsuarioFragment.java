package com.pf.proyectofinal.Fragmentos;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.pf.proyectofinal.Modelos.UsuarioViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;
import com.pf.proyectofinal.databinding.FragmentEditarUsuarioBinding;
import com.pf.proyectofinal.databinding.FragmentPerfilUsuarioBinding;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditarUsuarioFragment extends Fragment {
    private UsuarioViewModel usuarioViewModel;
    private FirebaseServicios firebaseServicios;
    private FragmentEditarUsuarioBinding binding;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        firebaseServicios = new FirebaseServicios();



        binding = FragmentEditarUsuarioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String usuario = getActivity().getIntent().getExtras().getString("user");
        usuarioViewModel.getUser(null,usuario).observe(getViewLifecycleOwner(),usuario1 -> {
            binding.nmbrPerfil2.setText(usuario1.getNombre());
            binding.corrPerfil2.setText(usuario1.getCorreo());
            binding.cntPerfil2.setText(usuario1.getContacto());
            firebaseServicios.downloadImage(usuario1.getUrl_img(),binding.imgPerfil2);
        });

        binding.edtPerfil2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = getActivity().getIntent().getExtras().getString("user");
                String correo = binding.corrPerfil2.getText().toString();
                String nombre = binding.nmbrPerfil2.getText().toString();
                String contacto = binding.cntPerfil2.getText().toString();
                Bitmap img = null;
                try {
                    img = ((BitmapDrawable) (binding.imgPerfil2).getDrawable()).getBitmap();
                } catch (ClassCastException e) {
                    img = null;
                }

                Bitmap finalImg = img;
                if(img != null && !nombre.equals("") && !correo.equals("") && !contacto.equals("") ){
                    usuarioViewModel.getUser(null,usuario).observe(getViewLifecycleOwner(), usuario1 -> {

                        usuarioViewModel.getUser(correo,null).observe(getViewLifecycleOwner(),usuario2 -> {
                            if(usuario2 == null || correo.equals(usuario1.getCorreo())){
                                usuario1.setCorreo(correo);
                                usuario1.setNombre(nombre);
                                usuario1.setContacto(contacto);
                                usuario1.setUrl_img(firebaseServicios.uploadFile(firebaseServicios.getImageUri(getContext(),finalImg),usuario1.getUrl_img()));
                                usuarioViewModel.update(usuario1);
                                NavHostFragment.findNavController(EditarUsuarioFragment.this).popBackStack();
                            } else {
                                Toast.makeText(getContext(), "Ya existe un usuario con ese correo", Toast.LENGTH_SHORT).show();
                            }
                        });

                    });
                } else {
                    Toast.makeText(getContext(), "No se puede dejar espacios vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.imgPerfil2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView img = binding.imgPerfil2;

        if(resultCode == RESULT_OK){
            if(requestCode == 200){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                img.setImageBitmap(imageBitmap);
            }
            else if(requestCode == 201){
                final InputStream imageStream;
                try {
                    imageStream = getActivity().getContentResolver().openInputStream((Uri) data.getData());
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    img.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}