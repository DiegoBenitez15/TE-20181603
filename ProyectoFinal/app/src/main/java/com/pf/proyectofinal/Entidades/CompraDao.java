package com.pf.proyectofinal.Entidades;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import java.util.UUID;

@Dao
public interface CompraDao {

    @Insert
    void insert(Compra compra);

    @Query("SELECT * FROM compra ")
    LiveData<List<Compra>> getCompra();
}
