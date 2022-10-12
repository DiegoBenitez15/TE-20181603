package com.te.practica2;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.lights.LightState;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private List<Switch> switches;
    private final int REQUEST_PERMISSION_CAMARA=0;
    HashMap<String,String> permisos = new HashMap<String,String>(){{put("Storage", READ_EXTERNAL_STORAGE);put("Location",Manifest.permission.ACCESS_COARSE_LOCATION);
        put("Camara",Manifest.permission.CAMERA);put("Phone",Manifest.permission.CALL_PHONE);put("Contacts",Manifest.permission.READ_CONTACTS);}};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switches = new ArrayList<Switch>();
        switches.add(findViewById(R.id.storage));
        switches.add(findViewById(R.id.location));
        switches.add(findViewById(R.id.camara));
        switches.add(findViewById(R.id.phone));
        switches.add(findViewById(R.id.contacts));
        this.actualizarChecks();
        this.activarListenerSwitch();
    }

    public void actualizarChecks(){
        for(Switch sw:switches){
            sw.setChecked(false);
        }

        this.habilitarChecks();
    }

    public void habilitarChecks(){
        for(Switch sw:switches){
            if (ContextCompat.checkSelfPermission(this, Objects.requireNonNull(permisos.get(String.valueOf(sw.getText())))) == PackageManager.PERMISSION_GRANTED) {
                sw.setChecked(true);
            }
        }
    }

    public void activarListenerSwitch(){
        for(Switch sw: switches){
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(sw.isChecked()) {
                        solicitudPermisos(String.valueOf(sw.getText()));
                    } else {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Objects.requireNonNull(permisos.get(String.valueOf(sw.getText())))) == PackageManager.PERMISSION_GRANTED) {
                            sw.setChecked(true);
                        }
                    }
                }
            });
        }
    }

   public void continuar(View view){


        Intent i = new Intent(this, Second_Activity.class);
        startActivity(i);
    }

    public void cancelar(View view){
        this.finishAndRemoveTask();
    }

    public void solicitudPermisos(String opc){

        switch(opc){
            case "Storage":
                this.habilitarPermiso(Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
            case "Location":
                this.habilitarPermiso(Manifest.permission.ACCESS_COARSE_LOCATION);
                break;
            case "Camara":
                this.habilitarPermiso(Manifest.permission.CAMERA);
                break;
            case "Phone":
                this.habilitarPermiso(Manifest.permission.CALL_PHONE);
                break;
            case "Contacts":
                this.habilitarPermiso(Manifest.permission.READ_CONTACTS);
                break;
            default:
                Toast.makeText(this,"Opcion no valida",Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void habilitarPermiso(String permiso){
        if (ContextCompat.checkSelfPermission(this,permiso) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permiso)) {

            } else {
                ActivityCompat.requestPermissions(this,new String[]{permiso},1);
            }
        }
    }
}