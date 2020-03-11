package com.example.cazapatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cazapatos.common.Constantes;

import java.lang.reflect.Type;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etNick;
    Button btnStart;
    String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etNick = findViewById(R.id.editTextNick);
        btnStart = findViewById(R.id.buttonStart);

        //cambiamos fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(), "pixel.ttf");
        etNick.setTypeface(typeface);
        btnStart.setTypeface(typeface);

        //Listeners
        btnStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonStart:
                nick = etNick.getText().toString();
                if (nick.isEmpty()){
                    etNick.setError("El nombre de usuario es obligatorio para poder empezar");
                }else if (nick.length() < 3) {
                    etNick.setError("Debe tener almenos 3 caracteres");
                }else{
                    etNick.setText(""); //al volver de la pantalla de juego, se limpiara el nombre
                    Intent i = new Intent(LoginActivity.this, GameActivity.class);
                    i.putExtra(Constantes.EXTRA_NICK,nick);
                    startActivity(i);
                }
                break;
        }
    }
}
