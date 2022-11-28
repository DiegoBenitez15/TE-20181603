package com.pf.proyectofinal.Repositorio;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pf.proyectofinal.Configuraciones.AppDatabase;
import com.pf.proyectofinal.Entidades.Categoria;
import com.pf.proyectofinal.Entidades.CategoriaDao;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Entidades.ProductoDao;

import java.util.List;
import java.util.UUID;

public class CategoriaRepositorio {
    private static CategoriaDao categoriaDao;
    private LiveData<List<Categoria>> lista_categoria;

    public CategoriaRepositorio(Application app){
        AppDatabase db = AppDatabase.getInstance(app);
        categoriaDao = db.categoriaDao();
        lista_categoria = categoriaDao.getAll();
    }

    public LiveData<List<Categoria>> getAll(){
        return lista_categoria;
    }

    public LiveData<Categoria> getCategoria(long id){
        return categoriaDao.getCategoria(id);
    }

    public LiveData<Categoria> getCategoriaByName(String nombre){
        return categoriaDao.getCategoriaByName(nombre);
    }

    public static void insert(Categoria c){
        AppDatabase.databaseWriteExecutor.execute(() ->
                categoriaDao.insert(c)
        );
    }

    public static void delete(Categoria c){
        AppDatabase.databaseWriteExecutor.execute(() ->
                categoriaDao.delete(c)
        );
    }

    public static void update(Categoria c){
        AppDatabase.databaseWriteExecutor.execute(() ->
                categoriaDao.update(c)
        );
    }
}
