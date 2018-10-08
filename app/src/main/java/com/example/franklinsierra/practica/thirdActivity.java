package com.example.franklinsierra.practica;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;

public class thirdActivity extends AppCompatActivity {

    //creamos cada uno de los elementos que tenemos en el xml
    private EditText editTextPhone;
    private EditText editTextWeb;
    private ImageButton imageButtonPhone;
    private ImageButton imageButtonWeb;
    private ImageButton imageButtonCamera;
    //CODIGO PARA LLAMAR
    private final int codigoLlamar = 100;

    //SE CREA LA VARIABLE PARA CAPTURAR EL CONTENIDO DEL 2 ACTIVITY
    TextView textViewCompartir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        //los instanciamos
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextWeb = (EditText) findViewById(R.id.editTextWeb);
        imageButtonPhone = (ImageButton) findViewById(R.id.imageButtonPhone);
        imageButtonWeb = (ImageButton) findViewById(R.id.imageButtonWeb);
        imageButtonCamera = (ImageButton) findViewById(R.id.imageButtonCamera);

        //eventos cuando tocan el boton del telefono
        imageButtonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numTel = editTextPhone.getText().toString();
                if (numTel != null && !numTel.isEmpty()) {
                    //SI LA VERSION ACTUAL ES >= A LA VERSION MARSHALLMALLOW
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //Comprobamos si el usuario dio el permiso, lo nego o es la primer vez que
                        //se le pregunta
                        if (CheckPermission(Manifest.permission.CALL_PHONE)) {
                            //Concedio el permiso
                            Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numTel));
                            if (ActivityCompat.checkSelfPermission(thirdActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)return;
                            startActivity(intentCall);
                        }
                        //o no concedio el permiso o es la primera vez que se le pregunta
                        else {
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                                //Es la primer vez que se le pregunta
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, codigoLlamar);
                            } else {
                                //Niega el permiso
                                Toast.makeText(thirdActivity.this, "por favor habilite el permiso", Toast.LENGTH_SHORT).show();
                                 //se le evita al usuario tener que ir a buscar en configuraciones para que pueda activarlo
                                Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                //Se especifica a que aplicacion habilitar
                                //Se añade categoria
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:" + getPackageName()));
                                //Se agregan banderas que dan acceso a la configuracion de la app
                                //Y una vez configurado el permiso permite volver a la app
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//nueva tarea
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(i);
                            }
                        }
                    } else {
                        versionesAntiguas(numTel);
                    }
                } else {
                    Toast.makeText(thirdActivity.this, "ingrese un numero", Toast.LENGTH_SHORT).show();
                }
            }

            //VERSION ANTIGUA DE ANDROID
            private void versionesAntiguas(String numTel) {
                //SE CREA EL INTENT IMPLICITO DE LLAMAR
                Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel" + numTel));
                if (CheckPermission(Manifest.permission.CALL_PHONE)) {
                    if (ActivityCompat.checkSelfPermission(thirdActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                        return;
                    startActivity(intentCall);
                } else {
                    Toast.makeText(thirdActivity.this, "denegado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //SE BUSCA POR ID
        textViewCompartir = findViewById(R.id.textViewCompartir);

        //SE TOMAN LOS DATOS DEL INTENT
        Bundle bundle = getIntent().getExtras();
        //SE VERIFICA QUE NO VENGA VACIO EL BUNDLE
        if (bundle != null && bundle.getString("compartir") != null) {
            String msj = bundle.getString("compartir");
            Toast.makeText(thirdActivity.this, msj, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(thirdActivity.this, "vacio", Toast.LENGTH_SHORT).show();
        }
    }

    //SOBREESCRIBE EL METODO DE ANDROID
    //DEVUELVE LOS RESULTADOS DE LA SOLICITUD DE PERMISO
    //requestCode: codigo de servicio (100 para llamada)
    //permissions: permisos solicitados
    //resultados de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case codigoLlamar:
                String permiso = permissions[0];
                int result = grantResults[0];//resultado del usuario (permite o no)
                //VERIFICAR QUE EL PERMISO SEA EL DE LLAMADA
                if (permiso.equals(Manifest.permission.CALL_PHONE)) {
                    //conocer si se ha concedido el permiso
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        //se concede el permiso
                        String numTel = editTextPhone.getText().toString();
                        Intent intentLlamar = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numTel));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)return;
                        startActivity(intentLlamar);
                    }else {
                        //no se concede el permiso
                        Toast.makeText(thirdActivity.this, "acceso denegado", Toast.LENGTH_SHORT).show();
                }
                }break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    //METODO QUE PERMITE COMPROBAR SI CONTAMOS CON EL PERMISO EN EL MANIFEST
    //POR TANTO ES NECESARIO AGREGARLO EN EL MANIFEST
    //NO PREGUNTA AL USUARIO
    //metodo generico
    private boolean CheckPermission(String permiso) {
        int result = this.checkCallingOrSelfPermission(permiso);
        return result == PackageManager.PERMISSION_DENIED;
    }
}
