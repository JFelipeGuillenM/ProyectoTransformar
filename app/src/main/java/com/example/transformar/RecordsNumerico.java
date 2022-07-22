package com.example.transformar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RecordsNumerico extends AppCompatActivity {

    private TextView nombrePrimero, puntosPrimero, nombreSegundo, puntosSegundo,
            nombreTercero, puntosTercero, nombreCuarto, puntosCuarto, nombreQuinto, puntosQuinto;
    private Button btnSalir;
    String[] puntajes = new String[5];
    String[] nombres = new String[5];
    int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_numerico);

        btnSalir = (Button)findViewById(R.id.btnSalirInicio);
        nombrePrimero = (TextView)findViewById(R.id.txt_nombre1);
        nombreSegundo = (TextView)findViewById(R.id.txt_nombre2);
        nombreTercero = (TextView)findViewById(R.id.txt_nombre3);
        nombreCuarto = (TextView)findViewById(R.id.txt_nombre4);
        nombreQuinto = (TextView)findViewById(R.id.txt_nombre5);

        puntosPrimero = (TextView)findViewById(R.id.txt_puntaje1);
        puntosSegundo = (TextView)findViewById(R.id.txt_puntaje2);
        puntosTercero = (TextView)findViewById(R.id.txt_puntaje3);
        puntosCuarto = (TextView)findViewById(R.id.txt_puntaje4);
        puntosQuinto = (TextView)findViewById(R.id.txt_puntaje5);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("recordsNumerico").orderBy("puntaje", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()) {
                        Log.d("datos", document.getId() + "=>" + document.getData());
                        if (contador < 5){
                            puntajes[contador] = String.valueOf(document.get("puntaje"));
                            nombres[contador] = String.valueOf(document.get("nombre"));
                        }
                        contador++;
                    }

                    nombrePrimero.setText(nombres[0]);
                    nombreSegundo.setText(nombres[1]);
                    nombreTercero.setText(nombres[2]);
                    nombreCuarto.setText(nombres[3]);
                    nombreQuinto.setText(nombres[4]);

                    puntosPrimero.setText(puntajes[0]);
                    puntosSegundo.setText(puntajes[1]);
                    puntosTercero.setText(puntajes[2]);
                    puntosCuarto.setText(puntajes[3]);
                    puntosQuinto.setText(puntajes[4]);
                }
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(v.getContext(), RnumericoInicio.class);
                startActivity(i);
            }
        });
    }
}