package com.pf.proyectofinal.Repositorio;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pf.proyectofinal.Configuraciones.AppDatabase;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Entidades.ProductoDao;
import com.pf.proyectofinal.Entidades.Usuario;
import com.pf.proyectofinal.Entidades.UsuarioDao;

import java.util.List;
import java.util.UUID;

public class ProductoRepositorio {
    private static ProductoDao productoDao;
    private LiveData<List<Producto>> lista_productos;

    public ProductoRepositorio(Application app){
        AppDatabase db = AppDatabase.getInstance(app);
        productoDao = db.productoDao();
        lista_productos = productoDao.getAll();
    }

    public LiveData<List<Producto>> getAll(){
        return lista_productos;
    }

    public LiveData<Producto> getProducto(UUID codigo){
        return productoDao.getProducto(codigo);
    }

    public LiveData<List<Producto>> getProductoByCategoria(long categoria){
        return productoDao.getProductoByCategoria(categoria);
    }

    public LiveData<List<Producto>> getProductoBusqueda(String palabra){
        return productoDao.getProductoBusqueda(palabra);
    }

    public static void insert(Producto p){
        AppDatabase.databaseWriteExecutor.execute(() ->
                productoDao.insert(p)
        );
    }

    public static void delete(Producto p){
        AppDatabase.databaseWriteExecutor.execute(() ->
                productoDao.delete(p)
        );
    }

    public static void update(Producto p){
        AppDatabase.databaseWriteExecutor.execute(() ->
                productoDao.update(p)
        );
    }
}
