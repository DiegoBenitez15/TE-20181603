package com.pf.proyectofinal.Repositorio;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pf.proyectofinal.Configuraciones.AppDatabase;
import com.pf.proyectofinal.Entidades.Compra;
import com.pf.proyectofinal.Entidades.CompraDao;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Entidades.ProductoCarro;
import com.pf.proyectofinal.Entidades.ProductoCarroDao;

import java.util.List;
import java.util.UUID;

public class CompraRepositorio {
    private static CompraDao compraDao;

    public CompraRepositorio(Application app){
        AppDatabase db = AppDatabase.getInstance(app);
        compraDao = db.compraDao();
    }

    public LiveData<List<Compra>> getCompra(){
        return compraDao.getCompra();
    }

    public static void insert(Compra c){
        AppDatabase.databaseWriteExecutor.execute(() ->
                compraDao.insert(c)
        );
    }
}
