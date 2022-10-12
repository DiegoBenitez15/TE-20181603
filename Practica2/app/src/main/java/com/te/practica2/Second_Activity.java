package com.te.practica2;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class Second_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void storage(View view){
        String message;

        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            message = "Permission already granted";

            Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("OPEN", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("pdf/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivity(intent);
                }
            }).show();
        } else {
            message = "Permission not granted";
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }

    public void location(View view){
        String message;

        if (ContextCompat.checkSelfPermission(this,ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            message = "Permission already granted";

            Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("OPEN", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            }).show();
        } else {
            message = "Permission not granted";
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }

    public void camara(View view){
        String message;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            message = "Permission already granted";

            Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("OPEN", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(i);
                }
            }).show();
        } else {
            message = "Permission not granted";
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }

    public void phone(View view){
        String message;

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            message = "Permission already granted";

            Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("OPEN", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + "8298918451"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
                    startActivity(intent);
                }
            }).show();
        } else {
            message = "Permission not granted";
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }

    }

    public void contacts(View view){
        String message;

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            message = "Permission already granted";

            Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("OPEN", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                    startActivity(intent);
                }
            }).show();
        } else {
            message = "Permission not granted";
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }
}