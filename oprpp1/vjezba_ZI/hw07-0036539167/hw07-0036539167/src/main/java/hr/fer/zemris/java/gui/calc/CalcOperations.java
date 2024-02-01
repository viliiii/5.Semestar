package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;

public class CalcOperations {
    public static CalcOperation PLUS = new CalcOperation() {
        @Override
        public void apply(CalcModel model) {
            binaryOp(model);
            model.setPendingBinaryOperation(Double::sum);
            model.freezeValue(String.valueOf(model.getValue()));
            model.clear();
        }
    };

    public static CalcOperation MINUS = new CalcOperation() {
        @Override
        public void apply(CalcModel model) {
            binaryOp(model);
            model.setPendingBinaryOperation((a, b) -> a-b);
            model.freezeValue(String.valueOf(model.getValue()));
            model.clear();
        }
    };

    public static CalcOperation MULTIPLY = new CalcOperation() {
        @Override
        public void apply(CalcModel model) {
            binaryOp(model);
            model.setPendingBinaryOperation((a, b) -> a*b);
            model.freezeValue(String.valueOf(model.getValue()));
            model.clear();
        }
    };

    private static void binaryOp(CalcModel model) {
        if(model.isActiveOperandSet()){
            double operand = model.getActiveOperand();
            double val = model.getValue();
            model.setValue(model.getPendingBinaryOperation().applyAsDouble(operand,val));
            model.clearActiveOperand();
            model.setPendingBinaryOperation(null);
        }
        model.setActiveOperand(model.getValue());
    }

    public static CalcOperation DIVIDE = (model -> {
        binaryOp(model);
        model.setPendingBinaryOperation((a, b) -> a/b);
        model.freezeValue(String.valueOf(model.getValue()));
        model.clear();
    });

    public static CalcOperation POWER = (model -> {

        if(model.isInverted()){
            CalcOperations.POWER1N.apply(model);
        }else{
            binaryOp(model);
            model.setPendingBinaryOperation(Math::pow);
            model.freezeValue(String.valueOf(model.getValue()));
            model.clear();
        }
    });

    //ne radi? provjeri zašto, stvarno nemrem skužit zašto...
    public static CalcOperation POWER1N = (model -> {
        binaryOp(model);
        model.setPendingBinaryOperation((a,b)->Math.pow(a, 1.0/b));
        model.freezeValue(String.valueOf(model.getValue()));
        model.clear();
    });

    public static CalcOperation CLEAR = (CalcModel::clear);


    public static CalcOperation RESET = (CalcModel::clearAll);

    public static CalcOperation SIN = (model -> {
        if(model.isInverted()){
            CalcOperations.ARCSIN.apply(model);
        }else {
            model.setValue(Math.sin(model.getValue()));
        }

    });

    public static CalcOperation ARCSIN = (model -> {
        model.setValue(Math.asin(model.getValue()));
    });

    public static CalcOperation COS = (model -> {
        if(model.isInverted()){
            CalcOperations.ARCOS.apply(model);
        }else{
            model.setValue(Math.cos(model.getValue()));
        }

    });

    public static CalcOperation ARCOS = (model -> {
        model.setValue(Math.acos(model.getValue()));
    });

    public static CalcOperation TAN = (model -> {
        if(model.isInverted()){
            CalcOperations.ARCTAN.apply(model);
        }else{
            model.setValue(Math.tan(model.getValue()));
        }

    });

    public static CalcOperation ARCTAN = (model -> {
        model.setValue(Math.atan(model.getValue()));
    });

    public static CalcOperation CTG = (model -> {

        if(model.isInverted()){
            CalcOperations.ARCCTG.apply(model);
        }else{
            model.setValue(1.0/Math.atan(model.getValue()));
        }
    });

    public static CalcOperation ARCCTG = (model -> {
        model.setValue(Math.atan(1.0/model.getValue()));
    });

    public static CalcOperation DIVONE  = (model -> {
        model.setValue(1.0/ model.getValue());
    });

    public static CalcOperation LOG = (model -> {
        if(model.isInverted()){
            CalcOperations.TENPOW.apply(model);
        }else{
            model.setValue(Math.log10(model.getValue()));
        }
    });

    public static CalcOperation TENPOW = (model -> {
        model.setValue(Math.pow(10.0, model.getValue()));
    });

    public static CalcOperation LN = (model -> {
        if(model.isInverted()){
            CalcOperations.EPOW.apply(model);
        }else{
            model.setValue(Math.log1p(model.getValue()-1.0));
        }
    });


    public static CalcOperation EPOW = (model -> {
        model.setValue(Math.pow(Math.E, model.getValue()));
    });

    public static CalcOperation PM = (CalcModel::swapSign);

    public static CalcOperation DOT = (CalcModel::insertDecimalPoint);






    public static CalcOperation EQUAL = new CalcOperation() {
        @Override
        public void apply(CalcModel model) {
            double operand = model.getActiveOperand();
            double val = model.getValue();
            model.setValue(model.getPendingBinaryOperation().applyAsDouble(operand,val));
            model.clearActiveOperand();
            model.setPendingBinaryOperation(null);
        }
    };
}
