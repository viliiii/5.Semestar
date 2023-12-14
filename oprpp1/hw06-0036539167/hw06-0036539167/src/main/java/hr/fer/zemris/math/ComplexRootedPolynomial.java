package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class ComplexRootedPolynomial {
    private final Complex constant;
    private ArrayList<Complex> roots;

    public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
        if(roots == null) throw new IllegalArgumentException("roots can not be null");
        this.constant = constant;
        this.roots = new ArrayList<>();
        this.roots.addAll(Arrays.asList(roots));
    }

    public Complex apply(Complex z) {
        Complex result = constant;
        for(Complex r : roots){
            result = result.multiply(z.sub(r));
        }
        return result;
    }

    // converts this representation to ComplexPolynomial type
    public ComplexPolynomial toComplexPolynom() {
        ArrayList<Complex> factors = new ArrayList<Complex>();  //bit će ih roots.size() + 1
        factors.add(constant);
        for(int i=1; i<roots.size()+1; i++) {
            HashSet<HashSet<Integer>> combinations = new HashSet<>();
            calculateCombinations(roots.toArray(new Complex[0]), i, combinations, new HashSet<>(), 0);
            Complex sum = Complex.ZERO;
            for(HashSet<Integer> combination: combinations){
                Complex multiplication = Complex.ONE;
                for(int index: combination){
                    multiplication = multiplication.multiply(roots.get(index));
                }
                sum = sum.add(multiplication);
            }
            if(i%2 != 0) sum = sum.negate();
            factors.add(sum.multiply(constant));
        }

        return new ComplexPolynomial(factors.reversed().toArray(new Complex[0]));

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(constant.toString()).append(")");
        for(Complex r: roots){
            sb.append("*(z-").append("(").append(r.toString()).append(")");
        }
        ComplexPolynomial.deleteMinusBeforeZero(sb);

        return sb.toString();
    }

    // finds index of closest root for given complex number z that is within
    // treshold; if there is no such root, returns -1
    // first root has index 0, second index 1, etc
    public int indexOfClosestRootFor(Complex z, double treshold) {
        double min = Double.MAX_VALUE;
        int ind = -1;
        for(int i=0; i<roots.size(); i++) {
            double distance = z.sub(roots.get(i)).module();
            if(Math.abs(distance - treshold) < min) {
                min = distance;
                ind = i;
            }
        }
        return ind;
    }



    /**
     Recursively calculates all unique combinations of indices from the given array.*
     This method generates combinations of indices in a recursive manner to form all possible combinations
     with a specified number of elements. It ensures that each combination is unique and avoids duplications.
     *
     @param array             The input array from which combinations are generated.
     @param a                 The number of elements in each combination.
     @param combinations      The set to store unique combinations.
     @param currentCombination The current combination being formed during recursion.
     @param startIndex        The starting index for generating combinations.
     */
    private static void calculateCombinations(Complex[] array, int a, HashSet<HashSet<Integer>> combinations,
                                             HashSet<Integer> currentCombination, int startIndex) {
        if (currentCombination.size() == a) {
            combinations.add(new HashSet<>(currentCombination));
            return;
        }

        for (int i = startIndex; i < array.length; i++) {
            currentCombination.add(i);
            calculateCombinations(array, a, combinations, currentCombination, i + 1);
            currentCombination.remove(i);
        }
    }

}
