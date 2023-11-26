package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.lexer.Lexer;
import hr.fer.oprpp1.custom.scripting.lexer.Token;
import hr.fer.oprpp1.custom.scripting.lexer.TokenType;
import hr.fer.oprpp1.custom.scripting.nodes.*;

/**
 * Parser for the scripts with specific syntax rules.
 * Method parse() fills documentNode with Nodes that are
 * logical parts of the code given in String documentBody into
 * the constructor where method parse() is called.
 */
public class SmartScriptParser {

    Lexer smartScriptLexer;
    DocumentNode documentNode;

    /**
     * Constructor. Calls method parse() to delegate
     * parsing task to that method.
     * @param documentBody script body for parsing
     */
    public SmartScriptParser(String documentBody){
        smartScriptLexer = new Lexer(documentBody);
        parse();
    }

    /**
     * Method that parses script and fills documentNode
     * with the Nodes of the script.
     */
    public void parse(){
        ObjectStack stack = new ObjectStack();
        documentNode = new DocumentNode();

        stack.push(documentNode);
        Token currentToken = smartScriptLexer.nextToken();
        while (currentToken.getType() != TokenType.EOF){

            if(currentToken.getType() == TokenType.TAGEQUAL){
                currentToken = smartScriptLexer.nextToken();
                Element[] elements = new Element[20];   //Assuming that there will be 20 or fewer elements inside an empty tag
                int i = 0;
                while (currentToken.getType() != TokenType.TAGCLOSE){
                    switch (currentToken.getType()){
                        case VARIABLE -> elements[i] = new ElementVariable((String) currentToken.getValue());
                        case FUNCTION -> elements[i] = new ElementFunction((String) currentToken.getValue());
                        case SYMBOL -> elements[i] = new ElementOperator((String) currentToken.getValue());
                        case STRING -> elements[i] = new ElementString((String) currentToken.getValue());
                        case NUMBER -> elements[i] = new ElementConstantInteger(Integer.parseInt((String) currentToken.getValue()));
                        case DECIMALNUMBER -> elements[i] = new ElementConstantDouble(Double.parseDouble((String) currentToken.getValue()));
                    }
                    currentToken = smartScriptLexer.nextToken();
                    i++;
                }
                EchoNode echoNode = new EchoNode(elements);
                Node top = (Node) stack.peek();
                top.addChildNode(echoNode);
            }

            if(currentToken.getType() == TokenType.TAGDATE){
                currentToken = smartScriptLexer.nextToken();
                Element[] elements = new Element[1];
                if(currentToken.getType() == TokenType.STRING){
                    elements[0] = new ElementString((String)currentToken.getValue());
                }else{
                    throw new SmartScriptParserException("Invalid DATE tag.");
                }
                currentToken = smartScriptLexer.nextToken();
                if(currentToken.getType() != TokenType.TAGCLOSE){
                    throw new SmartScriptParserException("Invalid DATE tag.");
                }

                DateNode dateNode = new DateNode(elements);
                Node top = (Node) stack.peek();
                top.addChildNode(dateNode);
            }

            if(currentToken.getType() == TokenType.TEXT){
                TextNode textNode = new TextNode((String) currentToken.getValue());
                Node top = (Node) stack.peek();
                top.addChildNode(textNode);
            }

            if(currentToken.getType() == TokenType.TAGFOR){
                ElementVariable variable=null;
                Element startExpression=null;
                Element endExpression=null;
                Element stepExpression=null;

                currentToken = smartScriptLexer.nextToken();
                Element[] elements = new Element[20];   //Assuming that there will be 20 or fewer elements inside an empty tag
                int i = 0;
                while (currentToken.getType() != TokenType.TAGCLOSE){
                    switch (currentToken.getType()){
                        case VARIABLE -> elements[i] = new ElementVariable((String) currentToken.getValue());
                        case FUNCTION -> elements[i] = new ElementFunction((String) currentToken.getValue());
                        case SYMBOL -> elements[i] = new ElementOperator((String) currentToken.getValue());
                        case STRING -> elements[i] = new ElementString((String) currentToken.getValue());
                        case NUMBER -> elements[i] = new ElementConstantInteger(Integer.parseInt((String) currentToken.getValue()));
                        case DECIMALNUMBER -> elements[i] = new ElementConstantDouble(Double.parseDouble((String) currentToken.getValue()));
                    }
                    currentToken = smartScriptLexer.nextToken();
                    i++;
                }
                if(i<3 || i>4){
                    throw new SmartScriptParserException("Invalid FOR tag.");
                }else if(i==3){
                    variable = (ElementVariable) elements[0];
                    stepExpression = elements[1];
                    endExpression = elements[2];
                } else if (i==4) {
                    variable = (ElementVariable) elements[0];
                    startExpression = elements[1];
                    endExpression = elements[2];
                    stepExpression = elements[3];
                }

                ForLoopNode forLoopNode = new ForLoopNode(variable, startExpression, endExpression, stepExpression);
                Node top = (Node) stack.peek();
                top.addChildNode(forLoopNode);
                stack.push(forLoopNode);
            }

            if(currentToken.getType() == TokenType.TAGEND){
                if(stack.isEmpty()){
                    throw new SmartScriptParserException();
                }
                stack.pop();
                if(stack.isEmpty()){
                    throw new SmartScriptParserException();
                }
            }


            currentToken = smartScriptLexer.nextToken();
        }
    }

    /**
     * @return DocumentNode representing the script
     */
    public DocumentNode getDocumentNode() {
        return documentNode;
    }
}
