package com.pf.proyectofinal.Adaptadores;

import android.app.Activity;
import android.app.AlertDialog;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pf.proyectofinal.Actividades.ProductoActivity;
import com.pf.proyectofinal.Entidades.CompraProductos;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Entidades.ProductoCarro;
import com.pf.proyectofinal.Fragmentos.AgregarProductoFragment;
import com.pf.proyectofinal.Fragmentos.DescripcionProductoFragment;
import com.pf.proyectofinal.Fragmentos.EditarCategoriaFragment;
import com.pf.proyectofinal.Fragmentos.EditarProductoFragment;
import com.pf.proyectofinal.Fragmentos.ListadoProductoFragment;
import com.pf.proyectofinal.Modelos.CompraProductosViewModal;
import com.pf.proyectofinal.Modelos.ProductoCarroViewModel;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;

import java.util.ArrayList;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ItemViewHolder>{
    private List<Producto> list;
    private ProductoViewModel productoViewModel;
    private ProductoCarroViewModel productoCarroViewModel;
    private FirebaseServicios firebaseServicios = new FirebaseServicios();
    private Fragment listadoProductoFragment;


    public ProductoAdapter(Fragment listadoProductoFragment,List<Producto> list,ProductoViewModel productoViewModel, ProductoCarroViewModel productoCarroViewModel) {
        this.listadoProductoFragment = listadoProductoFragment;
        this.productoViewModel = productoViewModel;
        this.list = list;
        this.productoCarroViewModel = productoCarroViewModel;
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

                NavHostFragment.findNavController( listadoProductoFragment)
                        .navigate(R.id.action_nav_productos_to_nav_descripcion_productos,bundle);
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
                            NavHostFragment.findNavController(listadoProductoFragment)
                                    .navigate(R.id.action_nav_productos_to_nav_editar_productos,bundle);
                        }else {
                            productoCarroViewModel.getByProduto(producto.getCodigo()).observe(listadoProductoFragment.getViewLifecycleOwner(),productoCarros -> {
                                int c = 0;

                                for(ProductoCarro p:productoCarros){
                                    if(p.getCompra_id() != -1){
                                        c = 1;
                                        break;
                                    }
                                }

                                if(c != 1){
                                    productoViewModel.delete(producto);
                                }else{
                                    Toast.makeText(listadoProductoFragment.getContext(), "Este producto ha sido comprado anteriormente", Toast.LENGTH_SHORT).show();
                                }
                            });
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
