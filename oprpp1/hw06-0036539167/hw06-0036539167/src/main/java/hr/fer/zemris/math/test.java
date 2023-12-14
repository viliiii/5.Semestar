package hr.fer.zemris.math;

public class test {
    public static void main(String[] args) {
        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
              Complex.ONE, Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG
       );
        System.out.println(crp.indexOfClosestRootFor(new Complex(1, 1), 0.002));
        //System.out.println(crp);
        //System.out.println(cp);
        //System.out.println(cp.derive());

        /*ComplexPolynomial cp = new ComplexPolynomial(new Complex(4, 5), new Complex(3, 1), new Complex(0, 1));
        System.out.println(cp);
        System.out.println(cp.apply(new Complex(4, 5)));*/

    }
}
