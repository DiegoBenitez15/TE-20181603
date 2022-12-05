package com.pf.proyectofinal.Configuraciones;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.pf.proyectofinal.Entidades.Categoria;
import com.pf.proyectofinal.Entidades.CategoriaDao;
import com.pf.proyectofinal.Entidades.Compra;
import com.pf.proyectofinal.Entidades.CompraDao;
import com.pf.proyectofinal.Entidades.CompraProductos;
import com.pf.proyectofinal.Entidades.CompraProductosDao;
import com.pf.proyectofinal.Entidades.Notificacion;
import com.pf.proyectofinal.Entidades.NotificacionDao;
import com.pf.proyectofinal.Entidades.Producto;
import com.pf.proyectofinal.Entidades.ProductoCarro;
import com.pf.proyectofinal.Entidades.ProductoCarroDao;
import com.pf.proyectofinal.Entidades.ProductoDao;
import com.pf.proyectofinal.Entidades.Usuario;
import com.pf.proyectofinal.Entidades.UsuarioDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Usuario.class, Producto.class, Categoria.class, Notificacion.class, ProductoCarro.class, Compra.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static String NAME = "finalproject";
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, NAME)
                        .build();
            }
        }

        return INSTANCE;
    }


    public abstract UsuarioDao usuarioDao();
    public abstract ProductoDao productoDao();
    public abstract CategoriaDao categoriaDao();
    public abstract NotificacionDao notificacionDao();
    public abstract ProductoCarroDao productoCarroDao();
    public abstract CompraDao compraDao();
}
