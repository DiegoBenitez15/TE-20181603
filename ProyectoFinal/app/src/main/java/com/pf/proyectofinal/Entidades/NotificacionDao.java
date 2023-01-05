package com.pf.proyectofinal.Entidades;

import static android.icu.text.MessagePattern.ArgType.SELECT;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotificacionDao {

    @Query("SELECT * FROM notificacion WHERE usuario_id LIKE :usuario")
    LiveData<List<Notificacion>> getNotificacion(String usuario);

    @Insert
    void insert(Notificacion notificacion);

    @Delete
    void delete(Notificacion notificacion);
}
