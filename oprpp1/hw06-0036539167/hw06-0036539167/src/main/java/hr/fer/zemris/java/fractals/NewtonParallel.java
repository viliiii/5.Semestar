package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class NewtonParallel {
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
            roots.add(root);
        }
        System.out.println("Image of fractal will appear shortly. Thank you.\n");
        int[] tracks_workers = parseArgs(args);
        System.out.println("Broj dretvi: " + tracks_workers[1]);
        System.out.println("Broj poslova: " + tracks_workers[0]);
        FractalViewer.show(new NewtonParallel.MojProducer(new ComplexRootedPolynomial(new Complex(1, 0), roots.toArray(new Complex[0])), tracks_workers[0], tracks_workers[1]));
    }

    public static int[] parseArgs(String[] args) {
        int numOfTracks = -1;
        int numOfWorkers = -1;
        if(args.length == 0) {
            return new int[]{Runtime.getRuntime().availableProcessors()*4, Runtime.getRuntime().availableProcessors()};
        }

        StringBuilder input = new StringBuilder();
        for(String arg:args){
            input.append(arg.trim());
        }

        for(int i=0; i<input.length(); i++) {

            try {
                if(input.substring(i, i+10).equals("--workers=")){
                    if(numOfWorkers == -1){
                        StringBuilder number = new StringBuilder();
                        int numInd = i+10;
                        while (Character.isDigit(input.charAt(numInd))){
                            number.append(input.charAt(numInd));
                            numInd++;
                        }
                        numOfWorkers = Integer.parseInt(number.toString());
                        i = i+8;
                    }else {
                        throw new IllegalArgumentException("Workers specified twice.");
                    }
                }

                if(input.substring(i, i+9).equals("--tracks=")){
                    if(numOfTracks == -1){
                        StringBuilder number = new StringBuilder();
                        int numInd = i+9;
                        while (Character.isDigit(input.charAt(numInd))){
                            number.append(input.charAt(numInd));
                            numInd++;
                        }
                        numOfTracks = Integer.parseInt(number.toString());
                        i = i+8;
                    }else {
                        throw new IllegalArgumentException("Tracks specified twice.");
                    }
                }

                if(input.substring(i, i+2).equals("-t")){
                    if(numOfTracks == -1){
                        StringBuilder number = new StringBuilder();
                        int numInd = i+2;
                        while (Character.isDigit(input.charAt(numInd))){
                            number.append(input.charAt(numInd));
                            numInd++;
                        }
                        numOfTracks = Integer.parseInt(number.toString());
                        i = i+1;
                    }else {
                        throw new IllegalArgumentException("Tracks specified twice.");
                    }
                }

                if(input.substring(i, i+2).equals("-w")){
                    if(numOfWorkers == -1){
                        StringBuilder number = new StringBuilder();
                        int numInd = i+2;
                        while (Character.isDigit(input.charAt(numInd))){
                            number.append(input.charAt(numInd));
                            numInd++;
                        }
                        numOfWorkers = Integer.parseInt(number.toString());
                        i = i+1;
                    }else {
                        throw new IllegalArgumentException("Workers specified twice.");
                    }
                }
            }catch (StringIndexOutOfBoundsException e) {
                break;
            }

        }

        int[] tracks_workers = new int[2];
        tracks_workers[0] = numOfTracks == -1 ? Runtime.getRuntime().availableProcessors() : numOfTracks;
        tracks_workers[1] = numOfWorkers == -1 ? Runtime.getRuntime().availableProcessors() : numOfWorkers;

        return tracks_workers;
    }

    public static class PosaoIzracuna implements Runnable {
        double reMin;
        double reMax;
        double imMin;
        double imMax;
        int width;
        int height;
        int yMin;
        int yMax;
        int m;
        short[] data;
        AtomicBoolean cancel;
        private ComplexRootedPolynomial rooted;
        private ComplexPolynomial powered;
        private ComplexPolynomial derived;
        public static PosaoIzracuna NO_JOB = new PosaoIzracuna();

        private PosaoIzracuna() {
        }

        public PosaoIzracuna(double reMin, double reMax, double imMin,
                             double imMax, int width, int height, int yMin, int yMax,
                             int m, short[] data, AtomicBoolean cancel, ComplexRootedPolynomial rooted,
                             ComplexPolynomial powered,
                             ComplexPolynomial derived) {
            super();
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.yMin = yMin;
            this.yMax = yMax;
            this.m = m;
            this.data = data;
            this.cancel = cancel;
            this.rooted = rooted;
            this.powered = powered;
            this.derived = derived;
        }

        @Override
        public void run() {
            int offset = yMin * width;
            for(int y = yMin; y <= yMax && !cancel.get(); ++y) {
                if(cancel.get()) break;
                for(int x = 0; x < width; ++x) {
                    double cre = x / (width-1.0) * (reMax - reMin) + reMin;
                    double cim = (height-1.0-y) / (height-1) * (imMax - imMin) + imMin;
                    Complex zn = new Complex(cre, cim);
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
                    } while(iters < m && module > 0.001);
                    int index = rooted.indexOfClosestRootFor(zn, 0.002);
                    data[offset] = (short)(index+1);
                    ++offset;
                }
            }
        }
    }

    public static class MojProducer implements IFractalProducer {

        private ComplexRootedPolynomial rooted;
        private ComplexPolynomial powered;
        private ComplexPolynomial derived;
        private int numOfLines;
        private int numOfWorkers;

        public MojProducer(ComplexRootedPolynomial rooted, int numOfLines, int numOfWorkers) {
            this.rooted = rooted;
            this.numOfLines = numOfLines;
            this.numOfWorkers = numOfWorkers;
            powered = rooted.toComplexPolynom();
            derived = powered.derive();
        }


        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            System.out.println("Zapocinjem izracun...");
            int m = 16*16;
            short[] data = new short[width * height];
            final int brojTraka = numOfLines;
            int brojYPoTraci = height / brojTraka;

            final BlockingQueue<PosaoIzracuna> queue = new LinkedBlockingQueue<>();

            Thread[] radnici = new Thread[numOfWorkers];
            for(int i = 0; i < radnici.length; i++) {
                radnici[i] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(true) {
                            PosaoIzracuna p = null;
                            try {
                                p = queue.take();
                                if(p==PosaoIzracuna.NO_JOB) break;
                            } catch (InterruptedException e) {
                                continue;
                            }
                            p.run();
                        }
                    }
                });
            }
            for(int i = 0; i < radnici.length; i++) {
                radnici[i].start();
            }

            for(int i = 0; i < brojTraka; i++) {
                int yMin = i*brojYPoTraci;
                int yMax = (i+1)*brojYPoTraci-1;
                if(i==brojTraka-1) {
                    yMax = height-1;
                }
                PosaoIzracuna posao = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, cancel, rooted, powered, derived);
                while(true) {
                    try {
                        queue.put(posao);
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }
            for(int i = 0; i < radnici.length; i++) {
                while(true) {
                    try {
                        queue.put(PosaoIzracuna.NO_JOB);
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }

            for(int i = 0; i < radnici.length; i++) {
                while(true) {
                    try {
                        radnici[i].join();
                        break;
                    } catch (InterruptedException e) {
                    }
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

        if(line.startsWith("i") ||line.endsWith("i")){
            return new Complex(0, parseIm(line));
        }else {
            return new Complex(Double.parseDouble(line), 0);
        }

    }

    static double parseIm(String imaginaryPart){
        imaginaryPart = imaginaryPart.replaceAll("i", "");
        if(imaginaryPart.equals("-")) return -1.0;
        return imaginaryPart.isEmpty() ? 1 : Double.parseDouble(imaginaryPart);
    }
}
