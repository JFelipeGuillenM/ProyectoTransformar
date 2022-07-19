package com.example.transformar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.StringValue;

public class QuizLogico extends AppCompatActivity {

    CountDownTimer countDownTimer;
    int timerValue = 100;
    int puntajeValue = 0;
    int vidas = 3;
    ProgressBar progressBar;
    TextView puntaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_logico);

        puntaje = (TextView)findViewById(R.id.txt_puntaje);
        puntaje.setText(String.valueOf(puntajeValue));

        iniciarContador();

    }

    public void iniciarContador(){
        progressBar = (ProgressBar)findViewById(R.id.barraTiempo);
        countDownTimer = new CountDownTimer(120000, 1200) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerValue = timerValue-1;
                progressBar.setProgress(timerValue);
            }
            @Override
            public void onFinish() {
                Toast.makeText(QuizLogico.this, "Ha terminado el contador", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    public void setVida(){

    }

}