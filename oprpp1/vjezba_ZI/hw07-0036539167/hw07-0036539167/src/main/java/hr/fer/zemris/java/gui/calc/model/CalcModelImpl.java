package hr.fer.zemris.java.gui.calc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

public class CalcModelImpl implements CalcModel{

    private boolean isEditable = true;
    private boolean isNegative = false;
    private String digits = "";
    private double number = 0;  //formed from String digits
    private String freezeValue;
    private double activeOperand;
    private boolean activeOperandSet = false;
    private DoubleBinaryOperator pendingOperation;
    private List<CalcValueListener> listeners = new ArrayList<>();

    private boolean inverted = false;

    /*Dodaj neš kaj pamti jel stisnuto inv ili ne kako bi mogao u CalcOperations
    * u unarnima provjerit koju će primijenit.*/


    /**
     * Prijava promatrača koje treba obavijestiti kada se
     * promijeni vrijednost pohranjena u kalkulatoru.
     *
     * @param l promatrač; ne smije biti <code>null</code>
     * @throws NullPointerException ako je za <code>l</code> predana vrijednost <code>null</code>
     */
    @Override
    public void addCalcValueListener(CalcValueListener l) {
        if (l == null) {
            throw new NullPointerException("Listener cannot be null.");
        }
        listeners.add(l);
    }

    /**
     * Odjava promatrača s popisa promatrača koje treba
     * obavijestiti kada se promijeni vrijednost
     * pohranjena u kalkulatoru.
     *
     * @param l promatrač; ne smije biti <code>null</code>
     * @throws NullPointerException ako je za <code>l</code> predana vrijednost <code>null</code>
     */
    @Override
    public void removeCalcValueListener(CalcValueListener l) {
        if (l == null) {
            throw new NullPointerException("Listener cannot be null.");
        }
        listeners.remove(l);
    }

    /**
     * Vraća trenutnu vrijednost koja je pohranjena u kalkulatoru.
     *
     * @return vrijednost pohranjena u kalkulatoru
     */
    @Override
    public double getValue() {
        return number;
    }

    /**
     * Upisuje decimalnu vrijednost u kalkulator. Vrijednost smije
     * biti i beskonačno odnosno NaN. Po upisu kalkulator
     * postaje needitabilan.
     *
     * @param value vrijednost koju treba upisati
     */
    @Override
    public void setValue(double value) {
        this.number = value;
        this.digits = String.valueOf(value);
        isEditable = false;
        this.freezeValue = null;
        notifyListeners();
    }

    /**
     * Vraća informaciju je li kalkulator editabilan (drugim riječima,
     * smije li korisnik pozivati metode {@link #swapSign()},
     * {@link #insertDecimalPoint()} te {@link #insertDigit(int)}).
     *
     * @return <code>true</code> ako je model editabilan, <code>false</code> inače
     */
    @Override
    public boolean isEditable() {
        return isEditable;
    }

    /**
     * Resetira trenutnu vrijednost na neunesenu i vraća kalkulator u
     * editabilno stanje.
     */
    @Override
    public void clear() {
        digits = "";
        isEditable = true;
        isNegative = false;
        notifyListeners();
    }

    /**
     * Obavlja sve što i {@link #clear()}, te dodatno uklanja aktivni
     * operand i zakazanu operaciju.
     */
    @Override
    public void clearAll() {
        digits = "";
        isEditable = true;
        activeOperandSet = false;
        pendingOperation = null;
        isNegative = false;
        freezeValue = null;

        notifyListeners();
    }

    /**
     * Mijenja predznak unesenog broja.
     *
     * @throws CalculatorInputException ako kalkulator nije editabilan
     */
    @Override
    public void swapSign() throws CalculatorInputException {
        if(!isEditable) throw new CalculatorInputException("Cannot swap sign, currently not editable.");
        freezeValue = null;
        number = number*-1.0;
        if(isNegative){
            isNegative = false;
            digits = digits.replaceAll("-", "");
        }else {
            isNegative = true;
            digits = "-" + digits;
        }

        notifyListeners();
    }

    /**
     * Dodaje na kraj trenutnog broja decimalnu točku.
     *
     * @throws CalculatorInputException ako nije još unesena niti jedna znamenka broja,
     *                                  ako broj već sadrži decimalnu točku ili ako kalkulator nije editabilan
     */
    @Override
    public void insertDecimalPoint() throws CalculatorInputException {
        if(!isEditable) throw new CalculatorInputException("Cannot insert decimal point, currently not editable.");
        if(digits.isEmpty() || digits.contains(".") || digits.equals("-")) throw new CalculatorInputException("Cannot insert decimal point");
        freezeValue = null;

        digits = digits + ".";

        notifyListeners();
    }

    /**
     * U broj koji se trenutno upisuje na kraj dodaje poslanu znamenku.
     * Ako je trenutni broj "0", dodavanje još jedne nule se potiho
     * ignorira.
     *
     * @param digit znamenka koju treba dodati
     * @throws CalculatorInputException ako bi dodavanjem predane znamenke broj postao prevelik za konačan prikaz u tipu {@link Double}, ili ako kalkulator nije editabilan.
     * @throws IllegalArgumentException ako je <code>digit &lt; 0</code> ili <code>digit &gt; 9</code>
     */
    @Override
    public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
        if(!isEditable) throw new CalculatorInputException("Cannot insert, model is not editable.");
        if(digit<0 || digit>9) throw new IllegalArgumentException("Not a single digit.");
        freezeValue = null;
        if(digits.equals("0") && digit == 0){
            return;
        }
        if(digits.equals("0") && digit!= 0){
            digits = "";
        }
        String tmpDigits = digits;
        tmpDigits += String.valueOf(digit);
        try{
            if(Double.parseDouble(tmpDigits) > Double.MAX_VALUE) throw new CalculatorInputException();
            number = Double.parseDouble(tmpDigits);
            digits = tmpDigits;
        }catch(NumberFormatException nfe){
            throw new CalculatorInputException("Cannot parse into finite number.");
        }

        notifyListeners();
    }

    /**
     * Provjera je li upisan aktivni operand.
     *
     * @return <code>true</code> ako je aktivani operand upisan, <code>false</code> inače
     */
    @Override
    public boolean isActiveOperandSet() {
        return activeOperandSet;
    }

    /**
     * Dohvat aktivnog operanda.
     *
     * @return aktivni operand
     * @throws IllegalStateException ako aktivni operand nije postavljen
     */
    @Override
    public double getActiveOperand() throws IllegalStateException {
        if(!activeOperandSet) throw new IllegalStateException("Active operand is not set.");
        return activeOperand;
    }

    /**
     * Metoda postavlja aktivni operand na predanu vrijednost.
     * Ako kalkulator već ima postavljen aktivni operand, predana
     * vrijednost ga nadjačava.
     *
     * @param activeOperand vrijednost koju treba pohraniti kao aktivni operand
     */
    @Override
    public void setActiveOperand(double activeOperand) {
        this.activeOperand = activeOperand;
        activeOperandSet = true;

        notifyListeners();
    }

    /**
     * Uklanjanje zapisanog aktivnog operanda.
     */
    @Override
    public void clearActiveOperand() {
        activeOperandSet = false;
        notifyListeners();
    }

    /**
     * Dohvat zakazane operacije.
     *
     * @return zakazanu operaciju, ili <code>null</code> ako nema zakazane operacije
     */
    @Override
    public DoubleBinaryOperator getPendingBinaryOperation() {
        return pendingOperation;
    }

    /**
     * Postavljanje zakazane operacije. Ako zakazana operacija već
     * postoji, ovaj je poziv nadjačava predanom vrijednošću.
     *
     * @param op zakazana operacija koju treba postaviti; smije biti <code>null</code>
     */
    @Override
    public void setPendingBinaryOperation(DoubleBinaryOperator op) {
        pendingOperation = op;
        notifyListeners();
    }


    public void freezeValue(String value) {
        this.freezeValue = value;
        notifyListeners();
    }

    boolean hasFrozenValue(){
        return this.freezeValue != null;
    }

    @Override
    public String toString() {
        if(hasFrozenValue()){
            return clean(freezeValue);
        }else if(digits.isEmpty() || digits.equals("-")){
            return isNegative ? "-0" : "0";
        }else{
            return clean(digits);
        }
    }


    private void notifyListeners(){
        for(CalcValueListener l: listeners){
            l.valueChanged(this);
        }
    }

    /**
     * Cleans the given string by attempting to parse it as a double and returning the integer value if it is an integer.
     *
     * @param digits the string to clean
     * @return the cleaned string
     */
    private String clean(String digits){
        if (digits == null || digits.isEmpty()) {
            return digits;
        }

        double number;
        try {
            number = Double.parseDouble(digits);
        } catch (NumberFormatException e) {
            return digits;
        }

        if (number == (int) number) {
            return String.valueOf((int) number);
        }

        return digits;
    }

    public boolean isInverted() {
        return inverted;
    }
    public void swapInvert(){
        inverted = !inverted;
    }
}
