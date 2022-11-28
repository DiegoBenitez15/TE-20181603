package com.pf.proyectofinal.Modelos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Entidades.Usuario;
import com.pf.proyectofinal.Repositorio.ProductoRepositorio;
import com.pf.proyectofinal.Repositorio.UsuarioRepositorio;

import java.util.List;
import java.util.UUID;

public class ProductoViewModel extends AndroidViewModel {
    private ProductoRepositorio productoRepositorio;
    private LiveData<List<Producto>> lista_productos;

    public ProductoViewModel(@NonNull Application app){
        super(app);
        productoRepositorio = new ProductoRepositorio(app);
        lista_productos = productoRepositorio.getAll();
    }

    public LiveData<List<Producto>> getAll() {
        return lista_productos;
    }

    public LiveData<Producto> getProducto(UUID codigo) {
        return productoRepositorio.getProducto(codigo);
    }

    public LiveData<List<Producto>> getProductoByCategoria(long categoria) {
        return productoRepositorio.getProductoByCategoria(categoria);
    }

    public void update(Producto p) {
        ProductoRepositorio.update(p);
    }

    public void insert(Producto producto) {
        ProductoRepositorio.insert(producto);
    }

    public void delete(Producto producto) {
        ProductoRepositorio.delete(producto);
    }
}
