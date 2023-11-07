package hr.fer.oprpp1.hw02.prob1;

import java.io.CharArrayReader;
import java.util.Arrays;
import java.util.LinkedList;

public class Lexer {
    private char[] data; // ulazni tekst
    private Token token; // trenutni token
    private int currentIndex; // indeks prvog neobrađenog znaka

    private LexerState currentState;

    // konstruktor prima ulazni tekst koji se tokenizira
    public Lexer(String text) {
        data = text.toCharArray();
        currentIndex = 0;
        currentState = LexerState.BASIC;
        //token = nextToken();
    }

    // generira i vraća sljedeći token
    // baca LexerException ako dođe do pogreške
    public Token nextToken() {
        int startIndex;
        int endIndex;
        //If the end has already been printed, calling this method
        //is an exception.
        if (token != null && token.getType() == TokenType.EOF) {
            throw new LexerException("The end is already reached");
        }

        skipBlanks();

        //The end of input has been reached.
        if (currentIndex >= data.length) {
            token = new Token(TokenType.EOF, null);
            return token;
        }



        //Checking if currentIndex has reached a letter or a word.
        if ((Character.isLetter(data[currentIndex]) || data[currentIndex]=='\\')
                &&currentState == LexerState.BASIC) {
            StringBuilder sb = new StringBuilder();
            while (currentIndex < data.length && (Character.isLetter(data[currentIndex])
                    ||data[currentIndex]=='\\' )) {
                if(data[currentIndex]=='\\'){
                    if((currentIndex+1) >= data.length){
                        throw new LexerException();
                    }else if(data[currentIndex+1]=='\\'){
                        sb.append("\\");
                        currentIndex++;
                        currentIndex++;
                    } else if (Character.isDigit(data[currentIndex+1])) {
                        sb.append(data[currentIndex+1]);
                        currentIndex++;
                        currentIndex++;
                    }else if(Character.isLetter(data[currentIndex+1])){
                        throw new LexerException();
                    }
                }else{
                    sb.append(data[currentIndex]);

                    currentIndex++;
                }


            }

            token = new Token(TokenType.WORD, sb.toString());
            return token;
        }



        //Checking if currentIndex has reached a Long - parse-able number.
        if(Character.isDigit(data[currentIndex])&&currentState == LexerState.BASIC){
            startIndex = currentIndex;
            currentIndex++;
            while (currentIndex < data.length && Character.isDigit(data[currentIndex])) {
                currentIndex++;
            }
            endIndex = currentIndex;
            String value = new String(data, startIndex, endIndex - startIndex);
            try {
                Long longValue = Long.parseLong(value);
                token = new Token(TokenType.NUMBER, longValue);
                return token;
            }catch (NumberFormatException ex){
                throw new LexerException("The input is not valid.");
            }
        }

        //Catching the character that indicates the change of
        //working mode.
        if(data[currentIndex] == '#'){
            Token tmp = new Token(TokenType.SYMBOL, '#');
            currentIndex++;
            if(currentState == LexerState.BASIC){
                setState(LexerState.EXTENDED);
            }else {
                setState(LexerState.BASIC);
            }
            return tmp;
        }

        //LexerState is EXTENDED and all the characters except spaces are treated the same,
        //as words separated by spaces.
        if(currentState == LexerState.EXTENDED){
            StringBuilder sb = new StringBuilder();
            while (currentIndex < data.length && (data[currentIndex] != ' '
            &&data[currentIndex] != '\t' && data[currentIndex] != '\r'
            &&data[currentIndex] != '\n' &&data[currentIndex] != '#')){
                sb.append(data[currentIndex]);
                currentIndex++;
            }

            token = new Token(TokenType.WORD, sb.toString());
            return token;
        }






        //If nothing is found rather than some SYMBOL.
        Token tmp = new Token(TokenType.SYMBOL, data[currentIndex]);
        currentIndex++;
        return tmp;

    }


    // vraća zadnji generirani token; može se pozivati
    // više puta; ne pokreće generiranje sljedećeg tokena
    public Token getToken() {
        return this.token;
    }

    public void setState(LexerState state){
        if(state == null){
            throw new NullPointerException("Invalid state.");
        }
        this.currentState = state;
    }



    private void skipBlanks(){
        while (currentIndex < data.length){
            char tmp = data[currentIndex];
            if(tmp==' ' || tmp=='\t' || tmp=='\r' || tmp=='\n'){
                currentIndex++;
                continue;
            }
            break;
        }
    }


}
