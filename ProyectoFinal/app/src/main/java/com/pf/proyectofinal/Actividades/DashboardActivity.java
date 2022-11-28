package com.pf.proyectofinal.Actividades;

import android.net.IpSecManager;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.pf.proyectofinal.Fragmentos.AgregarCategoriaFragment;
import com.pf.proyectofinal.Modelos.UsuarioViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;
import com.pf.proyectofinal.databinding.ActivityDashboard2Binding;

public class DashboardActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDashboard2Binding binding;
    private UsuarioViewModel usuarioViewModel;
    private FirebaseServicios firebaseServicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usuarioViewModel =new ViewModelProvider(this).get(UsuarioViewModel.class);
        firebaseServicios = new FirebaseServicios();
        binding = ActivityDashboard2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDashboard.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_categorias, R.id.nav_productos)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dashboard);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        usuarioViewModel.getUser(null,getIntent().getExtras().getString("user")).observe(this,usuario -> {
            firebaseServicios.downloadImage(usuario.getUrl_img(),findViewById(R.id.imageView));
            ((TextView) findViewById(R.id.nombre)).setText(usuario.getNombre());
            ((TextView) findViewById(R.id.textView)).setText(usuario.getCorreo());
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dashboard);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}