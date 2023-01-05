package com.pf.proyectofinal.Entidades;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(tableName = "compra",foreignKeys = @ForeignKey(entity = Usuario.class,
        parentColumns = "usuario",
        childColumns = "usuario_id",
        onDelete = CASCADE))
public class Compra {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "usuario_id")
    private String usuario_id;

    public Compra(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }
}
