package com.example.testyananas5;

import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

/**
 *  Ta klasa tworzy główny wątek gry
 * @author Wiktor Milecki
 */
public class MainThread extends Thread {
    private final SurfaceHolder surfaceHolder;
    /**Obiekt gameView typu GameView*/
    private final GameView gameView;
    /**Zmienna przechowująca informacje o działaniu pętli w metodzie run*/
    private boolean running;
    /**Płótno na którym jest rysowany ekran gry*/
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    /**
     * Metoda run rysuje obraz gry i aktualizuje elementy widoku
     */
    @Override
    public void run() {
            while (running) {
                canvas = null;
                try {
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        this.gameView.update();
                        this.gameView.draw(canvas);
                    }
                } catch (Exception e) {
                } finally {
                    if (canvas != null) {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }
}
