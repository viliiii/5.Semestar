package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.Arrays;

public class ComplexPolynomial {
    private final ArrayList<Complex> factors; //[0]*z^3+[1]*z^2+...

    public ComplexPolynomial(Complex ...factors) {
        this.factors = new ArrayList<>();
        this.factors.addAll(Arrays.asList(factors).reversed());
    }

    public short order() {
        return (short) (factors.size() - 1);
    }

    public ComplexPolynomial multiply(ComplexPolynomial p) {
        int resultSize = order() + p.order() + 1;
        ArrayList<Complex> resultFactors = new ArrayList<Complex>(Arrays.asList(new Complex[resultSize]));  //kako bi svi bili inicijalno null

        for (int i=0; i< this.factors.size(); i++) {
            for(int j=0; j< p.factors.size(); j++) {
                if (resultFactors.get(i + j) == null) {
                    resultFactors.set(i + j, this.factors.get(i).multiply(p.factors.get(j)));
                } else {
                    resultFactors.set(i + j, resultFactors.get(i + j).add(this.factors.get(i).multiply(p.factors.get(j))));
                }
            }
        }

        return new ComplexPolynomial(resultFactors.toArray(new Complex[0]));
    }


    public ComplexPolynomial derive() {
        ArrayList<Complex> newFactors = new ArrayList<>();
        for(int i = 0; i < factors.size()-1; i++) {
            newFactors.add(factors.get(i).multiply(new Complex(order()-i, 0)));
        }
        Complex[] arr = new Complex[newFactors.size()];
        return new ComplexPolynomial(newFactors.reversed().toArray(arr));
    }


    public Complex apply(Complex z) {
        Complex result = Complex.ZERO;
        for (int i = 0; i < factors.size(); i++) {
            result = result.add(factors.get(i).multiply(z.power(order()-i)));
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<=order(); i++) {
            if(i==order()) {
                sb.append("(").append(factors.get(i).toString()).append(")");
                break;
            }
            sb.append("(").append(factors.get(i).toString()).append(")");
            sb.append("*z^").append(order()-i).append("+");
        }
        deleteMinusBeforeZero(sb);
        return sb.toString();
    }

    public ArrayList<Complex> getFactors() {
        return new ArrayList<>(factors);
    }

    public static void deleteMinusBeforeZero(StringBuilder sb) {
        int i = 0;
        while (i < sb.length()) {
            if (sb.charAt(i) == '-' && i + 1 < sb.length() && sb.charAt(i + 1) == '0') {
                sb.deleteCharAt(i);
            } else {
                i++;
            }
        }
    }
}
