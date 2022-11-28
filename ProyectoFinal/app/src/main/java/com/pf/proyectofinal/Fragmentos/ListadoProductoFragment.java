package com.pf.proyectofinal.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pf.proyectofinal.Actividades.ProductoActivity;
import com.pf.proyectofinal.Adaptadores.ProductoAdapter;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;
import com.pf.proyectofinal.databinding.FragmentListadoProductoBinding;

public class ListadoProductoFragment extends Fragment {
    private FragmentListadoProductoBinding binding;
    private ProductoViewModel productoViewModel;
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
        firebaseServicios = new FirebaseServicios();
        RecyclerView recyclerView = binding.listado;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        try{
            id_categoria = getArguments().getLong("id");
            productoViewModel.getProductoByCategoria(id_categoria).observe(getViewLifecycleOwner(), productos -> {
                ProductoAdapter adapter = new ProductoAdapter(this, productos, productoViewModel);
                recyclerView.setAdapter(adapter);
            });
        }catch (NullPointerException e){
            binding.floatingActionButton3.setVisibility(View.INVISIBLE);
            productoViewModel.getAll().observe(getViewLifecycleOwner(), productos -> {
                ProductoAdapter adapter = new ProductoAdapter(this, productos, productoViewModel);
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

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_dashboard, AgregarProductoFragment.class,bundle,null);
                fragmentTransaction.setReorderingAllowed(true).addToBackStack(null).commit();
            }
        });

        binding.floatingActionButton3.setOnClickListener(new View.OnClickListener() {
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