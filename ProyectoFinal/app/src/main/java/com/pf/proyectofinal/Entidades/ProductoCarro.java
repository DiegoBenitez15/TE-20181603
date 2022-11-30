package com.pf.proyectofinal.Entidades;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "productocarro",foreignKeys = {@ForeignKey(entity = Producto.class,
        parentColumns = "codigo",
        childColumns = "codigo_producto",
        onDelete = CASCADE),@ForeignKey(entity = Usuario.class,
        parentColumns = "usuario",
        childColumns = "usuario_id",
        onDelete = CASCADE)})
public class ProductoCarro {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "codigo_producto")
    private UUID codigo_producto;
    @ColumnInfo(name = "usuario_id")
    private String usuario_id;
    @ColumnInfo(name = "cantidad")
    private int cantidad;
    @ColumnInfo(name = "activo")
    private boolean activo;
    @ColumnInfo(name = "compra_id")
    private long compra_id;

    public ProductoCarro(UUID codigo_producto, String usuario_id, int cantidad,long compra_id) {
        this.codigo_producto = codigo_producto;
        this.usuario_id = usuario_id;
        this.cantidad = cantidad;
        this.compra_id = compra_id;
        activo = true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getCodigo_producto() {
        return codigo_producto;
    }

    public void setCodigo_producto(UUID codigo_producto) {
        this.codigo_producto = codigo_producto;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public long getCompra_id() {
        return compra_id;
    }

    public void setCompra_id(long compra_id) {
        this.compra_id = compra_id;
    }
}
