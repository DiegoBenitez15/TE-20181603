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
public interface ProductoDao {

    @Insert
    void insert(Producto producto);

    @Delete
    void delete(Producto producto);

    @Query("SELECT * from producto")
    LiveData<List<Producto>> getAll();

    @Query("SELECT * FROM producto WHERE codigo = :codigo")
    LiveData<Producto> getProducto(UUID codigo);

    @Query("SELECT * FROM producto WHERE categoria = :categoria")
    LiveData<List<Producto>> getProductoByCategoria(long categoria);

    @Update
    void update(Producto p);

}
