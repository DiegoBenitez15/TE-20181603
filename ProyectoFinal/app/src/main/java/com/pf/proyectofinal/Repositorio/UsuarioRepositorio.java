package com.pf.proyectofinal.Repositorio;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pf.proyectofinal.Configuraciones.AppDatabase;
import com.pf.proyectofinal.Entidades.Usuario;
import com.pf.proyectofinal.Entidades.UsuarioDao;

import java.util.List;

public class UsuarioRepositorio {
    private static UsuarioDao usuarioDao;
    private LiveData<List<Usuario>> lista_usuarios;

    public UsuarioRepositorio(Application app){
        AppDatabase db = AppDatabase.getInstance(app);
        usuarioDao = db.usuarioDao();
        lista_usuarios = usuarioDao.getAll();
    }

    public LiveData<List<Usuario>> getAll(){
        return lista_usuarios;
    }

    public static void insert(Usuario u){
        AppDatabase.databaseWriteExecutor.execute(() ->
                usuarioDao.insert(u)
        );
    }

    public static void update(Usuario u){
        AppDatabase.databaseWriteExecutor.execute(() ->
                usuarioDao.update(u)
        );
    }

    public LiveData<Usuario> getUser(String correo,String usuario){
        return usuarioDao.getUser(correo,usuario);
    }

    public LiveData<List<Usuario>> getUsers(String correo,String usuario){
        return usuarioDao.getUsers(correo,usuario);
    }
}
