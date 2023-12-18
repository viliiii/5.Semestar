package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing complex number with its real and imaginary parts.
 */
public class Complex {
    private final double re;
    private final double im;
    public static final Complex ZERO = new Complex(0,0);
    public static final Complex ONE = new Complex(1,0);
    public static final Complex ONE_NEG = new Complex(-1,0);
    public static final Complex IM = new Complex(0,1);
    public static final Complex IM_NEG = new Complex(0,-1);


    /**
     * Creates a new complex number with default values: 0.
     */
    public Complex() {
        this.re = 0;
        this.im = 0;
    }

    /**
     * Creates a new complex number with the specified real and imaginary parts.
     *
     * @param re the real part
     * @param im the imaginary part
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * Returns the module of the complex number.
     * @return the module of the complex number
     */
    public double module(){
        return Math.sqrt(Math.pow(re, 2) + Math.pow(im, 2));
    }


    /**
     * Returns the result of multiplying this complex number by the specified complex number.
     *
     * @param c the complex number to multiply with
     * @return the result of multiplying this complex number by the specified complex number
     */
    public Complex multiply(Complex c){
        return new Complex(re*c.re - im*c.im, re*c.im + im*c.re);
    }

    /**
     * Returns the result of dividing this complex number by the specified complex number.
     *
     * @param c the complex number to divide with
     * @return the result of dividing this complex number by the specified complex number
     */
    public Complex divide(Complex c) {
        return new Complex((re*c.re + im*c.im)/(Math.pow(c.re, 2) + Math.pow(c.im, 2)), (-re*c.im + im*c.re)/(Math.pow(c.re, 2) + Math.pow(c.im, 2)));
    }

    /**
     * Adds two complex numbers together and returns the result as a new complex number.
     *
     * @param c the complex number to add to this complex number
     * @return the sum of this complex number and the specified complex number
     */
    public Complex add(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }

    /**
     * Returns the result of subtracting the specified complex number from this complex number.
     *
     * @param c the complex number to subtract from this complex number
     * @return the result of subtracting the specified complex number from this complex number
     */
    public Complex sub(Complex c) {
        return new Complex(re - c.re, im - c.im);
    }

    /**
     * Returns the negated complex number.
     *
     * @return the negated complex number
     */
    public Complex negate() {
        return new Complex(-re, -im);
    }


    /**
     * Returns the complex number raised to the specified power.
     *
     * @param n the power to raise the complex number to
     * @return the complex number raised to the specified power
     * @throws IllegalArgumentException if the specified power is negative
     */
    public Complex power(int n) {
        double angle = Math.atan2(im, re);
        double r = module();
        return new Complex(Math.pow(r, n) * Math.cos(n*angle), Math.pow(r, n) * Math.sin(n*angle));
    }

    /**
     * Returns the n-th root of this complex number.
     * @param n the positive integer specifying the root to return
     * @return the n-th root of this complex number as a list of complex numbers, roots
     * @throws IllegalArgumentException if the specified power is negative
     */
    // returns n-th root of this, n is positive integer
    public List<Complex> root(int n) {
        if (n <= 0){
            throw new IllegalArgumentException("Invalid power");
        }
        double r = Math.pow(this.module(), 1.0/n);
        double angle = Math.atan2(im, re);
        List<Complex> result = new ArrayList<>();
        for (int k = 0; k < n; k++) {
            double rootAngle = (angle + 2*k*Math.PI) /n;
            result.add(new Complex(r*Math.cos(rootAngle), r*Math.sin(rootAngle)));
        }
        return result;
    }

    /**
     * Returns a string representation of the complex number in the form a + bi, where a and b are the real and imaginary parts of the complex number, respectively.
     *
     * @return a string representation of the complex number in the form a + bi, where a and b are the real and imaginary parts of the complex number, respectively.
     */
    @Override
    public String toString() {
        return re + " + " + im  + "i";
    }
}
