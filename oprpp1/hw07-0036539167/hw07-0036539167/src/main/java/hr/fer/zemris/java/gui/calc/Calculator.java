package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcModelImpl;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

public class Calculator extends JFrame {

    private CalcModel calcModel;
    private Stack<Double> stack;
    private JLabel display;
    private HashMap<String, CalcOperation> operations = new HashMap<>();

    {
        operations.put("-", CalcOperations.MINUS);
        operations.put("+", CalcOperations.PLUS);
        operations.put("=", CalcOperations.EQUAL);
        operations.put("*", CalcOperations.MULTIPLY);
        operations.put("/", CalcOperations.DIVIDE);
        operations.put("x^n", CalcOperations.POWER);
        operations.put("x^(1/n)", CalcOperations.POWER1N);
        operations.put("reset", CalcOperations.RESET);
        operations.put("clr", CalcOperations.CLEAR);
        operations.put("sin", CalcOperations.SIN);
        operations.put("arcsin", CalcOperations.ARCSIN);
        operations.put("cos", CalcOperations.COS);
        operations.put("arccos", CalcOperations.ARCOS);
        operations.put("tan", CalcOperations.TAN);
        operations.put("arctan", CalcOperations.ARCTAN);
        operations.put("ctg", CalcOperations.CTG);
        operations.put("arcctg", CalcOperations.ARCCTG);
        operations.put("1/x", CalcOperations.DIVONE);
        operations.put("log", CalcOperations.LOG);
        operations.put("10^x", CalcOperations.TENPOW);
        operations.put("ln", CalcOperations.LN);
        operations.put("e^x", CalcOperations.EPOW);
        operations.put("+/-", CalcOperations.PM);
        operations.put(".", CalcOperations.DOT);
    }

    public Calculator(){
        setTitle("Calculator 1.0");
        setSize(300, 300);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.calcModel = new CalcModelImpl();
        this.stack = new Stack<>();
        initGUI();
    }

    private void initGUI(){
        Container cp = getContentPane();
        cp.setLayout(new CalcLayout(3));

        display = new JLabel(" ");
        display.setBackground(Color.ORANGE);
        display.setOpaque(true);
        display.setHorizontalAlignment(JLabel.RIGHT);
        display.setForeground(Color.WHITE);
        display.setFont(new Font("Arial", Font.PLAIN, 50));
        display.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        calcModel.addCalcValueListener(new CalcValueListener() {
            @Override
            public void valueChanged(CalcModel model) {
                display.setText(model.toString());
            }
        });
        cp.add(display, new RCPosition(1,1));

        cp.add(createButton("="), new RCPosition(1,6));
        cp.add(createButton("clr"), new RCPosition(1,7));

        cp.add(createButton("1/x"), new RCPosition(2,1));
        cp.add(createButton("sin"), new RCPosition(2,2));
        cp.add(createButton("7"), new RCPosition(2,3));
        cp.add(createButton("8"), new RCPosition(2,4));
        cp.add(createButton("9"), new RCPosition(2,5));
        cp.add(createButton("/"), new RCPosition(2,6));
        cp.add(createButton("reset"), new RCPosition(2,7));

        cp.add(createButton("log"), new RCPosition(3,1));
        cp.add(createButton("cos"), new RCPosition(3,2));
        cp.add(createButton("4"), new RCPosition(3,3));
        cp.add(createButton("5"), new RCPosition(3,4));
        cp.add(createButton("6"), new RCPosition(3,5));
        cp.add(createButton("*"), new RCPosition(3,6));
        cp.add(createButton("push"), new RCPosition(3,7));

        cp.add(createButton("ln"), new RCPosition(4,1));
        cp.add(createButton("tan"), new RCPosition(4,2));
        cp.add(createButton("1"), new RCPosition(4,3));
        cp.add(createButton("2"), new RCPosition(4,4));
        cp.add(createButton("3"), new RCPosition(4,5));
        cp.add(createButton("-"), new RCPosition(4,6));
        cp.add(createButton("pop"), new RCPosition(4,7));

        cp.add(createButton("x^n"), new RCPosition(5,1));
        cp.add(createButton("ctg"), new RCPosition(5,2));
        cp.add(createButton("0"), new RCPosition(5,3));
        cp.add(createButton("+/-"), new RCPosition(5,4));
        cp.add(createButton("."), new RCPosition(5,5));
        cp.add(createButton("+"), new RCPosition(5,6));
        JCheckBox checkBox = new JCheckBox("Inv");
        checkBox.setFont(new Font("Arial", Font.PLAIN, 20));
        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                invert(cp, e.getStateChange() == ItemEvent.SELECTED);
                calcModel.swapInvert();
            }
        });
        cp.add(checkBox, new RCPosition(5,7));


    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            new Calculator().setVisible(true);
        });
    }

    /**
     * Inverts the button text of the specified container, if the button text matches a supported function.
     *
     * @param c the container to invert the button text of
     * @param invert whether to invert the button text or not
     */
    private void invert(Container c, boolean invert) {
        for (Component component : c.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                String buttonText = button.getText();

                switch (buttonText) {
                    case "sin":
                        button.setText(invert ? "arcsin" : "sin");
                        break;
                    case "arcsin":
                        button.setText(!invert ? "sin" : "arcsin");
                        break;
                    case "log":
                        button.setText(invert ? "10^x" : "log");
                        break;
                    case "10^x":
                        button.setText(!invert ? "log" : "10^x");
                        break;
                    case "cos":
                        button.setText(invert ? "arccos" : "cos");
                        break;
                    case "arccos":
                        button.setText(!invert ? "cos" : "arccos");
                        break;
                    case "ln":
                        button.setText(invert ? "e^x" : "ln");
                        break;
                    case "e^x":
                        button.setText(!invert ? "ln" : "e^x");
                        break;
                    case "tan":
                        button.setText(invert ? "arctan" : "tan");
                        break;
                    case "arctan":
                        button.setText(!invert ? "tan" : "arctan");
                        break;
                    case "x^n":
                        button.setText(invert ? "x^(1/n)" : "x^n");
                        break;
                    case "x^(1/n)":
                        button.setText(!invert ? "x^n" : "x^(1/n)" );
                        break;
                    case "ctg":
                        button.setText(invert ? "arcctg" : "ctg");
                        break;
                    case "arcctg":
                        button.setText(!invert ? "ctg" : "arcctg");
                        break;
                }
            }
        }
    }




    private JButton createButton(String text){
        JButton button = new JButton(text);
        button.setBackground(Color.BLUE);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        button.setFont(new Font("Arial", Font.PLAIN, 20));

        if(Character.isDigit(text.toCharArray()[0]) && text.toCharArray().length==1){
            button.addActionListener(e -> digitPressed(Integer.parseInt(text)));
        }else if(text.equals("push")){
            button.addActionListener(e -> stack.push(Double.parseDouble(display.getText())));
        }else if(text.equals("pop")){
            button.addActionListener(e -> {
               try{
                   calcModel.setValue(stack.pop());

                   display.setText(String.valueOf(calcModel.getValue()));
               }catch(EmptyStackException empt){
                   display.setText(display.getText() + "Stack is empty!");
               }
            });
        }
        else{
            button.addActionListener(e -> operations.get(text).apply(calcModel));
        }

        return button;
    }

    private void digitPressed(int digit){
        calcModel.insertDigit(digit);
    }
}
