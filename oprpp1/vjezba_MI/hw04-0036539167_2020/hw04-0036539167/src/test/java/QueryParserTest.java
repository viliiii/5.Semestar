import hr.fer.oprpp1.hw04.db.QueryParser;
import org.junit.jupiter.api.Test;

import java.io.EOFException;

import static org.junit.jupiter.api.Assertions.*;
public class QueryParserTest {
    @Test
    public void lexerTest() {
        QueryParser.Lexer parser = new QueryParser.Lexer(" jmbag = \"0000000003\" AND lastName LIKE \"B*\"\n");
        assertEquals(QueryParser.TokenType.JMBAG, parser.nextToken().getType());
        assertEquals(QueryParser.TokenType.EQUALS, parser.nextToken().getType());
        assertEquals(QueryParser.TokenType.STRING, parser.nextToken().getType());
        assertEquals(QueryParser.TokenType.AND, parser.nextToken().getType());
        assertEquals(QueryParser.TokenType.LASTNAME, parser.nextToken().getType());
        assertEquals(QueryParser.TokenType.LIKE, parser.nextToken().getType());
        assertEquals(QueryParser.TokenType.STRING, parser.nextToken().getType());
        assertEquals(QueryParser.TokenType.EOF, parser.nextToken().getType());

    }

    @Test
    public void parserTest(){
        QueryParser qp1 = new QueryParser(" jmbag =\"0123456789\" ");
        assertTrue(qp1.isDirectQuery());
        assertEquals("0123456789", qp1.getQueriedJMBAG());
        assertEquals(1, qp1.getQuery().size());
    }

    @Test
    public void parserTestNotDirectQuery(){
        QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
        assertFalse(qp2.isDirectQuery());
        assertThrows(IllegalStateException.class, ()->qp2.getQueriedJMBAG());
        assertEquals(2, qp2.getQuery().size());
    }
}
