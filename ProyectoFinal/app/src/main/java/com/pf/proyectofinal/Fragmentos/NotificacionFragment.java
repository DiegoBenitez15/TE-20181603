package com.pf.proyectofinal.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pf.proyectofinal.Adaptadores.NotificacionAdapter;
import com.pf.proyectofinal.Adaptadores.ProductoAdapter;
import com.pf.proyectofinal.Modelos.NotificacionViewModel;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;
import com.pf.proyectofinal.databinding.FragmentListadoProductoBinding;
import com.pf.proyectofinal.databinding.FragmentNotificacionBinding;


public class NotificacionFragment extends Fragment {
    private FragmentNotificacionBinding binding;
    private NotificacionViewModel notificacionViewModel;
    private FirebaseServicios firebaseServicios;
    private String usuario;

    public NotificacionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificacionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = binding.listadoNot;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notificacionViewModel.getNotificacion(usuario).observe(getViewLifecycleOwner(),notificacions -> {
            NotificacionAdapter adapter = new NotificacionAdapter(this, notificacions, notificacionViewModel);
            recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificacionViewModel = new ViewModelProvider(this).get(NotificacionViewModel.class);
        firebaseServicios = new FirebaseServicios();
        usuario = getArguments().getString("user");
    }

}