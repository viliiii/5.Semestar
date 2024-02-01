package ispit.zi.zad1;

import javax.swing.*;
import java.awt.*;

public class ExamZad01_2 extends JDialog {
    public ExamZad01_2() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        initGUI();
        pack();
    }
    private void initGUI() {
        Container cp = getContentPane();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JSlider slider = new JSlider(10, 90);
        panel.add(slider, BorderLayout.NORTH);

        JPanel managerPanel = new JPanel();
        ExamLayoutManager exlm = new ExamLayoutManager(20);

        slider.addChangeListener((c) -> {
            exlm.setPostotak(slider.getValue());
            managerPanel.revalidate();
        });
        managerPanel.setLayout(exlm);
        managerPanel.add(
                makeLabel("Ovo je tekst za područje 1.", Color.RED),
                ExamLayoutManager.AREA1, -1);
        managerPanel.add(
                makeLabel("Područje 2.", Color.GREEN),
                ExamLayoutManager.AREA2, -1);
        managerPanel.add(
                makeLabel("Područje 3.", Color.YELLOW),
                ExamLayoutManager.AREA3, -1);

        panel.add(managerPanel, BorderLayout.CENTER);

        cp.add(panel);
    }
    private Component makeLabel(String txt, Color col) {
        JLabel lab = new JLabel(txt);
        lab.setOpaque(true);
        lab.setBackground(col);
        return lab;
    }
}

