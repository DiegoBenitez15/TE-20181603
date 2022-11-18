package com.example.practica3.Entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.storage.StorageReference;

@Entity
public class Producto {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private String marca;
    private double precio;
    private char estado;
    private String image;

    public Producto(String nombre, String marca, double precio,String image) {
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.estado = 'A';
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }
}
