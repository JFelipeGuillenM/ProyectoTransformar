package com.example.transformar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Inicio extends AppCompatActivity {

    private Button bntLogOut;
    private CardView rLogico, rNumerico, rVerbal;
    FirebaseAuth firebaseAuth;
    DatabaseReference mRootReference;
    private String userID;
    private String nombreCompleto;

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
                obtenerNombre();
            }
        });


    }

    private void abrirLogin() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public String obtenerNombre(){
        firebaseAuth = FirebaseAuth.getInstance();
        mRootReference = FirebaseDatabase.getInstance().getReference();

        userID = firebaseAuth.getCurrentUser().getUid();

        mRootReference.child("usuarios").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String nombre = dataSnapshot.child("nombre").getValue(String.class);
                    String apellido = dataSnapshot.child("apellido").getValue(String.class);
                    nombreCompleto = nombre+" "+apellido;
                    Toast.makeText(Inicio.this, nombreCompleto, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Inicio.this, "No se encontró el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        return nombreCompleto;
    }
}