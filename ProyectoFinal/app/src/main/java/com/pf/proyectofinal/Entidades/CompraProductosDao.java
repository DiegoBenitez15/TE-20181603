package com.pf.proyectofinal.Entidades;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;
import java.util.UUID;

@Dao
public interface CompraProductosDao {
    @Transaction
    @Query("SELECT * FROM compra,productocarro WHERE codigo_producto == :codigo")
    LiveData<List<CompraProductos>> getByProducto(UUID codigo);

}
