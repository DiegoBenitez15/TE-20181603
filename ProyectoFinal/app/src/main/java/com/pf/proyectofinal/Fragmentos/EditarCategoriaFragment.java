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

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pf.proyectofinal.Modelos.CategoriaViewModel;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;
import com.pf.proyectofinal.databinding.FragmentEditarCategoriaBinding;
import com.pf.proyectofinal.databinding.FragmentEditarProductoBinding;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;


public class EditarCategoriaFragment extends Fragment {
    private CategoriaViewModel categoriaViewModel;
    private FirebaseServicios firebaseServicios;
    private FragmentEditarCategoriaBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        String id = getArguments().getString("id");
        categoriaViewModel = new ViewModelProvider(this).get(CategoriaViewModel.class);
        firebaseServicios = new FirebaseServicios();

        categoriaViewModel.getCategoria(Long.parseLong(id)).observe(getViewLifecycleOwner(), categoria -> {
            firebaseServicios.downloadImage(categoria.getImagen(),binding.fotoCategoria2);
            binding.nombreCategoria2.setText(categoria.getNombre());
        });

        binding = FragmentEditarCategoriaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fotoCategoria2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
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

        binding.crearProducto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = String.valueOf(((EditText) binding.nombreCategoria2).getText());
                String id = getArguments().getString("id");
                Bitmap img = null;
                try {
                    img = ((BitmapDrawable) ((ImageView) binding.fotoCategoria2).getDrawable()).getBitmap();
                } catch (ClassCastException e) {
                    img = null;
                }

                if(img !=null && !nombre.equals("")){
                    Bitmap finalImg = img;
                    categoriaViewModel.getCategoria(Long.parseLong(id)).observe(getViewLifecycleOwner(), categoria -> {
                        String url_img = firebaseServicios.uploadFile(firebaseServicios.getImageUri(getContext(), finalImg));
                        categoria.setNombre(nombre);
                        categoria.setImagen(url_img);
                        categoriaViewModel.update(categoria);
                        getFragmentManager().popBackStackImmediate();
                    });
                } else {
                    Toast.makeText(getContext(), "No se pueden dejar espacios vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.volverProducto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView img = binding.fotoCategoria2;

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