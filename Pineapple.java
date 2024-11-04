package com.example.testyananas5;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.Toast;

import java.util.Random;

/**
 * Klasa odpowiadająca za generowanie pozycji x oraz y ananasa, wyświetlenia na ananasie wartości liczbowej,
 * czyli jednej wartości z potencjalnych rozwiązań równania oraz rysowania ananasa na canvasie.
 * @author Wiktor Milecki
 */
public class Pineapple {
    /**Nowy obiekt klasy Paint*/
    private final Paint textPaint = new Paint();
    /**Nowa bitmapa ananasa przed skalowaniem*/
    private final Bitmap pineapplebeforescale;
    /**Nowa bitmapa ananasa po skalowaniu*/
    private Bitmap scaledPineapple;
    /**Pozycja x ananasa*/
    private int x;
    /**Pozycja y ananasa*/
    private int y;
    /**Początkowa pozycja y ananasa*/
    private int y1;
    /**Zmienna przechowująca informacje o przyjętym podziale ekranu (aby ananasy były odpowiednio rozłożone na ekranie)*/
    private final int division=5;
    /**Zmienna przechowująca numer ananasa*/
    private final int pineappleNumber;
    /**Zmienna którą inkrementowana jest pozycja y ananasa*/
    private final int ypos = 3;
    /**Zmienna którą inkrementowana jest pozycja y ananasa po przekroczeniu określonego przedziału*/
    private final int ypos1 = 4;
    /**Zmienna którą inkrementowana jest pozycja y ananasa po przekroczeniu określonego przedziału*/
    private final int ypos2 = 5;
    /**Zmienna którą inkrementowana jest pozycja y ananasa po przekroczeniu określonego przedziału*/
    private final int ypos3 = 5;
    /**Zmienna przechowująca pobraną z urządzenia szerokość ekranu*/
    private final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    /**Zmienna przechowująca pobraną z urządzenia wysokość ekranu*/
    private final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    /**Zmienna przez którą wyświetlana jest wartość liczbowa na ananasie*/
    private String number;
    /**Przechowuje informacje o tym czy ananasa dotknął dolnej krawędzi ekranu*/
    private boolean end;
    /**Zmienna do której przekazywany jest wynik, który osiąga gracz podczas gry*/
    private int points;

    /**
     * Konstruktor klasy Pineapple
     * @param bmp przekazywana bitmapa ananasa w celu narysowania jej na canvasie
     * @param pineappleNumber przekazywany numer ananasa w celu wybrania odpowiedniej pozycji x
     */
    public Pineapple(Bitmap bmp, int pineappleNumber) {
        pineapplebeforescale = bmp;
        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize(70);
        textPaint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        textPaint.setTypeface(Typeface.SANS_SERIF);
        textPaint.setTextAlign(Paint.Align.CENTER);
        this.pineappleNumber=pineappleNumber;
        pineappleCoordinates(pineappleNumber);
        scaledPineapple = scaleDown(pineapplebeforescale, 500, true);
    }

    /**
     * W metodzie draw rysowany jest ananas  oraz wartość liczbowa
     * @param canvas to płótno na którym rysowany jest ananas i wartość liczbowa wyświetlana na nim
     */
    public void draw(Canvas canvas) {
        canvas.drawBitmap(scaledPineapple, x, y, null);
        canvas.drawText(number, x+scaledPineapple.getWidth()/2, y+scaledPineapple.getHeight()/5*4, textPaint);
    }

    /**
     * W tej metodzie aktualizowane są pozycje y ananasów oraz sprawdzane jest czy ananas nie dotknął dolnej krawędzi ekranu
     * @param point jest to liczba punktów, które osiągnął gracz i w zależności od niej zwiększana jest szybkość spadania ananasów
     */
    public void update(int point) {
        this.points=point;
        points++;

        if (points<10)
        {  y += ypos;}
        else if(points < 20)
        {
            y += ypos1;
        }
        else if(points < 30)
        {
            y += ypos2;
        }
        else {
            y += ypos3;
        }
        end=false;

        if (y > screenHeight-scaledPineapple.getHeight() ) {
            y=y1;
            pineappleCoordinates(pineappleNumber);
            end=true;
        }
    }

    /**
     * W tej metodzie skalowane są ananasy
     * @param realImage oznacza przekazywany do skalowania obraz
     * @param maxImageSize maksymalny rozmiar ananasa
     * @param filter true oznacza lepszą jakość obrazu kosztem gorszej wydajności
     * @return zwraca przeskalowany obraz
     */
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());
        if (ratio >= 1.0){ return realImage;}
        return Bitmap.createScaledBitmap(realImage, width, height, filter);
    }

    /**
     * Metoda odpowiedzialna za losowe generowanie pozycji x każego z anansa tak by nie wystąpiła sytuacja, w której ananasy nałożyły by się na siebie
     * @param pineappleNumber zmienna przechwoująca numer ananasa
     */
    public void pineappleCoordinates(int pineappleNumber)
    {
        switch (pineappleNumber) {
            case 1:
                x=randomGenerator(0,screenWidth/division);
                break;
            case 2:
                x=randomGenerator(screenWidth/division+screenWidth/10,screenWidth/division*2);
                break;
            case 3:
                x=randomGenerator(screenWidth/division*2+screenWidth/10,screenWidth/division*3);
                break;
            case 4:
                x=randomGenerator(screenWidth/division*3+screenWidth/10,screenWidth/division*4+screenWidth/20);
                break;
        }
    }

    /**
     * Metoda generująca losową liczbę w zależności od podanego przedziału
     * @param minimumNumber dolna granica przedziału losowania
     * @param maximumNumber górna granica przedziału losowania
     * @return zwraca losowo wygenerowaną liczbę z podanego przedziału
     */
    private int randomGenerator (int minimumNumber, int maximumNumber){
        return (new Random()).nextInt((maximumNumber-minimumNumber)+1)+minimumNumber;
    }

    /**
     * Getter do szerokości przeskalowanego ananasa
     * @return zwraca pobraną szerokość przeskalowanego ananasa
     */
    public int getImgWidth() { return this.scaledPineapple.getWidth(); }

    /**
     * Getter do wysokości przeskalowanego ananasa
     * @return zwraca pobraną wysokość przeskalowanego ananasa
     */
    public int getImgHeight() { return this.scaledPineapple.getHeight(); }

    /**
     * Pobiera pozycję x ananasa
     * @return Zwraca pozycję x
     */
    public int getLeft() {
        return this.x;
    }

    /**
     * Pobiera pozycje y ananasa
     * @return Zwraca pozycje y
     */
    public int getTop() {
        return this.y;
    }

    /**
     * Setter służący do przekazania wyniku równania (błędnego lub właściwego)
     * @param number przechowuje informacje o wyniku równania
     */
    public void setEquationScore(String number) {
        this.number=number;
    }

    /**
     * Metoda służąca do zmiany parametru y na wartość początkową y1
     */
    public void changeY(){
        y=y1;
    }

    /**
     *Przechowuje informacje o tym czy ananas dotknął dolnej krawędzi ekranu
     * @return zwracane jest true jeśli ananas dotknął dolnej krawędzi albo false jeżeli nie
     */
    public boolean getEnd() {
        return this.end;
    }
}
