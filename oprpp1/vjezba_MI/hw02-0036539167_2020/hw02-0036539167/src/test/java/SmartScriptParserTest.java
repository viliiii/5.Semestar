import static org.junit.jupiter.api.Assertions.*;

import hr.fer.oprpp1.custom.scripting.lexer.*;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SmartScriptParserTest {
    private String readExample(int n) {
        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer"+n+".txt")) {
            if(is==null) throw new RuntimeException("Datoteka extra/primjer"+n+".txt je nedostupna.");
            byte[] data = is.readAllBytes();
            String text = new String(data, StandardCharsets.UTF_8);
            return text;
        } catch(IOException ex) {
            throw new RuntimeException("Greška pri čitanju datoteke.", ex);
        }
    }

    @Test
    public void testPr1(){
        String text = readExample(1);
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode docNode = parser.getDocumentNode();
        assertEquals(1, docNode.numberOfChildren());
    }

    @Test
    public void testPr2(){
        String text = readExample(2);
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode docNode = parser.getDocumentNode();
        assertEquals(1, docNode.numberOfChildren());
        assertEquals(TextNode.class, docNode.getChild(0).getClass());
    }

    @Test
    public void testPr3(){
        String text = readExample(3);
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode docNode = parser.getDocumentNode();
        assertEquals(1, docNode.numberOfChildren());
        assertEquals(TextNode.class, docNode.getChild(0).getClass());
    }

    @Test
    public void testPr4(){
        String text = readExample(4);
        assertThrows(SmartScriptParserException.class, ()-> new SmartScriptParser(text));

    }

    @Test
    public void testPr5(){
        String text = readExample(5);
        assertThrows(SmartScriptParserException.class, ()-> new SmartScriptParser(text));

    }

    @Test
    public void testPr6(){
        String text = readExample(6);
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode docNode = parser.getDocumentNode();
        assertEquals(1, docNode.numberOfChildren());
        assertEquals(TextNode.class, docNode.getChild(0).getClass());

    }

    @Test
    public void testPr7(){
        String text = readExample(7);
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode docNode = parser.getDocumentNode();
        assertEquals(3, docNode.numberOfChildren());
        assertEquals(TextNode.class, docNode.getChild(0).getClass());
        assertEquals(EchoNode.class, docNode.getChild(1).getClass());
        assertEquals(TextNode.class, docNode.getChild(2).getClass());
    }


    @Test
    public void testPr8(){
        String text = readExample(8);
        assertThrows(SmartScriptParserException.class, ()-> new SmartScriptParser(text));

    }
    @Test
    public void testPr9(){
        String text = readExample(9);
        assertThrows(SmartScriptParserException.class, ()-> new SmartScriptParser(text));

    }

    @Test
    public void myTestLast(){
        String text = readExample(10);
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode docNode = parser.getDocumentNode();
        assertEquals(2, docNode.numberOfChildren());
        assertEquals(TextNode.class, docNode.getChild(0).getClass());
        assertEquals(ForLoopNode.class, docNode.getChild(1).getClass());
        assertEquals(EchoNode.class, docNode.getChild(1).getChild(1).getClass());

    }

}
