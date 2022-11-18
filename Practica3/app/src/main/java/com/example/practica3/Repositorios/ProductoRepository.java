package com.example.practica3.Repositorios;

import android.app.Application;
import android.icu.lang.UCharacter;

import androidx.lifecycle.LiveData;

import com.example.practica3.Configuracion.AppDatabase;
import com.example.practica3.Configuracion.ProductoDao;
import com.example.practica3.Entidades.Producto;

import java.util.List;

public class ProductoRepository {
    private static ProductoDao productoDao;
    private LiveData<List<Producto>> lista_productos;

    public ProductoRepository(Application app){
        AppDatabase db = AppDatabase.getInstance(app);
        productoDao = db.productoDao();
        lista_productos = productoDao.getAll();
    }

    public static void insert(Producto p){
        AppDatabase.databaseWriteExecutor.execute(() ->
                productoDao.insert(p)
        );
    }

    public static void update(Producto p){
        AppDatabase.databaseWriteExecutor.execute(() ->
                productoDao.update(p)
        );
    }

    public static void eliminar(Producto p){
        AppDatabase.databaseWriteExecutor.execute(() ->
                productoDao.eliminar(p)
        );
    }

    public LiveData<Producto> getProducto(int id){
        return productoDao.getProducto(id);
    }

    public LiveData<List<Producto>> getAll(){
        return lista_productos;
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(() ->
                productoDao.deleteAll());
    }
}
