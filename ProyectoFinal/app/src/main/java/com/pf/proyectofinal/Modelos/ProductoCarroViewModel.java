package com.pf.proyectofinal.Modelos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Entidades.ProductoCarro;
import com.pf.proyectofinal.Repositorio.ProductoCarroRepositorio;
import com.pf.proyectofinal.Repositorio.ProductoRepositorio;

import java.util.List;
import java.util.UUID;

public class ProductoCarroViewModel extends AndroidViewModel {
    private ProductoCarroRepositorio productoRepositorio;

    public ProductoCarroViewModel(@NonNull Application app){
        super(app);
        productoRepositorio = new ProductoCarroRepositorio(app);
    }

    public LiveData<List<ProductoCarro>> getByUser(String usuario) {
        return productoRepositorio.getByUser(usuario);
    }

    public LiveData<ProductoCarro> getById(long id) {
        return productoRepositorio.getById(id);
    }

    public LiveData<List<ProductoCarro>> getByProduto(UUID id) {
        return productoRepositorio.getByProduto(id);
    }

    public void update(ProductoCarro p) {
        ProductoCarroRepositorio.update(p);
    }

    public void insert(ProductoCarro producto) {
        ProductoCarroRepositorio.insert(producto);
    }

    public void delete(ProductoCarro producto) {
        ProductoCarroRepositorio.delete(producto);
    }
}
