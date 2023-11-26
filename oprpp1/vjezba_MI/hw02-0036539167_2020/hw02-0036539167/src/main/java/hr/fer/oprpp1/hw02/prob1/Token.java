package hr.fer.oprpp1.hw02.prob1;

/**
 * Class representing the parts of the script code.
 * Every part divided with spaces has its own token
 * with type (described in the TokenType enum) and value (exact String
 * value of the part).
 */
public class Token {

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
