package com.example.practica3.Configuracion;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.practica3.Entidades.Producto;

import java.util.List;

@Dao
public interface ProductoDao {
    @Query("SELECT * from producto WHERE estado=65")
    LiveData<List<Producto>> getAll();

    @Query("SELECT * FROM producto WHERE id=:id")
    LiveData<Producto> getProducto(int id);

    @Insert
    void insert(Producto p);

    @Update
    void update(Producto p);

    @Delete
    void eliminar(Producto p);

    @Query("DELETE FROM producto")
    void deleteAll();
}
