package com.example.franklinsierra.practica;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class secondActivity extends AppCompatActivity {

    TextView textView;
    Button buttonCompartir;
    //BOTON PARA CONECTAR CON EL SEGUNDO ACTIVITY
    private final String COMPARTIR = "vas a compartir";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //se busca por id
        textView = findViewById(R.id.textViewMain);

        //se toman los datos del intent el bundle es como una caja
        Bundle bundle = getIntent().getExtras();
        //puede que lo que obtenga este vacio
        if (bundle != null && bundle.getString("saludo") != null) {
            String saludo = bundle.getString("saludo");//busca el contenido que tiene key saludo
            Toast.makeText(secondActivity.this, saludo, Toast.LENGTH_SHORT).show();
            textView.setText(saludo);
        } else {
            Toast.makeText(secondActivity.this, "esta vacio", Toast.LENGTH_SHORT).show();
        }

        //buscamos por id
        buttonCompartir=findViewById(R.id.buttonCompartir);
        //para pasar al thirdActivity
        buttonCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CREA EL INTENT
                Intent intent = new Intent(secondActivity.this, thirdActivity.class);
                //SE CARGAN LOS DATOS
                intent.putExtra("compartir", COMPARTIR);
                //SE LANZA
                startActivity(intent);
            }
        });
    }

}
