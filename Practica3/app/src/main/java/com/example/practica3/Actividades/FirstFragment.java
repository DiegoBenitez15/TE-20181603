package com.example.practica3.Actividades;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.practica3.Entidades.Producto;
import com.example.practica3.ModelViews.ProductoViewModel;
import com.example.practica3.R;
import com.example.practica3.databinding.FragmentFirstBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class FirstFragment extends Fragment {
    private FragmentFirstBinding binding;
    private ProductoViewModel mv_productos;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();


//        ItemAdapter adapter = new ItemAdapter(FirstFragment.this);
//
//        mv_productos = new ViewModelProvider(this).get(ProductoViewModel.class);
//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();
//        RecyclerView recyclerView = binding.lista;
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
//
//
//        mv_productos.getAll().observe(getViewLifecycleOwner(), productos -> {
//            adapter.setList(productos);
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 201) {
                int id = data.getExtras().getInt("id");
                String nombre = data.getExtras().getString("nombre");
                String marca = data.getExtras().getString("marca");
                String precio = data.getExtras().getString("precio");
                Uri uri = Uri.parse(data.getExtras().getString("uri"));


                mv_productos.getProducto(id).observe(this,producto -> {
                    // reuploadImage(uri,producto.getImage());
                    producto.setNombre(nombre);
                    producto.setMarca(marca);
                    producto.setPrecio(Float.parseFloat(precio));
                    mv_productos.update(producto);
                });
            }
        }
    }

    public String uploadImage(Uri imageUri)
    {
        if (imageUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String id ="images/" + UUID.randomUUID().toString();

            StorageReference ref = storageReference.child(id);

            ref.putFile(imageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(),"Se ha registrado el articulo adecuadamente",Toast.LENGTH_LONG).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void iniciarActividad(Intent i){
        startActivityForResult(i,201);
    }
}