package com.example.cazapatos.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cazapatos.R;
import com.example.cazapatos.common.Constantes;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCounterPatos, tvTimer, tvNick;
    private ImageView ivPato, ivDialog;
    private int counter = 0;
    private int anchoPantalla;
    private int altoPantalla;
    private Random random;
    private boolean acabado = false;

    private int tiempoMaximo = 60000;

    private String id, nick;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //iniciamos conexion a firestore
        db = FirebaseFirestore.getInstance();

        //iniciamos variables de los componentes
        tvCounterPatos = findViewById(R.id.textViewCounter);
        tvTimer = findViewById(R.id.textViewTimer);
        tvNick = findViewById(R.id.textViewNick);
        ivPato = findViewById(R.id.imageViewPato);
        ivDialog = findViewById(R.id.imageViewDialog);

        //cambiamos fuentes
        Typeface typeface = Typeface.createFromAsset(getAssets(), "pixel.ttf");
        tvCounterPatos.setTypeface(typeface);
        tvTimer.setTypeface(typeface);
        tvNick.setTypeface(typeface);

        // Obtencion del nick y ponerlo de titulo en la pantalla de juego
        Bundle extras = getIntent().getExtras();
        nick = extras.getString(Constantes.EXTRA_NICK);
        id = extras.getString(Constantes.EXTRA_ID);

        tvNick.setText(nick);

        //listeners
        ivPato.setOnClickListener(this);
        ivDialog.setOnClickListener(this);

        //cogemos la grandaria de la pantalla del dispositivo donde ejecutaremos el juego
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        anchoPantalla = size.x; //x por coordenada x izquierdaderecha
        altoPantalla = size.y; //y por la coordenada y arribaabajo

        //inicializamos el objeto random
        random = new Random();

        //cuenta atras
        cuentaAtras();

        //mueve el animal por la pantalla
        moverAnimalSiempre();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewPato:
                //si la partida no ha acabado aun, se puede hacer click al pato
                if (!acabado) {
                    //sube el contador de numero de veces hecho click sobre el pato
                    counter++;
                    tvCounterPatos.setText(String.valueOf(counter));

                    //cambia la imagen del pato al ser clicado y la restaura en medio segundo
                    ivPato.setImageResource(R.drawable.duck_clicked);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ivPato.setImageResource(R.drawable.duck);
                            moverPato();
                        }
                    }, 100);
                }
                break;
            case R.id.imageViewDialog:
                mostrarCuadroDialogoAcabado();
                break;
        }
    }

    // cambia la posicion del animal, por defecto pato, en una otra random de la pantalla
    private void moverPato() {
        int min = 0; //(0,0)
        int maxX = anchoPantalla - ivPato.getWidth(); //restandole la anchura del pato es para que no se salga
        //de la pantalla su cuerpo
        int maxY = altoPantalla - ivPato.getHeight();//restandole la altura del pato es para que no se salga
        //de la pantalla su altura

        //generamos dos numeros aleatorios, para la coordenada x y.
        int randomX = random.nextInt(((maxX - min) + 1) + min);
        int randomY = random.nextInt(((maxY - min) + 1) + min);

        //utilizamos los randoms para mover el pato
        ivPato.setX(randomX);
        ivPato.setY(randomY);
    }

    private void cuentaAtras() {
        new CountDownTimer(tiempoMaximo, 1000) { //param1: recibe los milisegundos que va a durar param2: milisegundos que va a ir bajando

            //se llama cada segundo que pasa
            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished / 1000;
                tvTimer.setText(segundosRestantes + "s");
            }

            //cuando acaba el tiempo
            public void onFinish() {
                tvTimer.setText("0s");
                acabado = true;
                mostrarCuadroDialogoAcabado();
                saveResultFirestore();
            }
        }.start();
    }

    private void moverAnimalSiempre() {
        final Timer timer = new Timer();
        if (!acabado) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                moverPato();
                            }
                        });
                    }
                }, 0, 1000);
        }
    }

    //el usuario vera los patos que ha cazado y ademas vamos a gestionar la alternativa de salir del juego o reiniciar la partida
    private void mostrarCuadroDialogoAcabado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setMessage("Has conseguido cazar " + counter + " patos")
                .setTitle("Game Over");

        builder.setPositiveButton("Reiniciar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                counter = 0;
                tvCounterPatos.setText("0");
                acabado = false;
                cuentaAtras();
                moverPato();
            }
        });
        builder.setNegativeButton("Ver Ranking", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Intent i = new Intent(GameActivity.this, RankingActivity.class);
                startActivity(i);
            }
        });
        /*
        builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                finish();
            }
        });
        */

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void saveResultFirestore() {
        db.collection("users").document(id).update("animal", counter);
    }
}
