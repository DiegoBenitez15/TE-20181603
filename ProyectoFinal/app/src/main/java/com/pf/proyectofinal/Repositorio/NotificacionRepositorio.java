package com.pf.proyectofinal.Repositorio;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pf.proyectofinal.Configuraciones.AppDatabase;
import com.pf.proyectofinal.Entidades.Notificacion;
import com.pf.proyectofinal.Entidades.NotificacionDao;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Entidades.ProductoDao;
import com.pf.proyectofinal.Entidades.Usuario;

import java.util.List;
import java.util.UUID;

public class NotificacionRepositorio {
    private static NotificacionDao notificacionDao;

    public NotificacionRepositorio(Application app){
        AppDatabase db = AppDatabase.getInstance(app);
        notificacionDao = db.notificacionDao();
    }

    public static LiveData<List<Notificacion>> getNotificacion(String usuario){
        return notificacionDao.getNotificacion(usuario);
    }

    public static void insert(Notificacion n){
        AppDatabase.databaseWriteExecutor.execute(() ->
                notificacionDao.insert(n)
        );
    }

    public static void delete(Notificacion n){
        AppDatabase.databaseWriteExecutor.execute(() ->
                notificacionDao.delete(n)
        );
    }

}
