package com.example.transformar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Inicio extends AppCompatActivity {

    private Button bntLogOut;
    private CardView rLogico, rNumerico, rVerbal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        bntLogOut = (Button)findViewById(R.id.btnLogOut);
        rLogico = (CardView)findViewById(R.id.CardItem);
        rNumerico = (CardView)findViewById(R.id.CardItem2);
        rVerbal = (CardView)findViewById(R.id.CardItem3);

        bntLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                abrirLogin();
            }
        });

        rLogico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RlogicoInicio.class);
                startActivity(i);
            }
        });


    }

    private void abrirLogin() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}