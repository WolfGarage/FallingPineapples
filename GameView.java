package com.example.testyananas5;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import java.util.Random;


/**
 * Klasa odpowiadająca za rysowanie elementów gry na canvasie oraz mechanikę gry
 * @author Wiktor Milecki
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private final Context myContext;
    /**Obiekt thread typu MainThread*/
    private final MainThread thread;
    /**Obiekt pineapple typu Pineapple*/
    private Pineapple pineapple;
    /**Obiekt pineapple1 typu Pineapple*/
    private Pineapple pineapple1;
    /**Obiekt pineapple2 typu Pineapple*/
    private Pineapple pineapple2;
    /**Obiekt pineapple3 typu Pineapple*/
    private Pineapple pineapple3;
    /**Zmienna przechowująca numer ananasa (od 1 do 4) wylosowany z metody randomNumber*/
    private int randomResult;
    /**Zmienna informująca o tym czy ananas ma spadać (z górnej do dolnej części planszy) czy ma zostać zresetowana jego pozycja y (ma wrócić na pozycje startową)*/
    private boolean allowToDraw;
     /**Obiekt klasy Paint przechowujący informację o parametrach rysowanego równania czy linii czyli jego kolorze, wielkości, rodzaju czcionki itd*/
    private final Paint equationParameters = new Paint();
    private final Paint lineParameters = new Paint();
     /**Zmienna przechowująca informację o aktualnym wyniku gracza*/
    private int scorePoints=0;
     /**Zmienna przechowująca równanie pobierane z klasy EquationGenerator do wyświetlenia na ekranie gry*/
    private String equation_text;
     /**Zmienna przechowująca pobraną z urządzenia szerokość ekranu*/
    private final float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
     /**Zmienna przechowująca pobraną z urządzenia wysokość ekranu*/
    private final float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
     /**Obraz ikony powrotu i jej przeskalowanie*/
    private final Bitmap pause = BitmapFactory.decodeResource(getResources(), R.drawable.powrot);
    private final Bitmap pauseScaled= Pineapple.scaleDown(pause, 100, true);
     /**Zmienna przechowująca informacje o dostępnych życiach w grze*/
    private int life=3;
     /**Obraz ikony pustego ananasa (pojawiającej się kiedy użytkownik staci życie) i jej przeskalowanie*/
    private final Bitmap lostLife = BitmapFactory.decodeResource(getResources(), R.drawable.straconezycie);
    private final Bitmap lostLifeScaled= scaling(lostLife, screenWidth/30, true);
     /**Obraz ikon żyć jako małe ananasy i ich przeskalowanie*/
    private final Bitmap pineappleAsLife= BitmapFactory.decodeResource(getResources(), R.drawable.ananasmini);
    private final Bitmap pineappleLife= scaling(pineappleAsLife, screenWidth/30, true);
     /**Zmienna przechowująca wynik równania matematycznego wygenerowanego w klasie EquationGenerator*/
    private int score;
     /**Zmienna która przechowuje wyniki losowania liczb które nie sa poprawnym rozwiązaniem równania*/
    private int ScoreEqualsScore;

    /**
     * Konstruktor klasy GameView
     * Ustawia parametry równania takie jak kolor, pogrubienie czy rozmiar
     * Ustawia parametry linii takie jak jej grubość oraz kolor
     * Tworzy nowy obiekt klasy MainThread
     */
    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        equationParameters.setColor(Color.BLACK);
        equationParameters.setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        equationParameters.setTextSize(80);
        equationParameters.setTextAlign(Paint.Align.CENTER);
        equationParameters.setTypeface(Typeface.MONOSPACE);
        lineParameters.setColor(Color.GREEN);
        lineParameters.setStrokeWidth(5);
        this.myContext = context;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    /**
     * Metoda ta tworzy nowe obiekty klasy Pineapple.
     * Wywołuje metodę odpowiedzialną za pozyskanie nowego równania.
     * Uruchamia wątek thread.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        pineapple = new Pineapple(BitmapFactory.decodeResource(getResources(), R.drawable.ananasmini), 1);
        pineapple1 = new Pineapple(BitmapFactory.decodeResource(getResources(), R.drawable.ananasmini), 2);
        pineapple2 = new Pineapple(BitmapFactory.decodeResource(getResources(), R.drawable.ananasmini), 3);
        pineapple3 = new Pineapple(BitmapFactory.decodeResource(getResources(), R.drawable.ananasmini), 4);
        equationScore();
        thread.setRunning(true);
        thread.start();
    }

    /**
     * Gdy widok jest niszczony, zatrzymywany jest wątek.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        while (retry) {
                try {
                    thread.setRunning(false);
                    thread.join();
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                retry = false;
            }
    }

    /**
     * W tej metodzie rysowany jest ekran gry, czyli równanie, wynik, życia użytkownika, linia, przycisk wyjścia oraz
     * wywoływane jest rysowanie ananasów lub zmiana ich pozycji na pozycje startowe (żeby spowrotem spadały z górnej części ekranu)
     * @param canvas płótno na którym rysowane są elementy gry
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if(pineapple.getEnd()){
            life--;
            if(life==0) openActivity();
            equationScore();
        }

        canvas.drawColor(Color.WHITE);
        canvas.drawText(equation_text, screenWidth/2, screenHeight / 15, equationParameters);
        canvas.drawText("Wynik= "+scorePoints, screenWidth/6, screenHeight / 15, equationParameters);
        canvas.drawLine(0, screenHeight / 12, getWidth(),screenHeight / 12, lineParameters);
        canvas.drawBitmap(pauseScaled, screenWidth/30*29-pauseScaled.getWidth(), screenHeight / 12-pauseScaled.getHeight(), null);

        if(life==3) {
            canvas.drawBitmap(pineappleLife, screenWidth / 30 * 26 - pineappleLife.getWidth(), screenHeight / 13 - pineappleLife.getHeight(), null);
            canvas.drawBitmap(pineappleLife, screenWidth / 30 * 25 - pineappleLife.getWidth(), screenHeight / 13 - pineappleLife.getHeight(), null);
            canvas.drawBitmap(pineappleLife, screenWidth / 30 * 24 - pineappleLife.getWidth(), screenHeight / 13 - pineappleLife.getHeight(), null);
        }else if(life==2) {
            canvas.drawBitmap(pineappleLife, screenWidth / 30 * 26 - pineappleLife.getWidth(), screenHeight / 13 - pineappleLife.getHeight(), null);
            canvas.drawBitmap(pineappleLife, screenWidth / 30 * 25 - pineappleLife.getWidth(), screenHeight / 13 - pineappleLife.getHeight(), null);
            canvas.drawBitmap(lostLifeScaled, screenWidth / 30 * 24 - pineappleLife.getWidth(), screenHeight / 13 - pineappleLife.getHeight(), null);
        }else if(life==1) {
            canvas.drawBitmap(pineappleLife, screenWidth / 30 * 26 - pineappleLife.getWidth(), screenHeight / 13 - pineappleLife.getHeight(), null);
            canvas.drawBitmap(lostLifeScaled, screenWidth / 30 * 25 - pineappleLife.getWidth(), screenHeight / 13 - pineappleLife.getHeight(), null);
            canvas.drawBitmap(lostLifeScaled, screenWidth / 30 * 24 - pineappleLife.getWidth(), screenHeight / 13 - pineappleLife.getHeight(), null);
        }else if(life==0) {
            canvas.drawBitmap(lostLifeScaled, screenWidth / 30 * 26 - pineappleLife.getWidth(), screenHeight / 13 - pineappleLife.getHeight(), null);
            canvas.drawBitmap(lostLifeScaled, screenWidth / 30 * 25 - pineappleLife.getWidth(), screenHeight / 13 - pineappleLife.getHeight(), null);
            canvas.drawBitmap(lostLifeScaled, screenWidth / 30 * 24 - pineappleLife.getWidth(), screenHeight / 13 - pineappleLife.getHeight(), null);
        }


        if (allowToDraw) {
            pineapple.draw(canvas);
            pineapple1.draw(canvas);
            pineapple2.draw(canvas);
            pineapple3.draw(canvas);
        } else {
            super.draw(canvas);
            canvas.drawColor(Color.WHITE);
            pineapple.changeY();
            pineapple1.changeY();
            pineapple2.changeY();
            pineapple3.changeY();
            allowToDraw = true;
        }
    }

    /**
     * Metoda ta aktualizuję pozycje ananasa w klasie Pineapple
     */
    public void update() {
        pineapple.update(scorePoints);
        pineapple1.update(scorePoints);
        pineapple2.update(scorePoints);
        pineapple3.update(scorePoints);
    }

    /**
     * Metoda ta obsługuje dotknięcie ekranu przez użytkownika
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (checkImagePosition(this.pineapple, x, y)) {
            if(randomResult==1){scorePoints++;}
            else {
                life--;
                if(life==0) openActivity();
            }

            equationScore();
            pineapple.pineappleCoordinates(1);
            allowToDraw = false;
        }

        if (checkImagePosition(this.pineapple1, x, y)) {
            if(randomResult==2){scorePoints++;}
            else {
                life--;
                if(life==0) openActivity();
            }

            equationScore();
            pineapple1.pineappleCoordinates(2);
            allowToDraw = false;
        }

        if (checkImagePosition(this.pineapple2, x, y)) {
            if(randomResult==3){ scorePoints++; }
            else {
                life--;
                if(life==0) openActivity();
            }

            equationScore();
            pineapple2.pineappleCoordinates(3);
            allowToDraw = false;
        }

        if (checkImagePosition(this.pineapple3, x, y)) {
            if(randomResult==4){scorePoints++;}
            else {
                life--;
                if(life==0) openActivity();
            }

            equationScore();
            pineapple3.pineappleCoordinates(4);
            allowToDraw = false;
        }

        if((x>=screenWidth/30*29-pauseScaled.getWidth() && x <=screenWidth) && (y>= screenHeight / 12-pauseScaled.getHeight()) && y <=screenHeight ) {
            surfaceDestroyed(getHolder());
            openActivity();
        }

        return super.onTouchEvent(event);
    }

    /**
     * Metoda ta otwiera nowy ekran z wynikiem końcowym gracza i przekazuje do tego ekranu wynik, który uzyskał użytkownik podczas gry
     */
    public void openActivity(){
        String s=Integer.toString(scorePoints);
        Intent intent2 = new Intent(myContext, ScoreActivity.class);
        intent2.putExtra("points", s);
        myContext.startActivity(intent2);
        ((Activity)myContext).finish();
    }

    /**
     * Metoda ta sprawdza czy dany ananas jest w tym samym miejscu co kliknięcie.
     * @param pineapple jest to ananas, który jest aktualnie przekazywany do tej metody.
     * @param posx pozycja x kliknięcia.
     * @param posy pozycja y kliknięcia.
     * @return zwraca true albo false
     */
    private boolean checkImagePosition(Pineapple pineapple, int posx, int posy) {
        int imageLeftStart = pineapple.getLeft();
        int imageLeftEnd = imageLeftStart + pineapple.getImgWidth();
        int imageTopStart = pineapple.getTop();
        int imageTopEnd = imageTopStart + pineapple.getImgHeight();
        return (posx >= imageLeftStart && posx <= imageLeftEnd) && (posy >= imageTopStart && posy <= imageTopEnd);
    }

    /**
     * Metoda ta pozyskuje nowe równanie oraz losuje wyniki poza prawidłowym, które mają zostać wyświetlone
     * na pozostałych ananasach
     */
    public void equationScore()
    {
        EquationGenerator equationGenerator = new EquationGenerator(scorePoints);
        equation_text = equationGenerator.getEquation();
        score= equationGenerator.getScore();
        String scoreS=Integer.toString(score);
        randomResult=randomNumber(1,4);

        if(randomResult==1) { pineapple.setEquationScore(scoreS); }
        else
        {
            ScoreEqualsScore=randomNumber(score/2, score+score/2);
            if(ScoreEqualsScore==score) { ScoreEqualsScore=randomNumber(score, score+100); }
            pineapple.setEquationScore(Integer.toString(ScoreEqualsScore));

        }

        if(randomResult==2) { pineapple1.setEquationScore(scoreS); }
        else
        {
            ScoreEqualsScore=randomNumber(score/2, score+score/2);
            if(ScoreEqualsScore==score) { ScoreEqualsScore=randomNumber(score, score+100); }
            pineapple1.setEquationScore(Integer.toString(ScoreEqualsScore));
        }

        if(randomResult==3) { pineapple2.setEquationScore(scoreS); }
        else
        {
            ScoreEqualsScore=randomNumber(score/2, score+score/2);
            if(ScoreEqualsScore==score) { ScoreEqualsScore=randomNumber(score, score+100); }
            pineapple2.setEquationScore(Integer.toString(ScoreEqualsScore));
        }

        if(randomResult==4) { pineapple3.setEquationScore(scoreS); }
        else
        {
            ScoreEqualsScore=randomNumber(score/2, score+score/2);
            if(ScoreEqualsScore==score) { ScoreEqualsScore=randomNumber(score, score+100); }
            pineapple3.setEquationScore(Integer.toString(ScoreEqualsScore));
        }
    }
    /**
     * Metoda generuje losową liczbę w zależności od podanego przedziału
     * @param minimumNumber dolna granica przedziału losowania
     * @param maximumNumber górna granica przedziału losowania
     * @return zwraca losowo wygenerowaną liczbę z podanego przedziału
     */
    private int randomNumber (int minimumNumber, int maximumNumber){
        return (new Random()).nextInt((maximumNumber-minimumNumber)+1)+minimumNumber;
    }

    /**
     * W tej metodzie skalowane są obrazy
     * @param realImage oznacza przekazywany do skalowania obraz
     * @param maxImageSize maksymalny rozmiar skalowanego obrazu
     * @param filter true oznacza lepszą jakość obrazu kosztem gorszej wydajności
     * @return zwraca przeskalowany obraz
     */
    public static Bitmap scaling(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());
        if (ratio >= 1.0){ return realImage;}
        return Bitmap.createScaledBitmap(realImage, width, height, filter);
    }
}
