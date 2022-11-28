package com.pf.proyectofinal.Adaptadores;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.pf.proyectofinal.Actividades.ProductoActivity;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Fragmentos.AgregarProductoFragment;
import com.pf.proyectofinal.Fragmentos.DescripcionProductoFragment;
import com.pf.proyectofinal.Fragmentos.EditarCategoriaFragment;
import com.pf.proyectofinal.Fragmentos.EditarProductoFragment;
import com.pf.proyectofinal.Fragmentos.ListadoProductoFragment;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;

import java.util.ArrayList;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ItemViewHolder>{
    private List<Producto> list;
    private ProductoViewModel productoViewModel;
    private FirebaseServicios firebaseServicios = new FirebaseServicios();
    private ListadoProductoFragment listadoProductoFragment;

    public ProductoAdapter(ProductoViewModel productoViewModel) {
        this.productoViewModel = productoViewModel;
        list = new ArrayList<Producto>();
    }

    public ProductoAdapter(ListadoProductoFragment listadoProductoFragment,List<Producto> list,ProductoViewModel productoViewModel) {
        this.listadoProductoFragment = listadoProductoFragment;
        this.productoViewModel = productoViewModel;
        this.list = list;
    }

    public ProductoAdapter(List<Producto> list, FirebaseServicios firebaseServicios) {
        this.list = list;
        this.firebaseServicios = firebaseServicios;
    }

    public void setList(List<Producto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductoAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.producto_layout , parent , false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter.ItemViewHolder holder, int position) {

        Producto producto = list.get(position);

        holder.codigo.setText(producto.getCodigo().toString());
        holder.precio.setText(String.valueOf(producto.getPrecio()));
        holder.descripcion.setText(producto.getDescripcion());
        firebaseServicios.downloadImage(producto.getImg(),holder.img);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id",producto.getCodigo().toString());
                FragmentManager fragmentManager = listadoProductoFragment.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_dashboard, DescripcionProductoFragment.class,bundle,null);
                fragmentTransaction.setReorderingAllowed(true).addToBackStack(null).commit();
            }
        });

        holder.opc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Seleccionar opcion");
                builder.setItems(R.array.opciones_producto, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            Bundle bundle = new Bundle();
                            bundle.putString("id",producto.getCodigo().toString());
                            FragmentManager fragmentManager = listadoProductoFragment.getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.nav_host_fragment_content_dashboard, EditarProductoFragment.class,bundle,null);
                            fragmentTransaction.setReorderingAllowed(true).addToBackStack(null).commit();
                        }else {
                            productoViewModel.delete(producto);
                        }
                    }
                });
                builder.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView codigo;
        TextView precio;
        TextView descripcion;
        ImageView opc;
        LinearLayout card;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.foto_item);
            codigo = itemView.findViewById(R.id.n_producto);
            precio = itemView.findViewById(R.id.p_producto);
            descripcion = itemView.findViewById(R.id.d_producto);
            opc = itemView.findViewById(R.id.info_producto);
            card = itemView.findViewById(R.id.producto);
        }
    }
}
