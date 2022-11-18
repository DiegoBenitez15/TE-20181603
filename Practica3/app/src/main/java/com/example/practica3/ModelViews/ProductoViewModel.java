package com.example.practica3.ModelViews;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.practica3.Entidades.Producto;
import com.example.practica3.Repositorios.ProductoRepository;

import java.util.List;

public class ProductoViewModel extends AndroidViewModel {
    private ProductoRepository r_producto;
    private LiveData<List<Producto>> lista_productos;

    public ProductoViewModel(@NonNull Application app){
        super(app);
        r_producto = new ProductoRepository(app);
        lista_productos = r_producto.getAll();
    }

    public void insert(Producto p) {
        ProductoRepository.insert(p);
    }

    public void update(Producto p) {
        ProductoRepository.update(p);
    }

    public void eliminar(Producto p) {
        ProductoRepository.eliminar(p);
    }

    public void deleteAll() {
        r_producto.deleteAll();
    }

    public LiveData<Producto> getProducto(int id){
        return r_producto.getProducto(id);
    }

    public LiveData<List<Producto>> getAll() {
        return lista_productos;
    }

}
