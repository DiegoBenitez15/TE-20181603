package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText nombre;
    private EditText apellido;

    private Spinner genero;
    private EditText nacimiento;

    private RadioGroup pregunta;

    private TableLayout lenguaje;
    private DatePickerDialog.OnDateSetListener setListener;

    private String[] generolist = {"Seleccionar","Masculino","Femenino"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        nacimiento = findViewById(R.id.nacimiento);
        genero = findViewById(R.id.genero);
        pregunta = findViewById(R.id.preguntaradiogroup);
        lenguaje = findViewById(R.id.lenguaje);
        this.setCalendar();

        genero.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, generolist);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genero.setAdapter(ad);
    }

    public void enviar(View view){
        String nombre = String.valueOf(this.nombre.getText());
        String apellido = String.valueOf(this.apellido.getText());
        String nacimiento = String.valueOf(this.nacimiento.getText());
        String genero = String.valueOf(this.genero.getSelectedItem());
        RadioButton radioButton = (RadioButton) findViewById(this.pregunta.getCheckedRadioButtonId());
        String pregunta = String.valueOf(radioButton.getText());
        List<String> lenguaje = this.getLenguaje();
        if(TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || genero.equals("Seleccionar")){
            Toast.makeText(this,"Los campos nombre, apellido y genero son obligatorios",Toast.LENGTH_LONG).show();
        } else if (lenguaje.size() == 0 && pregunta.equals("Si")){
            Toast.makeText(this, "Debe seleccionar al menos 1 lenguaje", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Se ha registrado correctamente", Toast.LENGTH_LONG).show();
            String resulado = "Hola mi nombre es " + nombre + " " + apellido + ".\n\nSoy " + genero + ", naci en fecha " +
                    nacimiento + ".\n\n";

            if(pregunta.equals("Si")){
                resulado = resulado + "Me gusta programar. Mis lenguajes favoritos son " + lenguaje.toString().replace("[","").replace("]","") + ".";
            } else {
                resulado = resulado + "No me gusta programar";
            }

            Intent i = new Intent(getApplicationContext(),FirstActivity.class);
            i.putExtra("resultado",resulado);
            startActivity(i);
        }


    }

    public void limpiar(View view){
        this.nombre.setText("");
        this.apellido.setText("");
        this.nacimiento.setText("");
        this.genero.setSelection(0);
        this.limpiarLenguaje();
        this.pregunta.check(R.id.buttonSi);
        this.validarOpcion(view);
        Toast.makeText(this,"Se ha limpiado correctamente",Toast.LENGTH_LONG).show();
    }

    public void limpiarLenguaje(){
        for(int i = 0;i < lenguaje.getChildCount();i++){
            TableRow tb = (TableRow) lenguaje.getChildAt(i);
            for(int j = 0;j < tb.getChildCount();j++)
            {
                CheckBox cb = (CheckBox) tb.getChildAt(j);
                cb.setChecked(false);
            }
        }
    }

    public List<String> getLenguaje(){
        List<String> l = new ArrayList<String>();
        for(int i = 0;i < lenguaje.getChildCount();i++){
            TableRow tb = (TableRow) lenguaje.getChildAt(i);
            for(int j = 0;j < tb.getChildCount();j++)
            {
                CheckBox cb = (CheckBox) tb.getChildAt(j);
                if(cb.isChecked()){
                    l.add(String.valueOf(cb.getText()));
                }
            }
        }

        return l;
    }

    public void isActiveLenguaje(boolean bool){
        this.limpiarLenguaje();

        for(int i = 0;i < lenguaje.getChildCount();i++){
            TableRow tb = (TableRow) lenguaje.getChildAt(i);
            for(int j = 0;j < tb.getChildCount();j++)
            {
                CheckBox cb = (CheckBox) tb.getChildAt(j);
                cb.setEnabled(bool);
            }
        }
    }

    public void setCalendar(){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        setListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
                month = month+1;
                String date = day + "/" + month + "/" + year;
                nacimiento.setText(date);
            }
        };

        nacimiento.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        nacimiento.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
    }

    public void validarOpcion(View view){
        RadioButton si = findViewById(R.id.buttonSi);

        if(si.isChecked()) {
            this.isActiveLenguaje(true);
        }else{;
            this.isActiveLenguaje(false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}