package com.pf.proyectofinal.Modelos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pf.proyectofinal.Entidades.Usuario;
import com.pf.proyectofinal.Repositorio.UsuarioRepositorio;

import java.util.List;

public class UsuarioViewModel extends AndroidViewModel {
    private UsuarioRepositorio r_usuario;
    private LiveData<List<Usuario>> lista_usuarios;

    public UsuarioViewModel(@NonNull Application app){
        super(app);
        r_usuario = new UsuarioRepositorio(app);
        lista_usuarios = r_usuario.getAll();
    }

    public LiveData<List<Usuario>> getAll() {
        return lista_usuarios;
    }

    public void insert(Usuario u) {
        UsuarioRepositorio.insert(u);
    }

    public LiveData<Usuario> getUser(String correo,String usuario){
        return r_usuario.getUser(correo,usuario);
    }

    public LiveData<List<Usuario>> getUsers(String correo,String usuario){
        return r_usuario.getUsers(correo,usuario);
    }
}
