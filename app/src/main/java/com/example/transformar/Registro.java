package com.example.transformar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    private EditText nombre, apellido, telefono, correo, contrasena;
    private Button btnregistrar;
    private String userID;

    FirebaseAuth firebaseAuth;
    DatabaseReference mRootReference;
    //FirebaseFirestore db;
    AwesomeValidation awesomeValidation;

    //RealTime Database




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        firebaseAuth = FirebaseAuth.getInstance();
        mRootReference = FirebaseDatabase.getInstance().getReference();
        //db = FirebaseFirestore.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //RealtimeDatabase

        nombre = findViewById(R.id.txtNombreRegistro);
        apellido = findViewById(R.id.txtApellidoRegistro);
        telefono = findViewById(R.id.txtTelefonoRegistro);
        correo = findViewById(R.id.txtCorreoRegistro);
        contrasena = findViewById(R.id.txtContrasenaRegistro);
        btnregistrar = findViewById(R.id.btnRegistrar);

        //awesomeValidation.addValidation(this, R.id.txtNombreRegistro, "[a-zA-Z\\s]+", R.string.nombreInvalido);
        //awesomeValidation.addValidation(this, R.id.txtApellidoRegistro, "[a-zA-Z\\s]+", R.string.apellidoInvalido);
        awesomeValidation.addValidation(this, R.id.txtTelefonoRegistro, RegexTemplate.TELEPHONE, R.string.telefonoInvalido);
        awesomeValidation.addValidation(this, R.id.txtCorreoRegistro, Patterns.EMAIL_ADDRESS,R.string.correoInvalido);
        awesomeValidation.addValidation(this, R.id.txtContrasenaRegistro, ".{6,}", R.string.contrasenaInvalida);



        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = correo.getText().toString();
                String pass = contrasena.getText().toString();
                String nomb = nombre.getText().toString();
                String apell = apellido.getText().toString();
                String telf =  telefono.getText().toString();

                if(!nomb.isEmpty() && !apell.isEmpty() && awesomeValidation.validate()){
                    firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                userID = firebaseAuth.getCurrentUser().getUid();

                                Map<String, Object> usuario = new HashMap<>();
                                usuario.put("nombre", nomb);
                                usuario.put("apellido", apell);
                                usuario.put("telefono", telf);
                                usuario.put("correo", mail);

                                mRootReference.child("usuarios").child(userID).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Registro.this, "Usuario registrado con éxto", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                finish();
                            }else{
                                String error = ((FirebaseAuthException) task.getException()).getErrorCode();
                                Toast.makeText(Registro.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(v.getContext(),"Debe completar correctamente todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}