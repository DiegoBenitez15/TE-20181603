package com.pf.proyectofinal.Modelos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pf.proyectofinal.Entidades.Compra;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Entidades.ProductoCarro;
import com.pf.proyectofinal.Repositorio.CompraRepositorio;
import com.pf.proyectofinal.Repositorio.ProductoCarroRepositorio;

import java.util.List;
import java.util.UUID;

public class CompraViewModal extends AndroidViewModel {

    private CompraRepositorio compraRepositorio;

    public CompraViewModal(@NonNull Application app){
        super(app);
        compraRepositorio = new CompraRepositorio(app);
    }

    public LiveData<List<Compra>> getCompra() {
        return compraRepositorio.getCompra();
    }

    public void insert(Compra compra) {
        CompraRepositorio.insert(compra);
    }
}
