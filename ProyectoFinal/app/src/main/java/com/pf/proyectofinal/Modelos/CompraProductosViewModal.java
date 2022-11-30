package com.pf.proyectofinal.Modelos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pf.proyectofinal.Entidades.Compra;
import com.pf.proyectofinal.Entidades.CompraProductos;
import com.pf.proyectofinal.Entidades.CompraProductosDao;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Repositorio.CompraProductosRepositorio;
import com.pf.proyectofinal.Repositorio.CompraRepositorio;

import java.util.List;
import java.util.UUID;

public class CompraProductosViewModal extends AndroidViewModel {
    private CompraProductosRepositorio compraRepositorio;

    public CompraProductosViewModal(@NonNull Application app){
        super(app);
        compraRepositorio = new CompraProductosRepositorio(app);
    }

    public LiveData<List<CompraProductos>> getByProducto(UUID codigo) {
        return compraRepositorio.getByProducto(codigo);
    }


}
