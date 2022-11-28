package com.pf.proyectofinal.Entidades;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuario")
public class Usuario {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="usuario")
    private String usuario;
    @ColumnInfo(name="nombre")
    private String nombre;
    @ColumnInfo(name="correo")
    private String correo;
    @ColumnInfo(name="contrasena")
    private String contrasena;
    @ColumnInfo(name="contacto")
    private String contacto;
    @ColumnInfo(name="url_img")
    private String url_img;

    public Usuario() {
    }

    public Usuario(@NonNull String usuario, String nombre, String correo, String contrasena, String contacto, String url_img) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.contacto = contacto;
        this.url_img = url_img;
    }

    @NonNull
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(@NonNull String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }
}

