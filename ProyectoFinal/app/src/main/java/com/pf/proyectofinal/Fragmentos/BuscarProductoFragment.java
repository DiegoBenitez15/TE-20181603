package com.pf.proyectofinal.Fragmentos;

import static android.view.KeyEvent.KEYCODE_SEARCH;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.pf.proyectofinal.Adaptadores.ProductoAdapter;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Modelos.CategoriaViewModel;
import com.pf.proyectofinal.Modelos.CompraProductosViewModal;
import com.pf.proyectofinal.Modelos.ProductoCarroViewModel;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;
import com.pf.proyectofinal.databinding.FragmentBuscarProductoBinding;
import com.pf.proyectofinal.databinding.FragmentListadoProductoBinding;

import java.util.ArrayList;
import java.util.List;

public class BuscarProductoFragment extends Fragment {
    private FragmentBuscarProductoBinding binding;
    private ProductoViewModel productoViewModel;
    private CategoriaViewModel categoriaViewModel;
    private FirebaseServicios firebaseServicios;
    private ProductoCarroViewModel productoCarroViewModel;



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentBuscarProductoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        productoCarroViewModel = new ViewModelProvider(this).get(ProductoCarroViewModel.class);
        categoriaViewModel = new ViewModelProvider(this).get(CategoriaViewModel.class);
        firebaseServicios = new FirebaseServicios();
        RecyclerView recyclerView = binding.buscarLista;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));




        binding.buscarInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                categoriaViewModel.getCategoriaBusqueda(s).observe(getViewLifecycleOwner(),categorias -> {
                    productoViewModel.getProductoBusqueda(s).observe(getViewLifecycleOwner(), productos -> {
                        ProductoAdapter adapter = new ProductoAdapter(getParentFragment(), productos, productoViewModel,productoCarroViewModel,1);
                        recyclerView.setAdapter(adapter);
                    });
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}