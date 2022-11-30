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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pf.proyectofinal.Actividades.CategoriaActivity;
import com.pf.proyectofinal.Actividades.ProductoActivity;
import com.pf.proyectofinal.Adaptadores.ProductoAdapter;
import com.pf.proyectofinal.Entidades.Categoria;
import com.pf.proyectofinal.Modelos.CategoriaViewModel;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;
import com.pf.proyectofinal.databinding.FragmentAgregarCategoriaBinding;
import com.pf.proyectofinal.databinding.FragmentListadoProductoBinding;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AgregarCategoriaFragment extends Fragment {
    private FragmentAgregarCategoriaBinding binding;
    private CategoriaViewModel categoriaViewModel;
    private FirebaseServicios firebaseServicios;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAgregarCategoriaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoriaViewModel = new ViewModelProvider(this).get(CategoriaViewModel.class);
        firebaseServicios = new FirebaseServicios();

        binding.fotoCategoria.setOnClickListener(new View.OnClickListener(){
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

        binding.crearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap img = null;
                String nombre = String.valueOf(((EditText) binding.nombreCategoria).getText());
                try {
                    img = ((BitmapDrawable) ((ImageView) binding.fotoCategoria).getDrawable()).getBitmap();
                } catch (ClassCastException e) {
                    img = null;
                }


                if(img != null && !nombre.equals("")){
                    String url_img = firebaseServicios.uploadFile(firebaseServicios.getImageUri(getContext(),img));
                    Categoria c = new Categoria(nombre,url_img);
                    categoriaViewModel.insert(c);
                    NavHostFragment.findNavController(AgregarCategoriaFragment.this).popBackStack();
                } else {
                    Toast.makeText(getContext(), "No se pueden dejar espacios vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView img = binding.fotoCategoria;

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