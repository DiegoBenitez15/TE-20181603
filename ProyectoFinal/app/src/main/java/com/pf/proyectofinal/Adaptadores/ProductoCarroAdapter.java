package com.pf.proyectofinal.Adaptadores;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Entidades.ProductoCarro;
import com.pf.proyectofinal.Modelos.ProductoCarroViewModel;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.Modelos.UsuarioViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;

import java.util.ArrayList;
import java.util.List;

public class ProductoCarroAdapter extends RecyclerView.Adapter<ProductoCarroAdapter.ItemViewHolder>{
    private List<ProductoCarro> list;
    private ProductoCarroViewModel productocarroViewModel;
    private ProductoViewModel productoViewModel;
    private UsuarioViewModel usuarioViewModel;
    private FirebaseServicios firebaseServicios = new FirebaseServicios();
    private Fragment fragment;

    public ProductoCarroAdapter(Fragment fragment, List<ProductoCarro> list, ProductoCarroViewModel productocarroViewModel, ProductoViewModel productoViewModel, UsuarioViewModel usuarioViewModel) {
        this.fragment = fragment;
        this.productocarroViewModel = productocarroViewModel;
        this.productoViewModel = productoViewModel;
        this.usuarioViewModel = usuarioViewModel;
        this.list = list;
    }

    public void setList(List<ProductoCarro> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductoCarroAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.producto_carro_layout , parent , false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoCarroAdapter.ItemViewHolder holder, int position) {

        ProductoCarro p = list.get(position);

        productocarroViewModel.getById(p.getId()).observe(fragment.getViewLifecycleOwner(),productoCarro -> {
            usuarioViewModel.getUser(null,p.getUsuario_id()).observe(fragment.getViewLifecycleOwner(), usuario -> {
                productoViewModel.getProducto(p.getCodigo_producto()).observe(fragment.getViewLifecycleOwner(),producto -> {
                    firebaseServicios.downloadImage(producto.getImg(),holder.img);
                    holder.desc_prod.setText(producto.getDescripcion());
                    holder.prec_prod.setText(String.valueOf(producto.getPrecio()));
                    holder.amount_prod.setText(String.valueOf(p.getCantidad()));
                });
            });
        });

        holder.subs_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(holder.amount_prod.getText().toString()) - 1;

                if(value > 0 ) {
                    String v = String.valueOf(value);
                    holder.amount_prod.setText(v);
                    p.setCantidad(value);
                    productocarroViewModel.update(p);
                }
            }
        });

        holder.sum_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = String.valueOf(Integer.parseInt(holder.amount_prod.getText().toString()) + 1);
                holder.amount_prod.setText(value);
                p.setCantidad(Integer.parseInt(value));
                productocarroViewModel.update(p);
            }
        });

        holder.delete_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p.setActivo(false);
                productocarroViewModel.update(p);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView desc_prod;
        TextView prec_prod;
        TextView amount_prod;
        Button subs_prod;
        Button sum_prod;
        Button delete_prod;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.foto_prod);
            desc_prod = itemView.findViewById(R.id.desc_prod);
            prec_prod = itemView.findViewById(R.id.prec_prod);
            amount_prod = itemView.findViewById(R.id.amount_prod);
            subs_prod = itemView.findViewById(R.id.subs_prod);
            sum_prod = itemView.findViewById(R.id.sum_prod);
            delete_prod = itemView.findViewById(R.id.delete_prod);
        }
    }
}
