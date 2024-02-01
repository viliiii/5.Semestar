import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.CalcLayoutException;
import hr.fer.zemris.java.gui.layouts.RCPosition;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
public class CalcLayoutTest {

    @Test
    public void testAdding(){
        Container cp = new Container();
        cp.setLayout(new CalcLayout(3));
        assertThrows(CalcLayoutException.class, ()-> cp.add(new JLabel("a"), new RCPosition(0, 0)));
        assertThrows(CalcLayoutException.class, ()-> cp.add(new JLabel("a"), new RCPosition(1, 3)));
        assertThrows(CalcLayoutException.class, ()-> cp.add(new JLabel("a"), new RCPosition(2, 8)));
        cp.add(new JLabel("a"), new RCPosition(2, 3));
        assertThrows(CalcLayoutException.class, ()-> cp.add(new JLabel("a"), new RCPosition(2, 3)));

    }

    @Test
    public void dimensionTest(){
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
        p.add(l1, new RCPosition(2,2));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();

        assertEquals(152, dim.width);
        assertEquals(158, dim.height);
    }

    @Test
    public void dimensionTest2(){
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
        p.add(l1, new RCPosition(1,1));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();

        assertEquals(152, dim.width);
        assertEquals(158, dim.height);
    }
}