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
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;
import com.pf.proyectofinal.databinding.FragmentEditarProductoBinding;
import com.pf.proyectofinal.databinding.FragmentListadoProductoBinding;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class EditarProductoFragment extends Fragment {
    private ProductoViewModel productoViewModel;
    private FirebaseServicios firebaseServicios;
    private FragmentEditarProductoBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        String id = getArguments().getString("id");
        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        firebaseServicios = new FirebaseServicios();

        productoViewModel.getProducto(UUID.fromString(id)).observe(getViewLifecycleOwner(), producto -> {
            firebaseServicios.downloadImage(producto.getImg(),binding.foto2);
            TextView precio = binding.precioProd2;
            TextView descripcion = binding.descripcionProd2;
            precio.setText(String.valueOf(producto.getPrecio()));
            descripcion.setText(producto.getDescripcion());
        });

        binding = FragmentEditarProductoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.foto2.setOnClickListener(new View.OnClickListener() {
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

        binding.editarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prec_producto = String.valueOf((binding.precioProd2).getText());
                String desc_producto = String.valueOf((binding.descripcionProd2).getText());
                String id = getArguments().getString("id");
                Bitmap img = null;
                try {
                    img = ((BitmapDrawable) (binding.foto2).getDrawable()).getBitmap();
                } catch (ClassCastException e) {
                    img = null;
                }

                if(!prec_producto.equals("") && !desc_producto.equals("")){
                    Bitmap finalImg = img;
                    productoViewModel.getProducto(UUID.fromString(id)).observe(getViewLifecycleOwner(), producto -> {
                        String url_img = firebaseServicios.uploadFile(firebaseServicios.getImageUri(getContext(), finalImg));
                        producto.setImg(url_img);
                        producto.setDescripcion(desc_producto);
                        producto.setPrecio(Double.parseDouble(prec_producto));
                        productoViewModel.update(producto);
                        getFragmentManager().popBackStackImmediate();
                    });
                } else {
                    Toast.makeText(getContext(), "No se pueden dejar espacios vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.volverProducto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView img = binding.foto2;

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