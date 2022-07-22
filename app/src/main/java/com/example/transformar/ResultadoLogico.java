package com.example.transformar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ResultadoLogico extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView txt_resultado;
    private int resultado = 0;
    Bundle datoPuntaje;
    private Button btnInicio;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_logico);

        if(countDownTimer!=null){
            countDownTimer.cancel();
        }

        txt_resultado = (TextView)findViewById(R.id.txt_resultado);
        btnInicio = (Button)findViewById(R.id.btnVolverInicio);
        progressBar = (ProgressBar)findViewById(R.id.barra_resultado);
        datoPuntaje = getIntent().getExtras();
        resultado = datoPuntaje.getInt("puntaje");
        txt_resultado.setText(String.valueOf(resultado));

        //progressBar.setMax(100);
        progressBar.setProgress(resultado);

        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(v.getContext(), Inicio.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}