import static org.junit.jupiter.api.Assertions.*;

import hr.fer.oprpp1.custom.scripting.lexer.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class LexerTest {



    // Helper method for checking if lexer generates the same stream of tokens
    // as the given stream.
    private void checkTokenStream(Lexer lexer, Token[] correctData) {
        int counter = 0;
        for(Token expected : correctData) {
            Token actual = lexer.nextToken();
            String msg = "Checking token "+counter + ":";
            assertEquals(expected.getType(), actual.getType(), msg);
            assertEquals(expected.getValue(), actual.getValue(), msg);
            counter++;
        }
    }




    @Test
    public void myTest(){
        Lexer lexer = new Lexer("This is sample text.\n");
        checkToken(lexer.nextToken(), new Token(TokenType.TEXT, "This is sample text.\n"));
    }

    @Test
    public void myTest2(){
        Lexer lexer = new Lexer("This is sample text.\n" +
                "{$ FOR i 1 10 1 $}");
        checkToken(lexer.nextToken(), new Token(TokenType.TEXT, "This is sample text.\n"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGOPEN, "{$"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGFOR, "FOR"));
        checkToken(lexer.nextToken(), new Token(TokenType.VARIABLE, "i"));
        checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, "1"));
        checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, "10"));
        checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, "1"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGCLOSE, "$}"));
        checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
    }

    @Test
    public void myTest3(){
        Lexer lexer = new Lexer("This is sample text.\n" +
                "{$ FOR i 1 10 1 $}\n" +
                " This is {$= i $}-th time this message is generated.\n" +
                "{$END$}\n");
        checkToken(lexer.nextToken(), new Token(TokenType.TEXT, "This is sample text.\n"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGOPEN, "{$"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGFOR, "FOR"));
        checkToken(lexer.nextToken(), new Token(TokenType.VARIABLE, "i"));
        checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, "1"));
        checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, "10"));
        checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, "1"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGCLOSE, "$}"));

        checkToken(lexer.nextToken(), new Token(TokenType.TEXT, "\n This is "));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGOPEN, "{$"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGEQUAL, "="));
        checkToken(lexer.nextToken(), new Token(TokenType.VARIABLE, "i"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGCLOSE, "$}"));
        checkToken(lexer.nextToken(), new Token(TokenType.TEXT, "-th time this message is generated.\n"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGOPEN, "{$"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGEND, "END"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGCLOSE, "$}"));
        checkToken(lexer.nextToken(), new Token(TokenType.TEXT, "\n"));
        checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
    }



    @Test
    public void myTest4(){
        Lexer lexer = new Lexer("{$FOR i 0 10 2 $}\n" +
                " sin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}\n" +
                "{$END$}");

        checkToken(lexer.nextToken(), new Token(TokenType.TAGOPEN, "{$"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGFOR, "FOR"));
        checkToken(lexer.nextToken(), new Token(TokenType.VARIABLE, "i"));
        checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, "0"));
        checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, "10"));
        checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, "2"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGCLOSE, "$}"));
        checkToken(lexer.nextToken(), new Token(TokenType.TEXT, "\n sin("));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGOPEN, "{$"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGEQUAL, "="));
        checkToken(lexer.nextToken(), new Token(TokenType.VARIABLE, "i"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGCLOSE, "$}"));
        checkToken(lexer.nextToken(), new Token(TokenType.TEXT, "^2) = "));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGOPEN, "{$"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGEQUAL, "="));
        checkToken(lexer.nextToken(), new Token(TokenType.VARIABLE, "i"));
        checkToken(lexer.nextToken(), new Token(TokenType.VARIABLE, "i"));
        checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, "*"));
        checkToken(lexer.nextToken(), new Token(TokenType.FUNCTION, "sin"));
        checkToken(lexer.nextToken(), new Token(TokenType.STRING, "0.000"));
        checkToken(lexer.nextToken(), new Token(TokenType.FUNCTION, "decfmt"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGCLOSE, "$}"));
        checkToken(lexer.nextToken(), new Token(TokenType.TEXT, "\n"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGOPEN, "{$"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGEND, "END"));
        checkToken(lexer.nextToken(), new Token(TokenType.TAGCLOSE, "$}"));
        checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));




    }


    private void checkToken(Token actual, Token expected) {
        String msg = "Token are not equal.";
        assertEquals(expected.getType(), actual.getType(), msg);
        assertEquals(expected.getValue(), actual.getValue(), msg);
    }



}
