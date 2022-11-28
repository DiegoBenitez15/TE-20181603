package com.pf.proyectofinal.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;
import com.pf.proyectofinal.databinding.FragmentDescripcionProductoBinding;

import java.util.UUID;


public class DescripcionProductoFragment extends Fragment {
    private ProductoViewModel productoViewModel;
    private FirebaseServicios firebaseServicios;
    private FragmentDescripcionProductoBinding binding;



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        String id = getArguments().getString("id");
        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        firebaseServicios = new FirebaseServicios();

        productoViewModel.getProducto(UUID.fromString(id)).observe(getViewLifecycleOwner(), producto -> {
            firebaseServicios.downloadImage(producto.getImg(),binding.imageView2);
            TextView precio = binding.preProd;
            TextView descripcion = binding.desProd;
            precio.setText(String.valueOf("Precio: $" + producto.getPrecio()));
            descripcion.setText(producto.getDescripcion());
        });

        binding = FragmentDescripcionProductoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}