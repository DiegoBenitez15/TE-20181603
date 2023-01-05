package com.pf.proyectofinal.Entidades;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.NavigableMap;

@Entity(tableName = "notificacion",foreignKeys = @ForeignKey(entity = Usuario.class,
        parentColumns = "usuario",
        childColumns = "usuario_id",
        onDelete = CASCADE))
public class Notificacion {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "usuario_id")
    private String usuario_id;
    @ColumnInfo(name = "mensaje")
    private String mensaje;
    @ColumnInfo(name = "fecha")
    private String fecha;

    public Notificacion() {
    }

    public Notificacion(String usuario_id, String mensaje,String fecha) {
        this.usuario_id = usuario_id;
        this.mensaje = mensaje;
        this.fecha = fecha;
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

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
