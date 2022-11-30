package com.pf.proyectofinal.Fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pf.proyectofinal.Entidades.ProductoCarro;
import com.pf.proyectofinal.Modelos.ProductoCarroViewModel;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.Modelos.UsuarioViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;
import com.pf.proyectofinal.databinding.FragmentDescripcionProductoBinding;

import java.util.UUID;


public class DescripcionProductoFragment extends Fragment {
    private ProductoViewModel productoViewModel;
    private ProductoCarroViewModel productoCarroViewModel;
    private UsuarioViewModel usuarioViewModel;
    private FirebaseServicios firebaseServicios;
    private FragmentDescripcionProductoBinding binding;
    private UUID id;
    private String user;



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        id = UUID.fromString(getArguments().getString("id"));
        user = getActivity().getIntent().getExtras().getString("user");
        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        productoCarroViewModel = new ViewModelProvider(this).get(ProductoCarroViewModel.class);
        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        firebaseServicios = new FirebaseServicios();

        productoViewModel.getProducto(id).observe(getViewLifecycleOwner(), producto -> {
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

        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv = binding.amount;
                String value = String.valueOf(Integer.parseInt(tv.getText().toString()) + 1);

                binding.amount.setText(value);
            }
        });

        binding.subs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv = binding.amount;
                int value = Integer.parseInt(tv.getText().toString()) - 1;

                if(value > 0 ) {
                    String v = String.valueOf(value);
                    binding.amount.setText(v);
                }
            }
        });

        binding.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productoViewModel.getProducto(id).observe(getViewLifecycleOwner(),producto -> {
                    usuarioViewModel.getUser(null,user).observe(getViewLifecycleOwner(),usuario -> {
                        productoCarroViewModel.getByUser(user).observe(getViewLifecycleOwner(),productoCarros -> {
                            ProductoCarro p2 = null;

                            for(ProductoCarro p:productoCarros){
                                if(p.getCodigo_producto().equals(producto.getCodigo())){
                                    p2 = p;
                                    break;
                                }
                            }

                            if(p2 == null) {
                                ProductoCarro pc = new ProductoCarro(producto.getCodigo(), usuario.getUsuario(), Integer.parseInt(binding.amount.getText().toString()), -1);
                                productoCarroViewModel.insert(pc);
                            } else {
                                p2.setCantidad(p2.getCantidad() + Integer.parseInt(binding.amount.getText().toString()));
                                productoCarroViewModel.update(p2);
                            }

                            NavHostFragment.findNavController(DescripcionProductoFragment.this).popBackStack();
                        }
                        );

                    });
                });
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}