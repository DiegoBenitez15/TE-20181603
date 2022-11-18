package com.example.practica3.Configuracion;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.practica3.Entidades.Producto;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Producto.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static String NAME = "pra3";
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public abstract ProductoDao productoDao();

    public static AppDatabase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, NAME)
                        .build();
            }
        }

        return INSTANCE;
    }
}
