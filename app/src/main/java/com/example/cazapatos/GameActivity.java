package com.example.cazapatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cazapatos.common.Constantes;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvCounterPatos, tvTimer, tvNick;
    ImageView ivPato;
    int counter = 0;

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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewPato: //sube el contador de numero de veces hecho click sobre el pato
                counter++;
                tvCounterPatos.setText(String.valueOf(counter));
                break;
        }
    }
}
