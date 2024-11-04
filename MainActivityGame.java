package com.example.testyananas5;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Ta klasa jest odpowiedzialna za ustawienie nowego widoku oraz wymuszenia poziomej orientacji ekranu
 * @author Wiktor Milecki
 */
public class MainActivityGame extends Activity {
    @Override
    /**
     * Metoda, która ustawia nowy widok oraz wymusza poziomą orientację ekranu
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(new GameView(this));
    }

}