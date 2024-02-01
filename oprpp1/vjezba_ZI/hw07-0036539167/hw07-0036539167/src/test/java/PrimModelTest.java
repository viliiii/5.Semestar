import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcModelImpl;
import hr.fer.zemris.java.gui.prim.PrimModel;
import hr.fer.zemris.java.gui.prim.PrimModelImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PrimModelTest {
    private PrimModelImpl model;

    private static PrimModelImpl newPrimModel() {
        return new PrimModelImpl();
    }

    @BeforeEach
    public void setup() {
        model = newPrimModel();
    }

    @Test
    public void testNext(){
        assertEquals(1, model.getCurrent());
        model.next();
        assertEquals(2, model.getCurrent());
        model.next();
        assertEquals(3, model.getCurrent());
        model.next();
        assertEquals(5, model.getCurrent());
        model.next();
        assertEquals(7, model.getCurrent());
        model.next();
        assertEquals(11, model.getCurrent());
        model.next();
        assertEquals(13, model.getCurrent());
        model.next();
        assertEquals(17, model.getCurrent());
        model.next();
        assertEquals(19, model.getCurrent());
        model.next();
        assertEquals(23, model.getCurrent());
        model.next();
        assertEquals(29, model.getCurrent());

        assertEquals(11, model.getSize());

        assertEquals(19, model.getElementAt(8));
    }


}
