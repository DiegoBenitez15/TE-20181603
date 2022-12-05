package com.pf.proyectofinal.Modelos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pf.proyectofinal.Entidades.Categoria;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Repositorio.CategoriaRepositorio;
import com.pf.proyectofinal.Repositorio.ProductoRepositorio;

import java.util.List;
import java.util.UUID;

public class CategoriaViewModel extends AndroidViewModel {
    private CategoriaRepositorio categoriaRepositorio;
    private LiveData<List<Categoria>> lista_categoria;

    public CategoriaViewModel(@NonNull Application app){
        super(app);
        categoriaRepositorio = new CategoriaRepositorio(app);
        lista_categoria = categoriaRepositorio.getAll();
    }

    public LiveData<List<Categoria>> getAll() {
        return lista_categoria;
    }

    public LiveData<List<Categoria>> getCategoriaBusqueda(String palabra) {
        return categoriaRepositorio.getCategoriaBusqueda(palabra);
    }


    public LiveData<Categoria> getCategoria(long id) {
        return categoriaRepositorio.getCategoria(id);
    }

    public LiveData<Categoria> getCategoriaByName(String nombre) {
        return categoriaRepositorio.getCategoriaByName(nombre);
    }

    public void update(Categoria c) {
        CategoriaRepositorio.update(c);
    }

    public void insert(Categoria c) {
        CategoriaRepositorio.insert(c);
    }

    public void delete(Categoria c) {
        CategoriaRepositorio.delete(c);
    }
}
