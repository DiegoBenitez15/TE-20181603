package com.pf.proyectofinal.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pf.proyectofinal.Actividades.ProductoActivity;
import com.pf.proyectofinal.Adaptadores.ProductoAdapter;
import com.pf.proyectofinal.Entidades.CompraProductos;
import com.pf.proyectofinal.Modelos.CompraProductosViewModal;
import com.pf.proyectofinal.Modelos.ProductoCarroViewModel;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;
import com.pf.proyectofinal.databinding.FragmentListadoProductoBinding;

public class ListadoProductoFragment extends Fragment {
    private FragmentListadoProductoBinding binding;
    private ProductoViewModel productoViewModel;
    private ProductoCarroViewModel productoCarroViewModel;
    private FirebaseServicios firebaseServicios;
    private long id_categoria;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentListadoProductoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        productoCarroViewModel = new ViewModelProvider(this).get(ProductoCarroViewModel.class);
        firebaseServicios = new FirebaseServicios();
        RecyclerView recyclerView = binding.listado;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        try{
            id_categoria = getArguments().getLong("id");
            productoViewModel.getProductoByCategoria(id_categoria).observe(getViewLifecycleOwner(), productos -> {
                ProductoAdapter adapter = new ProductoAdapter(this, productos, productoViewModel,productoCarroViewModel);
                recyclerView.setAdapter(adapter);
            });
        }catch (NullPointerException e){
            productoViewModel.getAll().observe(getViewLifecycleOwner(), productos -> {
                ProductoAdapter adapter = new ProductoAdapter(this, productos, productoViewModel,productoCarroViewModel);
                recyclerView.setAdapter(adapter);
            });
        }

        binding.floatingActionButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Bundle bundle = new Bundle();
                if(id_categoria != -1){
                    bundle.putLong("id",id_categoria);
                }

                NavHostFragment.findNavController(ListadoProductoFragment.this)
                        .navigate(R.id.action_nav_productos_to_nav_agregar_productos,bundle);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}