
package com.example.testyananas5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Ta klasa jest odpowiedzialna za ustawienie ekranu początkowego gry (menu głównego)
 *  @author Wiktor Milecki
 */
public class MainActivity extends AppCompatActivity {
    /**Przycisik GRAJ*/
    private Button PlayButton;
    /**Przycisk AUTOR*/
    private Button AuthorButton;
    /**Przycisk JAK GRAĆ*/
    private Button HowToPlay;
    /**Logo Spadające Ananasy*/
    private ImageView Logo;
    /**Instrukcja jak grać w gre i co jest czym*/
    private ImageView BackgroundIMG;
    /**Przycisk powrotu*/
    private ImageView Return;
     /**Informacje o autorze*/
    private TextView AuthorInfo;

    /**
     * W metodzie onCreate znajduje się mechanizm, który w zależności od kliknięcia użytkownika w dany przycisk otwiera nowy ekran z grą lub innymi informacjami.
     * Gdy zostanie wciśnięty przycisk AUTOR wyświetl tekst oraz ukryj wszystkie przyciski i pokaz przycisk powrotu.
     * Gdy zostanie wciśnięty przycisk JAK GRAĆ wyŚwietl zdjęcie z instrukcją oraz ukryje wszystkie przyciski i pokaże przycisk powrotu.
     * Gdy zostanie wciśnięty przycisk powrotu przywróć widoczność przycisków z menu głównego i ukryj dane o autorze albo informację o tym jak grać w grę.
     * Gdy zostanie wciśnięty przycisk GRAJ, gra zostanie uruchomiona.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AuthorButton = findViewById(R.id.autor);
        PlayButton = findViewById(R.id.PrzyciskGraj);
        HowToPlay = findViewById(R.id.jakgrac);
        Logo = findViewById(R.id.logo);
        BackgroundIMG = findViewById(R.id.tlojakgrac);
        Return = findViewById(R.id.powrot1);
        AuthorInfo = findViewById(R.id.autorinfo);
        PlayButton.setOnClickListener(v -> openActivity2());
        AuthorButton.setOnClickListener(v -> {
            AuthorInfo.setText("Autor: Wiktor Milecki\nNumer indeksu: 176139\nKierunek: Eit\nGrupa: Elektronika 2");
            PlayButton.setVisibility(View.INVISIBLE);
            HowToPlay.setVisibility(View.INVISIBLE);
            Logo.setVisibility(View.INVISIBLE);
            AuthorButton.setVisibility(View.INVISIBLE);
            AuthorInfo.setVisibility(View.VISIBLE);
            Return.setVisibility(View.VISIBLE);
        });
        HowToPlay.setOnClickListener(v -> {
            PlayButton.setVisibility(View.INVISIBLE);
            HowToPlay.setVisibility(View.INVISIBLE);
            Logo.setVisibility(View.INVISIBLE);
            AuthorButton.setVisibility(View.INVISIBLE);
            BackgroundIMG.setVisibility(View.VISIBLE);
            Return.setVisibility(View.VISIBLE);
        });
        Return.setOnClickListener(v -> {
            PlayButton.setVisibility(View.VISIBLE);
            HowToPlay.setVisibility(View.VISIBLE);
            Logo.setVisibility(View.VISIBLE);
            AuthorButton.setVisibility(View.VISIBLE);
            AuthorInfo.setVisibility(View.INVISIBLE);
            Return.setVisibility(View.INVISIBLE);
            BackgroundIMG.setVisibility(View.INVISIBLE);
        });
    }

    /**
     * Ta metoda otwiera klasę MainActivityGame
     */
    public void openActivity2(){
        Intent intent = new Intent(this, MainActivityGame.class);
        startActivity(intent);
    }


}
