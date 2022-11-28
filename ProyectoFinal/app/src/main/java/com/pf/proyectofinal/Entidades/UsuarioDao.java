package com.pf.proyectofinal.Entidades;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UsuarioDao {

    @Query("SELECT * from usuario")
    LiveData<List<Usuario>> getAll();

    @Query("SELECT * from usuario Where correo = :correo or usuario = :usuario")
    LiveData<Usuario> getUser(String correo,String usuario);

    @Query("SELECT * from usuario Where correo = :correo or usuario = :usuario")
    LiveData<List<Usuario>> getUsers(String correo,String usuario);

    @Insert
    void insert(Usuario u);
}
