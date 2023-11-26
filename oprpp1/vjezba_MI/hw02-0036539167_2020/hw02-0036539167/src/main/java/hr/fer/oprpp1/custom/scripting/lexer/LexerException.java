package hr.fer.oprpp1.custom.scripting.lexer;

public class LexerException extends RuntimeException{
    public LexerException(){
        super();
    }

    public LexerException(String mes){
        super(mes);
    }
}
