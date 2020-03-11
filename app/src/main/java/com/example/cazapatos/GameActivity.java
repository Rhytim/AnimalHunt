package com.example.cazapatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cazapatos.common.Constantes;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvCounterPatos, tvTimer, tvNick;
    ImageView ivPato;
    int counter = 0;
    int anchoPantalla;
    int altoPantalla;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //iniciamos variables de los componentes
        tvCounterPatos = findViewById(R.id.textViewCounter);
        tvTimer = findViewById(R.id.textViewTimer);
        tvNick = findViewById(R.id.textViewNick);
        ivPato = findViewById(R.id.imageViewPato);

        //cambiamos fuentes
        Typeface typeface = Typeface.createFromAsset(getAssets(), "pixel.ttf");
        tvCounterPatos.setTypeface(typeface);
        tvTimer.setTypeface(typeface);
        tvNick.setTypeface(typeface);

        // Obtencion del nick y ponerlo de titulo en la pantalla de juego
        Bundle extras = getIntent().getExtras();
        String nick = extras.getString(Constantes.EXTRA_NICK);
        tvNick.setText(nick);

        //listeners
        ivPato.setOnClickListener(this);

        //cogemos la grandaria de la pantalla del dispositivo donde ejecutaremos el juego
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        anchoPantalla = size.x; //x por coordenada x izquierdaderecha
        altoPantalla = size.y; //y por la coordenada y arribaabajo

        //inicializamos el objeto random
        random = new Random();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewPato:
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
                }, 500);

                break;
        }
    }

    public void moverPato(){
        int min = 0; //(0,0)
        int maxX = anchoPantalla - ivPato.getWidth(); //restandole la anchura del pato es para que no se salga
        //de la pantalla su cuerpo
        int maxY = altoPantalla - ivPato.getHeight();//restandole la altura del pato es para que no se salga
        //de la pantalla

        //generamos dos numeros aleatorios, para la coordenada x y.
        int randomX = random.nextInt(((maxX - min)+1)+min);
        int randomY = random.nextInt(((maxY - min)+1)+min);

        //utilizamos los randoms para mover el pato
        ivPato.setX(randomX);
        ivPato.setY(randomY);
    }
}
