package com.example.transformar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CambioDatosPerfil extends AppCompatActivity {
    //Declaración de variables
    private EditText nombre, apellido, telefono, correo;
    private Button btnregistrar;
    private String userID;
    FirebaseAuth firebaseAuth;
    DatabaseReference mRootReference;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_datos_perfil);

        //Obteniendo las instancias de firebase
        firebaseAuth = FirebaseAuth.getInstance();
        mRootReference = FirebaseDatabase.getInstance().getReference();
        //Obtención del id de firebase del ususario logeado
        userID = firebaseAuth.getCurrentUser().getUid();

        //Declarando el objeto awesome validation para validar campos Edit text
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //Enlazando variables con elementos de la vista
        nombre = (EditText)findViewById(R.id.txtNombreCambio);
        apellido = (EditText)findViewById(R.id.txtApellidoCambio);
        telefono = (EditText)findViewById(R.id.txtTelefonoCambio);
        correo = (EditText)findViewById(R.id.txtCorreoCambio);
        btnregistrar = (Button)findViewById(R.id.btnAceptar);

        //Asignando toolbar con menu al activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_menu_24));
        setSupportActionBar(toolbar);


        //Asignando validación al edittext teléfono
        awesomeValidation.addValidation(this, R.id.txtTelefonoCambio, RegexTemplate.TELEPHONE, R.string.telefonoInvalido);

        //Trayendo datos de firebase en base al id, para mostrarlos en los edittext
        mRootReference.child("usuarios").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    nombre.setText(dataSnapshot.child("nombre").getValue(String.class));
                    apellido.setText(dataSnapshot.child("apellido").getValue(String.class));
                    correo.setText(dataSnapshot.child("correo").getValue(String.class));
                    telefono.setText(dataSnapshot.child("telefono").getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CambioDatosPerfil.this, "No se encontró el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        //Evento para registrar los datos
        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomb = nombre.getText().toString();
                String apell = apellido.getText().toString();
                String telf =  telefono.getText().toString();
                String mail = correo.getText().toString();

                //Condición para realizar la actualización de datos
                //no deben estar vacíos los campos de nombre y apellido y debe cumplirse la validación de awesomevalidation
                if(!nomb.isEmpty() && !apell.isEmpty() && awesomeValidation.validate()){
                    Map<String, Object> usuario = new HashMap<>();
                    usuario.put("nombre", nomb);
                    usuario.put("apellido", apell);
                    usuario.put("telefono", telf);
                    usuario.put("correo", mail);

                    //Actualización de los datos del usuario en base a su id
                    mRootReference.child("usuarios").child(userID).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(CambioDatosPerfil.this, "Datos Actualizados", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }else{
                    Toast.makeText(v.getContext(),"Debe completar correctamente todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Cargando menu en el activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_app, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Eventos de los items del menu
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

    //Método para cerar sesión y regresar al activity login
    private void abrirLogin() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}