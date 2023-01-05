package com.pf.proyectofinal.Modelos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pf.proyectofinal.Entidades.Notificacion;
import com.pf.proyectofinal.Entidades.NotificacionDao;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Repositorio.NotificacionRepositorio;
import com.pf.proyectofinal.Repositorio.ProductoRepositorio;

import java.util.List;
import java.util.UUID;

public class NotificacionViewModel extends AndroidViewModel {
    private final NotificacionRepositorio notificacionRepositorio;

    public NotificacionViewModel(@NonNull Application app){
        super(app);
        notificacionRepositorio = new NotificacionRepositorio(app);
    }

    public LiveData<List<Notificacion>> getNotificacion(String usuario) {
        return NotificacionRepositorio.getNotificacion(usuario);
    }

    public void insert(Notificacion n) {
        NotificacionRepositorio.insert(n);
    }

    public void delete(Notificacion n) {
        NotificacionRepositorio.delete(n);
    }
}
