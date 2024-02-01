package hr.fer.zemris.java.gui.prim;

import hr.fer.zemris.java.gui.calc.Calculator;

import javax.swing.*;
import java.awt.*;

public class PrimDemo extends JFrame {

    PrimModel primModel;

    public PrimDemo(){
        setTitle("PrimeNumberLists");
        setSize(500, 500);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.primModel = new PrimModelImpl();
        initGUI();
    }

    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        JList<Integer> leftList = new JList<>((ListModel<Integer>) primModel);
        JScrollPane leftScrollPane = new JScrollPane(leftList);

        JList<Integer> rightList = new JList<>((ListModel<Integer>) primModel);
        JScrollPane rightScrollPane = new JScrollPane(rightList);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScrollPane, rightScrollPane);
        splitPane.setResizeWeight(0.5);
        cp.add(splitPane, BorderLayout.CENTER);

        JButton buttonNext = new JButton("Sljedeći");
        buttonNext.addActionListener(e-> {
            primModel.next();
            int lastIndex = ((ListModel<?>) primModel).getSize() - 1;
            leftList.ensureIndexIsVisible(lastIndex);
            rightList.ensureIndexIsVisible(lastIndex);
        });
        cp.add(buttonNext, BorderLayout.SOUTH);

        cp.setVisible(true);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            new PrimDemo().setVisible(true);
        });
    }
}
