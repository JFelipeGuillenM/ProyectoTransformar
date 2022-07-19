package com.example.transformar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RlogicoInicio extends AppCompatActivity {

    private Button btnIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rlogico_inicio);

        btnIniciar = (Button)findViewById(R.id.btnIniciarLogico);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), QuizLogico.class);
                startActivity(i);
            }
        });
    }
}