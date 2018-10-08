package com.example.franklinsierra.practica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private final String SALUDO = "hola desde primer activity ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //buscamos la vista por id
        btn = findViewById(R.id.buttonMain);// R es una clase que se usa para referencias
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this,"me has clickeado",Toast.LENGTH_LONG).show();

                //acceder a un second activity
                //CREACION
                //intent pasa de un activity a otro
                Intent intent = new Intent(MainActivity.this, secondActivity.class);//pide desde cual activity estamos hasta cual queremos ir
                //AÑADIMOS DATOS
                intent.putExtra("saludo", SALUDO);//primer parametro es un id, segundo contiene el valor
                //LANZAMOS
                startActivity(intent);
            }
        });

    }
}
