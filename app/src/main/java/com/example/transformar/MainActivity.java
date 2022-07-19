package com.example.transformar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.MoreObjects;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView registro;
    private EditText correo, cont;
    private Button btnLogin;
    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        verificarSesion(user);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.txt_correo, Patterns.EMAIL_ADDRESS,R.string.correoInvalido);
        correo = (EditText)findViewById(R.id.txt_correo);
        cont = (EditText)findViewById(R.id.txt_password);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        registro = (TextView)findViewById(R.id.txt_crear_cuenta);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Registro.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()){
                    String mail = correo.getText().toString();
                    String pass = cont.getText().toString();

                    firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                abrirInicio();
                            }else{
                                Toast.makeText(MainActivity.this, "Correo electrónico o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private void verificarSesion(FirebaseUser user) {
        if(user != null){
            abrirInicio();
        }
    }

    private void abrirInicio() {
        Intent i = new Intent(this, Inicio.class);
        startActivity(i);
    }
}