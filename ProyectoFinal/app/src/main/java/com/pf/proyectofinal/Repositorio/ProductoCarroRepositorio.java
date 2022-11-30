package com.pf.proyectofinal.Repositorio;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pf.proyectofinal.Configuraciones.AppDatabase;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Entidades.ProductoCarro;
import com.pf.proyectofinal.Entidades.ProductoCarroDao;
import com.pf.proyectofinal.Entidades.ProductoDao;

import java.util.List;
import java.util.UUID;

public class ProductoCarroRepositorio {
    private static ProductoCarroDao productoCarroDao;

    public ProductoCarroRepositorio(Application app){
        AppDatabase db = AppDatabase.getInstance(app);
        productoCarroDao = db.productoCarroDao();
    }

    public LiveData<List<ProductoCarro>> getByUser(String usuario){
        return productoCarroDao.getByUser(usuario);
    }

    public LiveData<ProductoCarro> getById(long id){
        return productoCarroDao.getById(id);
    }

    public LiveData<List<ProductoCarro>> getByProduto(UUID id){
        return productoCarroDao.getByProduto(id);
    }

    public static void insert(ProductoCarro p){
        AppDatabase.databaseWriteExecutor.execute(() ->
                productoCarroDao.insert(p)
        );
    }

    public static void delete(ProductoCarro p){
        AppDatabase.databaseWriteExecutor.execute(() ->
                productoCarroDao.delete(p)
        );
    }

    public static void update(ProductoCarro p){
        AppDatabase.databaseWriteExecutor.execute(() ->
                productoCarroDao.update(p)
        );
    }
}
