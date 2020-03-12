package com.example.cazapatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cazapatos.common.Constantes;
import com.example.cazapatos.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etNick;
    Button btnStart;
    String nick;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //instanciar la conexion a cloud firestore
        db = FirebaseFirestore.getInstance();

        //
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
                //cogemos el nick insertado por el usuario en el EditText
                nick = etNick.getText().toString();
                if (nick.isEmpty()){
                    etNick.setError("El nombre de usuario es obligatorio para poder empezar");
                }else if (nick.length() < 3) {
                    etNick.setError("Debe tener almenos 3 caracteres");
                }else{
                    //introducir en la bd el nick del usuario con 0 animales matados
                    //no puede haver dos nicks iguales, buscaremos en la bd
                    db.collection("users").whereEqualTo("nick",nick).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.size()>0){ //el nick si ya estaba, el usuario tendra que insertar un nuevo nick que este disponible
                                etNick.setError("El nick no está disponible");
                            }else{
                                //en caso de no estar el nick, lo insertamos al firestore
                                db.collection("users").add(new User(nick,0)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        etNick.setText(""); //al volver de la pantalla de juego, se limpiara el nombre
                                        Intent i = new Intent(LoginActivity.this, GameActivity.class);
                                        i.putExtra(Constantes.EXTRA_NICK,nick);
                                        startActivity(i);
                                    }
                                });
                            }
                        }
                    });
                }
                break;
        }
    }
}
