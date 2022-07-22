package com.example.transformar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class PerfilUsuario extends AppCompatActivity {

    private TextView correo, telefono, nombre;
    private Button btnCambiarDatos;
    FirebaseAuth firebaseAuth;
    DatabaseReference mRootReference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        firebaseAuth = FirebaseAuth.getInstance();
        mRootReference = FirebaseDatabase.getInstance().getReference();
        nombre = (TextView)findViewById(R.id.txtNombrePerfil);
        correo = (TextView)findViewById(R.id.txtCorreoPerfil);
        telefono = (TextView)findViewById(R.id.txtTelefonoPerfil);
        btnCambiarDatos = (Button)findViewById(R.id.btnCambiarDatos);
        userID = firebaseAuth.getCurrentUser().getUid();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_menu_24));

        setSupportActionBar(toolbar);

        mRootReference.child("usuarios").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    nombre.setText(dataSnapshot.child("nombre").getValue(String.class)+" "+dataSnapshot.child("apellido").getValue(String.class));
                    correo.setText(dataSnapshot.child("correo").getValue(String.class));
                    telefono.setText(dataSnapshot.child("telefono").getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PerfilUsuario.this, "No se encontró el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        btnCambiarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), CambioDatosPerfil.class);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_app, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item1:
                Intent i = new Intent(this, PerfilUsuario.class);
                startActivity(i);
                return true;
            case R.id.menu_item2:
                FirebaseAuth.getInstance().signOut();
                abrirLogin();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirLogin() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}