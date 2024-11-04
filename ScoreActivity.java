package com.example.testyananas5;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Ta klasa odpowiada za uruchomienie ekranu z wynikiem końcowym po zakończeniu gry lub po skończeniu się dostępnych żyć użytkownika
 * @author Wiktor Milecki
 */
public class ScoreActivity extends AppCompatActivity {
    
    /**Przycisk POWRÓT DO MENU*/
    private Button ReturnButton;
    /**Tekst który informuje gracza o wyniku końcowym gry*/
    private TextView YourScore;

    /**
     * Tworzony jest nowy widok, w którym widnieje przycisk powrotu do menu oraz wyświetlany jest wynik gracza
     */
    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Intent intent= getIntent();
        Bundle i = intent.getExtras();
        YourScore = findViewById(R.id.twojwynik);

        if(i!=null)
        {
            String score=(String) i.get("points");
            YourScore.setText("Twój wynik to: "+ score );
        }

        ReturnButton = findViewById(R.id.PrzyciskPowrot);
        ReturnButton.setOnClickListener(v -> openActivity3());
    }

    /**
     * Gdy ta metoda jest wywołana, uruchamiana jest z powrotem klasa MainActivity
     */
    public void openActivity3(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}