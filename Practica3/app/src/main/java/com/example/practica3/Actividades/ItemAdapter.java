package com.example.practica3.Actividades;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica3.Entidades.Producto;
import com.example.practica3.R;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>
{
    private List<Producto> list;
    MainActivity main;

    public ItemAdapter(MainActivity main) {
        this.main =  main;
        list = new ArrayList<>();
    }

    public ItemAdapter(){
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent , false);
        return new ItemViewHolder(view);
    }

    public void setList(List<Producto> list) {
        this.list = list;
        notifyDataSetChanged();
    }



    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
       Producto producto = list.get(position);

       holder.nombre.setText(producto.getNombre());
       holder.marca.setText(producto.getMarca());
       holder.precio.setText(String.valueOf(producto.getPrecio()));

       holder.anadir_carrito.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(main,"Funcionalidad aun no disponible",Toast.LENGTH_LONG).show();
           }
       });

        holder.editar_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.iniciarActividad(producto.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView nombre;
        TextView marca;
        TextView precio;
        Button anadir_carrito;
        Button editar_producto;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre=itemView.findViewById(R.id.nombre_producto);
            marca=itemView.findViewById(R.id.marca_producto);
            precio=itemView.findViewById(R.id.precio_producto);
            anadir_carrito=itemView.findViewById(R.id.anadir_carrito);
            editar_producto=itemView.findViewById(R.id.editar_producto);
        }
    }
}
