package hr.fer.oprpp1.hw04.db;

import java.io.EOFException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * The QueryParser class represents a parser for querying a database using conditional expressions.
 * It parses the given query string and creates a list of ConditionalExpression objects for further processing.
 */
public class QueryParser {

    private List<ConditionalExpression> listConEx;
    private Lexer queryLexer;
    private final HashMap<TokenType, IComparisonOperator> operators;
    private final HashMap<TokenType, IFieldValueGetter> fieldValues;

    /**
     * Initializes the {@code operators} and {@code fieldValues} maps with appropriate mappings.
     */
    {
        operators = new HashMap<>();
        operators.put(TokenType.LESS, ComparisonOperators.LESS);
        operators.put(TokenType.LESS_EQUAL, ComparisonOperators.LESS_OR_EQUALS);
        operators.put(TokenType.GREATER, ComparisonOperators.GREATER);
        operators.put(TokenType.GREATER_EQUAL, ComparisonOperators.GREATER_OR_EQUALS);
        operators.put(TokenType.EQUALS, ComparisonOperators.EQUALS);
        operators.put(TokenType.NOT_EQUALS, ComparisonOperators.NOT_EQUALS);
        operators.put(TokenType.LIKE, ComparisonOperators.LIKE);

        fieldValues = new HashMap<>();
        fieldValues.put(TokenType.FIRSTNAME,FieldValueGetters.FIRST_NAME);
        fieldValues.put(TokenType.LASTNAME,FieldValueGetters.LAST_NAME);
        fieldValues.put(TokenType.JMBAG,FieldValueGetters.JMBAG);
    }

    /**
     * Constructs a {@code QueryParser} object and parses the input query string.
     *
     * @param query The input query string to be parsed.
     */
    public QueryParser(String query){
        queryLexer = new Lexer(query.replace("query", ""));
        listConEx = new LinkedList<>();
        parse();
    }

    /**
     * Parses the input query string and fills private list of conditional expressions.
     */
    public void parse(){
        IFieldValueGetter FieldGetter;
        IComparisonOperator ComparisonOperator;
        String StringLiteral;

        Token token = queryLexer.nextToken();

        while (token.getType() != TokenType.EOF){
            boolean negation = false;

            if(token.getType() == TokenType.AND) token= queryLexer.nextToken();

            if(token.getType() == TokenType.NEGATION){
                negation = true;
                token = queryLexer.nextToken();
            }

            FieldGetter = fieldValues.get(token.getType());
            token = queryLexer.nextToken();

            ComparisonOperator = operators.get(token.getType());
            token = queryLexer.nextToken();

            StringLiteral = (String) token.getValue();
            token = queryLexer.nextToken();

            listConEx.add(new ConditionalExpression(FieldGetter, StringLiteral, ComparisonOperator, negation));

        }

    }


    /**
     * Checks if the parsed query is a direct query with a single condition on JMBAG.
     * (jmbag = "000...")
     *
     * @return {@code true} if it's a direct query, {@code false} otherwise.
     */
    public boolean isDirectQuery(){



        return listConEx.size()==1
                && listConEx.get(0).getFieldGetter() == FieldValueGetters.JMBAG
                && listConEx.get(0).getComparisonOperator() == ComparisonOperators.EQUALS
                && !listConEx.get(0).isNegation();
    }

    /**
     * Returns the queried JMBAG if the query is a direct query.
     *
     * @return The queried JMBAG.
     * @throws IllegalStateException if the query is not a direct query.
     */
    public String getQueriedJMBAG(){
        if(!isDirectQuery()){
            throw new IllegalStateException("Not a direct query.");
        }
        return listConEx.get(0).getStringLiteral();
    }

    /**
     * Returns the list of conditional expressions parsed from the query.
     *
     * @return List of conditional expressions.
     */
    public List<ConditionalExpression> getQuery(){

        return listConEx;
    }


    /**
     * The {@code Lexer} class represents a lexer for tokenizing
     * the input query string.
     */
    public static class Lexer{
        private char[] data; // ulazni tekst
        private Token token; // trenutni token
        private int currentIndex; // indeks prvog neobrađenog znaka

        /**
         * Constructor which accepts String text (query text) and
         * saves it as Character array.
         * @param text query text
         */
        public Lexer(String text) {
            data = text.toCharArray();
            currentIndex = 0;
        }

        /**
         * Generates and returns next Token from input text. Reads text and tokenizes
         * it using Token class.
         * @return next Token from input.
         * @throws IllegalArgumentException if query is not valid
         */
        public Token nextToken() {
            //If the end has already been printed, calling this method
            //is an exception.
            if (token != null && token.getType() == TokenType.EOF) {
                throw new IllegalStateException("Already finished lexing.");
            }

            skipBlanks();

            //The end of input has been reached.
            if (currentIndex >= data.length) {
                token = new Token(TokenType.EOF, null);
                return token;
            }




            //Checking if currentIndex has reached a: JMBAG
            if(data[currentIndex] == 'j'){
                currentIndex = currentIndex + 5;
                return new Token(TokenType.JMBAG, "jmbag");
            }

            //Checking if currentIndex has reached a: !=
            if(currentIndex+1 < data.length && data[currentIndex] == '!'
                    &&data[currentIndex+1] == '='){
                currentIndex = currentIndex + 2;
                return new Token(TokenType.NOT_EQUALS, "!=");
            }

            //Checking if currentIndex has reached a: !
            if(data[currentIndex] == '!'){
                currentIndex++;
                if(currentIndex >= data.length){
                    throw new IllegalArgumentException("Invalid input: !" );
                }
                if(data[currentIndex] == '!'){
                    throw new IllegalArgumentException("Invalid input: !!" );
                }
                return new Token(TokenType.NEGATION, "!");
            }

            //Checking if currentIndex has reached a: >=
            if(currentIndex+1 < data.length && data[currentIndex] == '>'
            &&data[currentIndex+1] == '='){
                currentIndex = currentIndex + 2;
                return new Token(TokenType.GREATER_EQUAL, ">=");
            }

            //Checking if currentIndex has reached a: <=
            if(currentIndex+1 < data.length && data[currentIndex] == '<'
                    &&data[currentIndex+1] == '='){
                currentIndex = currentIndex + 2;
                return new Token(TokenType.LESS_EQUAL, "<=");
            }

            //Checking if currentIndex has reached a: >
            if(data[currentIndex] == '>'){
                currentIndex++;
                return new Token(TokenType.GREATER, ">");
            }

            //Checking if currentIndex has reached a: <
            if(data[currentIndex] == '<'){
                currentIndex++;
                return new Token(TokenType.LESS, "<");
            }

            //Checking if currentIndex has reached a: =
            if(data[currentIndex] == '='){
                currentIndex++;
                return new Token(TokenType.EQUALS, "=");
            }

            //Checking if currentIndex has reached a: AND
            if(data[currentIndex] == 'a' || data[currentIndex] == 'A'){
                currentIndex = currentIndex + 3;
                return new Token(TokenType.AND, "AND");
            }

            //Checking if currentIndex has reached a: lastname
            if(data[currentIndex] == 'l'){
                currentIndex = currentIndex + 8;
                return new Token(TokenType.LASTNAME, "lastName");
            }

            //Checking if currentIndex has reached a: firstname
            if(data[currentIndex] == 'f'){
                currentIndex = currentIndex + 9;
                return new Token(TokenType.FIRSTNAME, "firstName");
            }

            //Checking if currentIndex has reached a: string
            if(data[currentIndex] == '"'){
                StringBuilder sb = new StringBuilder();
                currentIndex++;
                while (data[currentIndex] != '"'){
                    sb.append(data[currentIndex++]);
                }
                currentIndex++;
                return new Token(TokenType.STRING, sb.toString());
            }

            if(data[currentIndex] == 'L'){
                currentIndex = currentIndex + 4;
                return new Token(TokenType.LIKE, "LIKE");
            }

            //If nothing was found to be legally tokenized,
            //the input must be invalid.
            throw new IllegalArgumentException("Invalid input.");
        }

        /**
         * @return current token (last returned by nextToken())
         */
        public Token getToken() {
            return this.token;
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

    /**
     * The {@code Token} class represents a token with its type and value.
     */
    public static class Token{
        TokenType type;
        Object value;

        public Token(TokenType type, Object value) {
            this.type = type;
            this.value = value;
        }
        public Object getValue() {
            return  this.value;
        }
        public TokenType getType() {
            return this.type;
        }
    }

    /**
     * Enum representing different token types that can be generated by the lexer.
     */
    public enum TokenType{
        EOF,
        JMBAG,
        FIRSTNAME,
        LASTNAME,
        EQUALS,
        NOT_EQUALS,
        LIKE,
        GREATER,
        GREATER_EQUAL,
        LESS,
        LESS_EQUAL,
        STRING,
        AND,
        EXIT,
        NEGATION

    }






}
