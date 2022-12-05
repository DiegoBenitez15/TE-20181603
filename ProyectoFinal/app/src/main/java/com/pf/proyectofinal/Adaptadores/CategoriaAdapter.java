package com.pf.proyectofinal.Adaptadores;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pf.proyectofinal.Actividades.CategoriaActivity;
import com.pf.proyectofinal.Actividades.ProductoActivity;
import com.pf.proyectofinal.Entidades.Categoria;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Fragmentos.AgregarCategoriaFragment;
import com.pf.proyectofinal.Fragmentos.DescripcionProductoFragment;
import com.pf.proyectofinal.Fragmentos.EditarCategoriaFragment;
import com.pf.proyectofinal.Fragmentos.EditarProductoFragment;
import com.pf.proyectofinal.Fragmentos.ListadoCategoriaFragment;
import com.pf.proyectofinal.Fragmentos.ListadoProductoFragment;
import com.pf.proyectofinal.Modelos.CategoriaViewModel;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;

import java.util.ArrayList;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ItemViewHolder>{
    private List<Categoria> list;
    private CategoriaViewModel categoriaViewModel;
    private ProductoViewModel productoViewModel;
    private FirebaseServicios firebaseServicios = new FirebaseServicios();
    private ListadoCategoriaFragment listadoCategoriaFragment;

    public CategoriaAdapter(CategoriaViewModel categoriaViewModel) {
        this.categoriaViewModel = categoriaViewModel;
        list = new ArrayList<Categoria>();
    }

    public CategoriaAdapter(ListadoCategoriaFragment listadoCategoriaFragment,List<Categoria> list, CategoriaViewModel categoriaViewModel,ProductoViewModel productoViewModel) {
        this.categoriaViewModel = categoriaViewModel;
        this.list = list;
        this.listadoCategoriaFragment = listadoCategoriaFragment;
        this.productoViewModel = productoViewModel;
    }

    public CategoriaAdapter(List<Categoria> list, FirebaseServicios firebaseServicios) {
        this.list = list;
        this.firebaseServicios = firebaseServicios;
    }

    public void setList(List<Categoria> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoriaAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoria_layout , parent , false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaAdapter.ItemViewHolder holder, int position) {

        Categoria categoria = list.get(position);

        holder.nombre.setText(categoria.getNombre());
        firebaseServicios.downloadImage(categoria.getImagen(),holder.img);

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
                            bundle.putString("id",String.valueOf(categoria.getId()));
                            NavHostFragment.findNavController(listadoCategoriaFragment)
                                    .navigate(R.id.action_nav_categorias_to_nav_editar_categorias,bundle);
                        }else {

                            productoViewModel.getProductoByCategoria(categoria.getId()).observe(listadoCategoriaFragment.getViewLifecycleOwner(),productos -> {
                                if(productos.size() == 0){
                                    categoriaViewModel.delete(categoria);
                                } else{
                                    Toast.makeText(listadoCategoriaFragment.getContext(), "Se encuentran productos relacionado a esta categoria", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                });
                builder.show();
            }
        });

        holder.card_categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putLong("id",categoria.getId());
                NavHostFragment.findNavController(listadoCategoriaFragment)
                        .navigate(R.id.action_nav_categorias_to_nav_productos,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        ImageView opc;
        TextView nombre;
        CardView card_categoria;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_categoria);
            nombre = itemView.findViewById(R.id.name_categoria);
            card_categoria = itemView.findViewById(R.id.card_categoria);
            opc = itemView.findViewById(R.id.opc_categorias);
        }
    }
}
