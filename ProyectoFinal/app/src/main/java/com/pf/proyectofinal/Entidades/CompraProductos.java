package com.pf.proyectofinal.Entidades;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import java.util.List;

public class CompraProductos {
    @Embedded
    public Compra compra;
    @Relation(
            parentColumn = "id",
            entityColumn = "usuario_id"
    )
    public List<ProductoCarro> productoList;
}
