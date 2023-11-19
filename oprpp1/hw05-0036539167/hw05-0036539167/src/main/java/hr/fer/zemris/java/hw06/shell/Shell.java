package hr.fer.zemris.java.hw06.shell;

import hr.fer.zemris.java.hw06.shell.commands.*;


import java.util.Objects;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

public class Shell implements Environment{

    //argumenti tipa scanner, ovaj simbol onaj simbol... trenutni status
    private Scanner scanner;
    ShellStatus status;
    private char PROMPT;
    private char MORELINES;
    private char MULTILINE;

    private final SortedMap<String, ShellCommand> commands = new TreeMap<>();

    {
        commands.put("charsets", new CharsetsShellCommand());
        commands.put("cat", new CatShellCommand());
        commands.put("ls", new LsShellCommand());
        commands.put("tree", new TreeShellCommand());
        commands.put("copy", new CopyShellCommand());
        commands.put("mkdir", new MkdirShellCommand());
        commands.put("hexdump", new HexdumpShellCommand());
        commands.put("symbol", new SymbolShellCommand());
        commands.put("exit", new ExitShellCommand());
        commands.put("help", new HelpShellCommand());
    }

    public Shell(){

        scanner = new Scanner(System.in);
        PROMPT = '>';
        MORELINES = '\\';
        MULTILINE = '|';
        status = ShellStatus.CONTINUE;
    }



    /**
     * Returns the String representation of what
     * the user has entered into the shell environment
     * as a single line string without MORELINES characters
     *
     * @return the String representation of what the user has entered into the shell environment
     * @throws ShellIOException if the input is invalid
     */
    @Override
    public String readLine() throws ShellIOException {

        StringBuilder sbInput = new StringBuilder();
        String line = "";
        while (true) {

            line = scanner.nextLine();
            if(Objects.equals(line, "")){
                //throw new ShellIOException("Missing MORELINES symbol.");
                //System.out.println("Invalid command line.");
                break;
            }
            if(!line.endsWith(String.valueOf(MORELINES))) {
                sbInput.append(" ").append(line.trim());
                break;
            }else {
                sbInput.append(" ").append(line, 0, line.length()-1);
                System.out.print(MULTILINE+ " ");
            }
        }

        return sbInput.toString().trim();
    }

    /**
     * Writes the given text string to the user as the response
     * to the last command.
     *
     * @param text the text to be written
     * @throws ShellIOException .
     */
    @Override
    public void write(String text) throws ShellIOException {
        System.out.print(text);
    }

    /**
     * Writes the given text string to the user as the response
     * to the last command and a newline character.
     *
     * @param text the text to be written
     * @throws ShellIOException .
     */
    @Override
    public void writeln(String text) throws ShellIOException {
        System.out.println(text);
    }

    /**
     * Returns map of possible commands.
     * Key is the command name and value is the command class.
     *
     * @return map of possible commands.
     */
    @Override
    public SortedMap<String, ShellCommand> commands() {
        return this.commands;
    }

    /**
     * @return current multiline symbol.
     */
    @Override
    public Character getMultilineSymbol() {
        return MULTILINE;
    }

    @Override
    public void setMultilineSymbol(Character symbol) {
        this.MULTILINE = symbol;
    }

    /**
     * @return current prompt symbol.
     */
    @Override
    public Character getPromptSymbol() {
        return PROMPT;
    }

    @Override
    public void setPromptSymbol(Character symbol) {
        this.PROMPT = symbol;
    }

    /**
     * @return current more lines symbol.
     */
    @Override
    public Character getMorelinesSymbol() {
        return MORELINES;
    }

    @Override
    public void setMorelinesSymbol(Character symbol) {
        this.MORELINES = symbol;
    }

}
