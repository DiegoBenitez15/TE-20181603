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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pf.proyectofinal.Entidades.Notificacion;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Modelos.NotificacionViewModel;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;

import java.util.ArrayList;
import java.util.List;

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.ItemViewHolder>{
    private List<Notificacion> list;
    private NotificacionViewModel notificacionViewModel;
    private FirebaseServicios firebaseServicios = new FirebaseServicios();
    private Fragment fragment;



    public NotificacionAdapter(Fragment fragment, List<Notificacion> list, NotificacionViewModel notificacionViewModel) {
        this.fragment = fragment;
        this.notificacionViewModel = notificacionViewModel;
        this.list = list;
    }

    public void setList(List<Notificacion> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificacionAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificacion_layout , parent , false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionAdapter.ItemViewHolder holder, int position) {

        Notificacion notificacion = list.get(position);

        holder.fecha.setText(notificacion.getFecha());
        holder.mensaje.setText(notificacion.getMensaje());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificacionViewModel.delete(notificacion);
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView fecha;
        TextView mensaje;
        CardView card;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            fecha = itemView.findViewById(R.id.fecha_mensaje);
            mensaje = itemView.findViewById(R.id.mensaje);
            card = itemView.findViewById(R.id.notificacion_card);
        }
    }
}
