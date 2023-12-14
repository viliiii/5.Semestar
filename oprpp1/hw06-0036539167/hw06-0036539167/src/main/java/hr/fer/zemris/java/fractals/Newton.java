package hr.fer.zemris.java.fractals;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Newton {

    public static void main(String[] args) {
        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.\n" +
                "Please enter at least two roots, one root per line. Enter 'done' when done.");
        int rootNum = 1;
        ArrayList<Complex> roots = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.print("Root " + rootNum++ + "> ");
            String line = scanner.nextLine();
            if(line.equals("done"))break;
            Complex root = parseComplex(line);
            //System.out.println(root);
            roots.add(root);
        }
        System.out.println("Image of fractal will appear shortly. Thank you.\n");

        FractalViewer.show(new MojProducer(new ComplexRootedPolynomial(new Complex(1, 0), roots.toArray(new Complex[0]))));
        /*ComplexRootedPolynomial cp = new ComplexRootedPolynomial(new Complex(1, 0), roots.toArray(new Complex[0]));
        System.out.println(cp.toComplexPolynom());*/
    }

    public static class MojProducer implements IFractalProducer {

        private ComplexRootedPolynomial rooted;
        private ComplexPolynomial powered;
        private ComplexPolynomial derived;

        public MojProducer(ComplexRootedPolynomial rooted) {
            this.rooted = rooted;
            powered = rooted.toComplexPolynom();
            derived = powered.derive();
        }


        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            System.out.println("Zapocinjem izracun...");
            int m = 16*16;
            int offset = 0;
            short[] data = new short[width * height];
            for(int y = 0; y < height; y++) {
                if(cancel.get()) break;
                for(int x = 0; x < width; x++) {
                    double cre = x / (width-1.0) * (reMax - reMin) + reMin;
                    double cim = (height-1.0-y) / (height-1) * (imMax - imMin) + imMin;
                    Complex c = new Complex(cre, cim);
                    double zre = 0;
                    double zim = 0;
                    Complex zn = c;
                    double module = 0;
                    int iters = 0;
                    do {
                        Complex numerator = powered.apply(zn);
                        Complex denominator = derived.apply(zn);
                        Complex znold = zn;
                        Complex fraction = numerator.divide(denominator);
                        zn = zn.sub(fraction);
                        module = znold.sub(zn).module();

                        iters++;
                    } while(iters < m && module < 0.001);
                    int index = rooted.indexOfClosestRootFor(zn, 0.002);
                    //System.out.println(index);
                    data[offset++] = (short)(index+1);
                }
            }
            System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
            observer.acceptResult(data, (short)(powered.order() + 1), requestNo);
        }
    }

    static Complex parseComplex(String line){
        line = line.trim();
        line = line.replaceAll("\\s", "");
        double re = 0;
        double im = 0;

        if(line.equals("i")) return new Complex(0, 1);

        int signIndex = line.lastIndexOf("+");
        if(signIndex == -1){
            signIndex = line.lastIndexOf("-");
        }

        if(signIndex != -1 && signIndex != 0){
            re = Double.parseDouble(line.substring(0, signIndex));
            im = parseIm(line.substring(signIndex));

            return new Complex(re, im);
        }

        if(signIndex == 0){
            if(line.contains("i")){
               return new Complex(0, parseIm(line));
            }else {
                return new Complex(Double.parseDouble(line), 0);
            }
        }

        if(line.startsWith("i")){
            return new Complex(0, parseIm(line));
        }else {
            return new Complex(Double.parseDouble(line), 0);
        }

    }

    static double parseIm(String imaginaryPart){
        imaginaryPart = imaginaryPart.replaceAll("i", "");
        return imaginaryPart.isEmpty() ? 1 : Double.parseDouble(imaginaryPart);
    }


}


