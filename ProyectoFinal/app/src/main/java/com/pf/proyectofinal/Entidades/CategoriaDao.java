package com.pf.proyectofinal.Entidades;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

@Dao
public interface CategoriaDao {
    @Insert
    void insert(Categoria categoria);

    @Delete
    void delete(Categoria categoria);

    @Query("SELECT * from categoria")
    LiveData<List<Categoria>> getAll();

    @Query("SELECT * FROM categoria WHERE id = :id")
    LiveData<Categoria> getCategoria(long id);

    @Query("SELECT * FROM categoria WHERE nombre = :nombre")
    LiveData<Categoria> getCategoriaByName(String nombre);

    @Update
    void update(Categoria categoria);
}
