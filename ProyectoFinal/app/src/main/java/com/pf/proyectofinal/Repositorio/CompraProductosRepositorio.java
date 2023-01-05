package com.pf.proyectofinal.Repositorio;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pf.proyectofinal.Configuraciones.AppDatabase;
import com.pf.proyectofinal.Entidades.Compra;
import com.pf.proyectofinal.Entidades.CompraDao;
import com.pf.proyectofinal.Entidades.CompraProductos;
import com.pf.proyectofinal.Entidades.CompraProductosDao;
import com.pf.proyectofinal.Entidades.Producto;

import java.util.List;
import java.util.UUID;

public class CompraProductosRepositorio {
    private static CompraProductosDao compraProductosDao;

    public CompraProductosRepositorio(Application app){
        AppDatabase db = AppDatabase.getInstance(app);
    }

    public LiveData<List<CompraProductos>> getByProducto(UUID codigo){
        return compraProductosDao.getByProducto(codigo);
    }

}
