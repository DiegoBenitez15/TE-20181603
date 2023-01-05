package com.pf.proyectofinal.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pf.proyectofinal.Actividades.CategoriaActivity;
import com.pf.proyectofinal.Actividades.ProductoActivity;
import com.pf.proyectofinal.Adaptadores.CategoriaAdapter;
import com.pf.proyectofinal.Adaptadores.ProductoAdapter;
import com.pf.proyectofinal.Entidades.Categoria;
import com.pf.proyectofinal.Modelos.CategoriaViewModel;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;
import com.pf.proyectofinal.databinding.FragmentListadoCategoriaBinding;
import com.pf.proyectofinal.databinding.FragmentListadoProductoBinding;

public class ListadoCategoriaFragment extends Fragment {
    private FragmentListadoCategoriaBinding binding;
    private CategoriaViewModel categoriaViewModel;
    private ProductoViewModel productoViewModel;
    private FirebaseServicios firebaseServicios;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentListadoCategoriaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        categoriaViewModel = new ViewModelProvider(this).get(CategoriaViewModel.class);
        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        firebaseServicios = new FirebaseServicios();

        RecyclerView recyclerView = binding.lista;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        categoriaViewModel.getAll().observe(getViewLifecycleOwner(), categorias -> {
            CategoriaAdapter adapter =  new CategoriaAdapter(this,categorias,categoriaViewModel,productoViewModel);
            recyclerView.setAdapter(adapter);
        });

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                NavHostFragment.findNavController(ListadoCategoriaFragment.this)
                        .navigate(R.id.action_nav_categorias_to_nav_crear_categorias);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}