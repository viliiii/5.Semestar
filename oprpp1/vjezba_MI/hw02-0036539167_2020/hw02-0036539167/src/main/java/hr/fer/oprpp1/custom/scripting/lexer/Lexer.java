package hr.fer.oprpp1.custom.scripting.lexer;

import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

public class Lexer {
    private char[] data; // ulazni tekst
    private Token token; // trenutni token
    private int currentIndex; // indeks prvog neobrađenog znaka

    private LexerState currentState;

    // konstruktor prima ulazni tekst koji se tokenizira
    public Lexer(String text) {
        data = text.toCharArray();
        currentIndex = 0;
        currentState = LexerState.TEXT;
        //token = nextToken();
    }

    // generira i vraća sljedeći token
    // baca LexerException ako dođe do pogreške
    public Token nextToken() {
        //If the end has already been printed, calling this method
        //is an exception.
        if (token != null && token.getType() == TokenType.EOF) {
            throw new LexerException("The end is already reached");
        }

        if(this.currentState != LexerState.TEXT){
            skipBlanks();
        }


        //The end of input has been reached.
        if (currentIndex >= data.length) {
            token = new Token(TokenType.EOF, null);
            return token;
        }

        //Catching the character that indicates the change of
        //working mode. { for TAG.

        if(currentIndex!=0 && data[currentIndex] == '{' && data[currentIndex+1] == '$'
        && data[currentIndex-1] != '\\'){
            Token tmp = new Token(TokenType.TAGOPEN, "{$");
            currentIndex++;
            currentIndex++;
            setState(LexerState.TAG);
            return tmp;
        }
        if(data[currentIndex] == '{' && data[currentIndex+1] == '$'){
            Token tmp = new Token(TokenType.TAGOPEN, "{$");
            currentIndex++;
            currentIndex++;
            setState(LexerState.TAG);
            return tmp;
        }


        //Lexer options for inside a TAG
        if(currentState == LexerState.TAG){
            //Checking FOR tag
            if((data[currentIndex] == 'F' || data[currentIndex] == 'f')&& (data[currentIndex+1] == 'O' || data[currentIndex+1] == 'o')
            &&(data[currentIndex+2] == 'R' || data[currentIndex+2] == 'r')){
                currentIndex = currentIndex + 3;
                return new Token(TokenType.TAGFOR, "FOR");
            }

            //Checking END tag
            if((data[currentIndex] == 'E' || data[currentIndex] == 'e') && (data[currentIndex+1] == 'N' || data[currentIndex+1] == 'n')
                    &&(data[currentIndex+2] == 'D' || data[currentIndex+2] == 'd')){
                currentIndex = currentIndex + 3;
                return new Token(TokenType.TAGEND, "END");
            }

            //Checking DATE tag
            if((data[currentIndex] == 'D' || data[currentIndex] == 'd') && (data[currentIndex+1] == 'A' || data[currentIndex+1] == 'a')
                    &&(data[currentIndex+2] == 'T' || data[currentIndex+2] == 't') &&(data[currentIndex+3] == 'E' || data[currentIndex+3] == 'e')){
                currentIndex = currentIndex + 4;
                return new Token(TokenType.TAGDATE, "DATE");
            }

            //Checking = tag
            if(data[currentIndex] == '='){
                currentIndex++;
                return new Token(TokenType.TAGEQUAL, "=");
            }

            //Checking if currentIndex has reached a variable
            if(Character.isLetter(data[currentIndex])){
                StringBuilder sb = new StringBuilder();
                while (Character.isLetter(data[currentIndex])
                        ||Character.isDigit(data[currentIndex])
                        || data[currentIndex] =='_'){
                    sb.append(data[currentIndex]);
                    currentIndex++;
                }
                return new Token(TokenType.VARIABLE, sb.toString());
            }

            //Checking if currentIndex has reached '@' so that means function
            if(data[currentIndex]=='@' && Character.isLetter(data[currentIndex+1])){
                currentIndex++;
                StringBuilder sb = new StringBuilder();
                while (Character.isLetter(data[currentIndex])
                ||Character.isDigit(data[currentIndex])
                || data[currentIndex] =='_'){
                    sb.append(data[currentIndex]);
                    currentIndex++;
                }
                return new Token(TokenType.FUNCTION, sb.toString());
            }

            //Checking if currentIndex has reached a string.
            if(data[currentIndex] == '"'){
                currentIndex++;
                StringBuilder sb = new StringBuilder();
                while (data[currentIndex] != '"'){
                    if(data[currentIndex] == '\\'
                    &&data[currentIndex+1] == '\\'){
                        currentIndex++;
                        sb.append(data[currentIndex]);
                        currentIndex++;
                    }else if(data[currentIndex] == '\\' && data[currentIndex+1] == '"'){
                        currentIndex++;
                        sb.append(data[currentIndex]);
                        currentIndex++;
                    }else if(data[currentIndex] == '\\'){
                        throw new SmartScriptParserException("Illegal escaping inside a String.");
                    }else {
                        sb.append(data[currentIndex]);
                        currentIndex++;
                    }

                }
                currentIndex++;
                return new Token(TokenType.STRING, sb.toString());
            }

            //Checking if currentIndex has reached a number.
            if(Character.isDigit(data[currentIndex])
            ||(data[currentIndex]=='-' && Character.isDigit(data[currentIndex+1]))){
                StringBuilder sb = new StringBuilder();
                if(data[currentIndex] =='-'){
                    sb.append(data[currentIndex]);
                    currentIndex++;
                }
                while (Character.isDigit(data[currentIndex])
                || data[currentIndex] == '.'){
                    sb.append(data[currentIndex]);
                    currentIndex++;
                }
                if(sb.indexOf(".") == -1){
                    return new Token(TokenType.NUMBER, sb.toString());
                }else {
                    return new Token(TokenType.DECIMALNUMBER, sb.toString());
                }

            }

            //Checking for SYMBOL operators.
            if(data[currentIndex] == '+' || data[currentIndex] == '-'
            || data[currentIndex] == '*' || data[currentIndex] == '/'
            || data[currentIndex] == '^'){
                Token tmp = new Token(TokenType.SYMBOL, String.valueOf(data[currentIndex]));
                currentIndex++;
                return tmp;
            }

            //Leaving TAG mode.
            if(data[currentIndex] == '$' && data[currentIndex+1] == '}'){
                Token tmp = new Token(TokenType.TAGCLOSE, "$}");
                currentIndex++;
                currentIndex++;
                setState(LexerState.TEXT);
                return tmp;
            }

        }

        //Lexer options for inside a TEXT (executing if not in TAG).
        StringBuilder sb = new StringBuilder();
        while (currentIndex< data.length && !(data[currentIndex] =='{' && data[currentIndex+1] =='$'
        &&data[currentIndex-1]!='\\')){
            if(((currentIndex+1)< data.length)&&data[currentIndex]=='\\' && (data[currentIndex+1]!='\\' && data[currentIndex+1]!='{')){
                throw new SmartScriptParserException("Illegal escaping outside of a tag.");
            }
            sb.append(data[currentIndex]);
            currentIndex++;
        }




        //currentIndex++;
        return new Token(TokenType.TEXT, sb.toString());
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
