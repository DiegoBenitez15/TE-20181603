package com.pf.proyectofinal.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pf.proyectofinal.Adaptadores.NotificacionAdapter;
import com.pf.proyectofinal.Adaptadores.ProductoCarroAdapter;
import com.pf.proyectofinal.Entidades.Compra;
import com.pf.proyectofinal.Entidades.Notificacion;
import com.pf.proyectofinal.Entidades.ProductoCarro;
import com.pf.proyectofinal.Modelos.CompraProductosViewModal;
import com.pf.proyectofinal.Modelos.CompraViewModal;
import com.pf.proyectofinal.Modelos.NotificacionViewModel;
import com.pf.proyectofinal.Modelos.ProductoCarroViewModel;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.Modelos.UsuarioViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;
import com.pf.proyectofinal.databinding.FragmentCarroComprasBinding;
import com.pf.proyectofinal.databinding.FragmentNotificacionBinding;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CarroComprasFragment extends Fragment {
    private FragmentCarroComprasBinding binding;
    private ProductoCarroViewModel productoCarroViewModel;
    private CompraProductosViewModal compraProductosViewModal;
    private CompraViewModal compraViewModal;
    private ProductoViewModel productoViewModel;
    private NotificacionViewModel notificacionViewModel;
    private UsuarioViewModel usuarioViewModel;
    private FirebaseServicios firebaseServicios;
    private String usuario;
    private static final DecimalFormat df = new DecimalFormat("0.00");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productoCarroViewModel = new ViewModelProvider(this).get(ProductoCarroViewModel.class);
        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        compraProductosViewModal = new ViewModelProvider(this).get(CompraProductosViewModal.class);
        notificacionViewModel = new ViewModelProvider(this).get(NotificacionViewModel.class);
        compraViewModal = new ViewModelProvider(this).get(CompraViewModal.class);
        firebaseServicios = new FirebaseServicios();
        usuario = getArguments().getString("user");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = binding.listaCarro;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        productoCarroViewModel.getByUser(usuario).observe(getViewLifecycleOwner(),productoCarros -> {
            int sum = 0;
            binding.precioCarro.setText("0.00");
            for(ProductoCarro p:productoCarros){
                sum += p.getCantidad();
                productoViewModel.getProducto(p.getCodigo_producto()).observe(getViewLifecycleOwner(),producto -> {
                    String value = String.valueOf(df.format(Float.parseFloat(binding.precioCarro.getText().toString()) + (producto.getPrecio() * p.getCantidad())));
                    binding.precioCarro.setText(value);
                });
            }

            String total = "Sub Total (" + sum + " items): $ ";
            binding.total.setText(total);

            ProductoCarroAdapter adapter = new ProductoCarroAdapter(this, productoCarros, productoCarroViewModel,productoViewModel,usuarioViewModel);
            recyclerView.setAdapter(adapter);
        });

        binding.btnProcesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productoCarroViewModel.getByUser(usuario).observe(getViewLifecycleOwner(),productoCarros -> {
                    if(productoCarros.size() > 0){
                        Compra c = new Compra(usuario);
                        compraViewModal.insert(c);

                        for(ProductoCarro p:productoCarros){
                            p.setCompra_id(c.getId());
                            p.setActivo(false);
                            productoCarroViewModel.update(p);
                        }


                        Date date = Calendar.getInstance().getTime();
                        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
                        String strDate = dateFormat.format(date);
                        notificacionViewModel.insert(new Notificacion(usuario,"Se ha realizado correctamente la compra solicitada",strDate));
                    }
                });
        }});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCarroComprasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}