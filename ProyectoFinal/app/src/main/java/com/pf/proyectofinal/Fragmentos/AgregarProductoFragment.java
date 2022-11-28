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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.pf.proyectofinal.Entidades.Categoria;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Modelos.CategoriaViewModel;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;
import com.pf.proyectofinal.databinding.FragmentAgregarCategoriaBinding;
import com.pf.proyectofinal.databinding.FragmentAgregarProductoBinding;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AgregarProductoFragment extends Fragment {
    private FragmentAgregarProductoBinding binding;
    private ProductoViewModel productoViewModel;
    private CategoriaViewModel categoriaViewModel;
    private FirebaseServicios firebaseServicios;


    public AgregarProductoFragment() {
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        categoriaViewModel = new ViewModelProvider(this).get(CategoriaViewModel.class);
        firebaseServicios = new FirebaseServicios();


        categoriaViewModel.getAll().observe(getViewLifecycleOwner(),categorias -> {
            List<String> list = new ArrayList<String>();

            for(Categoria c: categorias){
                list.add(c.getNombre());
            }

            String[] stringArray = list.toArray(new String[0]);


            ArrayAdapter ad = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,stringArray);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.categoria.setAdapter(ad);

            if(getArguments().getLong("id") != 0) {
                binding.categoria.setEnabled(false);
                binding.categoria.setVisibility(View.INVISIBLE);
            }

        });

        binding.foto.setOnClickListener(new View.OnClickListener() {
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

        binding.crearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap img = null;
                long id = getArguments().getLong("id");
                String categoria = ((Spinner)binding.categoria).getSelectedItem().toString();
                String prec_producto = String.valueOf((binding.precioProd).getText());
                String desc_producto = String.valueOf(((binding.descripcionProd)).getText());
                try {
                    img = ((BitmapDrawable) (binding.foto).getDrawable()).getBitmap();
                } catch (ClassCastException e) {
                    img = null;
                }


                if(img != null && !prec_producto.equals("") && !desc_producto.equals("")){
                    Bitmap finalImg = img;
                    categoriaViewModel.getCategoriaByName(categoria).observe(getViewLifecycleOwner(), categoria1 -> {
                        String url_img = firebaseServicios.uploadFile(firebaseServicios.getImageUri(getContext(), finalImg));
                        Producto p = null;

                        if(id == 0) {
                            p = new Producto(Double.parseDouble(prec_producto), desc_producto, url_img, categoria1.getId());
                        }
                        else{
                            p = new Producto(Double.parseDouble(prec_producto), desc_producto, url_img, id);
                        }
                        productoViewModel.insert(p);
                        getFragmentManager().popBackStackImmediate();
                    });
                } else {
                    Toast.makeText(getContext(), "No se pueden dejar espacios vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.volverProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView img = binding.foto;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAgregarProductoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}