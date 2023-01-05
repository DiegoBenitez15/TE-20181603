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
public interface ProductoCarroDao {

    @Insert
    void insert(ProductoCarro p);

    @Delete
    void delete(ProductoCarro p);

    @Update
    void update(ProductoCarro p);

    @Query("SELECT * FROM productocarro WHERE usuario_id = :usuario and activo == 1")
    LiveData<List<ProductoCarro>> getByUser(String usuario);

    @Query("SELECT * FROM productocarro WHERE id = :id")
    LiveData<ProductoCarro> getById(long id);

    @Query("SELECT * FROM productocarro WHERE codigo_producto = :id")
    LiveData<List<ProductoCarro>> getByProduto(UUID id);
}
