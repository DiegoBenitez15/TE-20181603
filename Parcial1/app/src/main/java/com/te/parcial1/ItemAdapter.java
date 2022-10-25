package com.te.parcial1;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>
{
    private final List<Producto> list;
    private final MainActivity main;

    public ItemAdapter(List<Producto> list,MainActivity main) {
        this.list = list;
        this.main =main;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent , false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

       Producto producto = list.get(position);

       holder.articulo.setText(producto.getArticulo());
       holder.descripcion.setText(producto.getDescripcion());
       holder.precio.setText(String.valueOf(producto.getPrecio()));

       holder.eliminar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               list.remove(producto);
               main.setProductos(list);
               main.recyclerView.setAdapter(new ItemAdapter(list,main));
           }
       });

        holder.compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "Nombre: " + producto.getArticulo() + " | " + "Descripcion: " + producto.getDescripcion() + " | " + "Precio" + producto.getPrecio() );

                main.iniciarActividad(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView articulo;
        TextView descripcion;
        TextView precio;
        Button eliminar;
        Button compartir;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            articulo = itemView.findViewById(R.id.articulo2);
            descripcion = itemView.findViewById(R.id.descripcion2);
            precio = itemView.findViewById(R.id.precio2);
            eliminar = itemView.findViewById(R.id.eliminar);
            compartir = itemView.findViewById(R.id.compartir);
        }
    }
    }
