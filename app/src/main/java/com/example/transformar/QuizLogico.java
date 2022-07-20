package com.example.transformar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizLogico extends AppCompatActivity {

    CountDownTimer countDownTimer;

    int puntajeValue = 0;
    int vidaInicial = 3;
    private ProgressBar progressBar;
    private TextView puntaje, pregunta, preguntaAct;
    private RadioButton rb_op1, rb_op2, rb_op3, rb_op4;
    private RadioGroup rbGroup;
    private ImageView vidas;
    private Button sgtePregunta;
    private List<ListaPreguntas> listaPreguntas = new ArrayList<>();
    private int preguntaActual = 0;
    private String itemSelected = "";
    private int timerValue = 100;
    private int totalPreguntas = 10;
    private int totalPantalla = 10;
    private int currentQuestion = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_logico);

        rb_op1 = (RadioButton)findViewById(R.id.rb_op1);
        rb_op2 = (RadioButton)findViewById(R.id.rb_op2);
        rb_op3 = (RadioButton)findViewById(R.id.rb_op3);
        rb_op4 = (RadioButton)findViewById(R.id.rb_op4);


        puntaje = (TextView)findViewById(R.id.txt_puntaje);
        puntaje.setText(String.valueOf(puntajeValue));
        pregunta = (TextView)findViewById(R.id.txtEnunciado);


        setPreguntas();


    }

    public void iniciarContador(){
        timerValue = 100;
        progressBar = (ProgressBar)findViewById(R.id.barraTiempo);

        if(countDownTimer!=null){
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(10000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerValue = timerValue-1;
                progressBar.setProgress(timerValue);
            }
            @Override
            public void onFinish() {
                if(vidaInicial>=2){
                    Toast.makeText(QuizLogico.this, "Has perdido una vida", Toast.LENGTH_SHORT).show();
                }
                vidaInicial--;
                setVida();
                iniciarContador();
            }
        }.start();
    }

    public void setVida(){
        vidas = (ImageView)findViewById(R.id.imageView_Vidas);
        int vida = vidaInicial;

        if(vida == 3){
            vidas.setImageResource(R.drawable.tres_vidas);
        }else if(vida == 2){
            vidas.setImageResource(R.drawable.dos_vidas);
        }else if (vida == 1){
            vidas.setImageResource(R.drawable.una_vida);
        }else {
            finish();
        }
    }

    public void setPreguntas(){
        //Obtener preguntas desde firebase
        preguntaAct = (TextView)findViewById(R.id.txt_preguntaActual);
        preguntaAct.setText(String.valueOf(currentQuestion)+"/"+String.valueOf(totalPantalla));
        iniciarContador();
        sgtePregunta = (Button)findViewById(R.id.btnSgtePregunta);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://transformar-feb68-default-rtdb.firebaseio.com/");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Obteniendo todas las preguntas de firebase del tipo lógico
                for(DataSnapshot dataSnapshot : snapshot.child("logico").getChildren()){

                    final String getPregunta = dataSnapshot.child("pregunta").getValue(String.class);
                    final String getOp1 = dataSnapshot.child("opcion1").getValue(String.class);
                    final String getOp2 = dataSnapshot.child("opcion2").getValue(String.class);
                    final String getOp3 = dataSnapshot.child("opcion3").getValue(String.class);
                    final String getOp4 = dataSnapshot.child("opcion4").getValue(String.class);
                    final String getRespuesta = dataSnapshot.child("respuesta").getValue(String.class);

                    ListaPreguntas listaPregunta = new ListaPreguntas(getPregunta, getOp1, getOp2, getOp3, getOp4, getRespuesta);
                    listaPreguntas.add(listaPregunta);
                }

                Random rand = new Random();
                preguntaActual = rand.nextInt(listaPreguntas.size()) + 1;

                pregunta.setText(listaPreguntas.get(preguntaActual).getPregunta());
                rb_op1.setText(listaPreguntas.get(preguntaActual).getOp1());
                rb_op2.setText(listaPreguntas.get(preguntaActual).getOp2());
                rb_op3.setText(listaPreguntas.get(preguntaActual).getOp3());
                rb_op4.setText(listaPreguntas.get(preguntaActual).getOp4());

                sgtePregunta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentQuestion++;
                        if(totalPreguntas == 1){
                            finish();
                        }
                        else if(rb_op1.isChecked()) {
                            itemSelected = listaPreguntas.get(preguntaActual).getOp1();
                            verificarRespuesta(itemSelected, listaPreguntas.get(preguntaActual).getRespuesta());
                        } else if(rb_op2.isChecked()) {
                            itemSelected = listaPreguntas.get(preguntaActual).getOp2();
                            verificarRespuesta(itemSelected, listaPreguntas.get(preguntaActual).getRespuesta());
                        } else if(rb_op3.isChecked()) {
                            itemSelected = listaPreguntas.get(preguntaActual).getOp3();
                            verificarRespuesta(itemSelected, listaPreguntas.get(preguntaActual).getRespuesta());
                        } else if(rb_op4.isChecked()) {
                            itemSelected = listaPreguntas.get(preguntaActual).getOp4();
                            verificarRespuesta(itemSelected, listaPreguntas.get(preguntaActual).getRespuesta());
                        } else {
                            Toast.makeText(v.getContext(), "Debe seleccionar una opción", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void verificarRespuesta(@NonNull String selected, String respuesta){
        rbGroup = (RadioGroup)findViewById(R.id.radioGroupOpciones);
        if(selected.equals(respuesta)){
            preguntaActual++;
            countDownTimer.cancel();
            setPreguntas();
            puntajeValue = puntajeValue+10;
            puntaje.setText(String.valueOf(puntajeValue));
            rbGroup.clearCheck();
            totalPreguntas--;
        }else{
            preguntaActual++;
            vidaInicial--;
            setVida();
            setPreguntas();
            rbGroup.clearCheck();
            totalPreguntas--;
        }
    }

}