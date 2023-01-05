package com.pf.proyectofinal.Entidades;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.UUID;

@Entity(tableName = "producto",foreignKeys = @ForeignKey(entity = Categoria.class,
        parentColumns = "id",
        childColumns = "categoria",
        onDelete = CASCADE))
public class Producto {
    @PrimaryKey
    @NonNull
    private UUID codigo;
    @ColumnInfo(name = "precio")
    private double precio;
    @ColumnInfo(name = "descripcion")
    private String descripcion;
    @ColumnInfo(name = "imagen")
    private String img;
    @ColumnInfo(name = "categoria")
    private long categoria;

    public Producto() {
        this.codigo = UUID.randomUUID();
    }

    public Producto(double precio, String descripcion, String img,long categoria) {
        this.codigo = UUID.randomUUID();
        this.precio = precio;
        this.descripcion = descripcion;
        this.img = img;
        this.categoria = categoria;
    }

    @NonNull
    public UUID getCodigo() {
        return codigo;
    }

    public void setCodigo(@NonNull UUID codigo) {
        this.codigo = codigo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getCategoria() {
        return categoria;
    }

    public void setCategoria(long categoria) {
        this.categoria = categoria;
    }
}
