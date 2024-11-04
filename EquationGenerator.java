package com.example.testyananas5;

import java.util.Random;

/**
 * Klasa odpowiedzialna za generowanie równania matematycznego.
 * Wraz z rozwojem gry można dodawać nowe sposoby generowania obliczeniowych zagadnień matematycznych.
 * @author Wiktor Milecki
 */
public class EquationGenerator {
   /**Wynik liczbowy równania*/
    private int score;
    /**Przedział początkowy pierwszej liczby generowanej do równania (w trakcie gry zwiększany jest przez zdobyte punkty) */
    private final int firstNumberMultiplier=50;
    /**Przedział początkowy drugiej liczby generowanej do równania (w trakcie gry zwiększany jest przez zdobyte punkty) */
    private final int secondNumberMultiplier=30;
    /**Równanie, które użytkownik musi rozwiązać przechowywane w formie tekstowej*/
    private String equation;
    /**Pierwsza cyfra wykorzystana do obliczeń*/
    private final int firstNumber;
    /**Druga cyfra wykorzystana do obliczeń*/
    private final int secondNumber;

    /**
     * Konstruktor klasy EquationGenerator w zależności od przekazanych punktów zwiększana jest trudność rozwiązywanych równań
     * @param punkty przechowuje punkty uzyskane przez użytkownika
     */
    public EquationGenerator(int punkty)
    {
        if(punkty>50)
        {
            firstNumber = new Random().nextInt(firstNumberMultiplier+punkty);
            secondNumber = new Random().nextInt(secondNumberMultiplier+punkty);
        }
        else {
            firstNumber = new Random().nextInt(firstNumberMultiplier+punkty*2);
            secondNumber = new Random().nextInt(secondNumberMultiplier+punkty*2);
        }
        int equationType= new Random().nextInt(3);
        equationType++;
        switch (equationType)
        {
            case(1):adding(firstNumber, secondNumber);break;
            case(2):subtracting(firstNumber/3+1, secondNumber/3+1);break;
            case(3):multiplication(firstNumber/10+1, secondNumber/10+1);break;
        }
    }

    /**
     * Metoda odpowiedzialna za generowanie równania z dodawaniem liczb
     * @param firstNumber pierwsza cyfra wykorzystana do dodawania
     * @param secondNumber druga cyfra wykorzystana do dodawania
     */
    public void adding(int firstNumber, int secondNumber){
        equation = firstNumber + " + " + secondNumber + " =";
        score=firstNumber+secondNumber;
    }

    /**
     * Metoda odpowiedzialna za generowanie równania z odejmowaniem liczb
     * @param firstNumber pierwsza cyfra wykorzystana do odejmowania
     * @param secondNumber druga cyfra wykorzystana do odejmowania
     */
    public void subtracting(int firstNumber, int secondNumber){
        firstNumber++;
        equation = firstNumber+secondNumber + " - " + secondNumber + " =";
        score=firstNumber+secondNumber-secondNumber;
    }

    /**
     * Metoda odpowiedzialna za generowanie równania z mnożeniem liczb
     * @param firstNumber pierwsza cyfra wykorzystana do mnożenia
     * @param secondNumber druga cyfra wykorzystana do mnożenia
     */
    public void multiplication(int firstNumber, int secondNumber){
        equation = firstNumber + " * " + secondNumber + " =";
        score=firstNumber*secondNumber;
    }

    /**
     * Getter zwracający równanie w postaci tekstu do wyświetlenia na canvasie
     * @return zwraca równanie matematyczne w postaci tekstu
     */
    public String getEquation() {
        return this.equation;
    }

    /**
     * Metoda ta zwraca wynik równania w postaci liczbowej
     * @return zwraca zmienną score
     */
    public int getScore(){
        return this.score;
    }
}
