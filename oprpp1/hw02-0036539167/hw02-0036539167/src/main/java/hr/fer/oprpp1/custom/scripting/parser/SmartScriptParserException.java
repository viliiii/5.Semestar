package hr.fer.oprpp1.custom.scripting.parser;

/**
 * The only exception that the SmartScriptParser can throw.
 */
public class SmartScriptParserException extends RuntimeException{
    public SmartScriptParserException(){
        super();
    }

    public SmartScriptParserException(String message){
        super(message);
    }
}
