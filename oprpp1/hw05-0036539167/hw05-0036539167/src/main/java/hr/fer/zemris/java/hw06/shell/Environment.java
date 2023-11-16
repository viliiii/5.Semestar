package hr.fer.zemris.java.hw06.shell;

import java.util.SortedMap;

public interface Environment {
    /**
     * Returns the String representation of what
     * the user has entered into the shell environment.
     * @return the String representation of what the user has entered into the shell environment
     * @throws ShellIOException if the input is invalid
     */
    String readLine() throws ShellIOException;

    /**
     * Writes the given text string to the user as the response
     * to the last command.
     * @param text  the text to be written
     * @throws ShellIOException .
     */
    void write(String text) throws ShellIOException;

    /**
     * Writes the given text string to the user as the response
     * to the last command and a newline character.
     * @param text  the text to be written
     * @throws ShellIOException .
     */
    void writeln(String text) throws ShellIOException;

    /**
     * Returns map of possible commands.
     * Key is the command name and value is the command class.
     * @return map of possible commands.
     */
    SortedMap<String, ShellCommand> commands();

    /**
     * @return current multiline symbol.
     */
    Character getMultilineSymbol();
    void setMultilineSymbol(Character symbol);

    /**
     * @return current prompt symbol.
     */
    Character getPromptSymbol();
    void setPromptSymbol(Character symbol);

    /**
     * @return current more lines symbol.
     */
    Character getMorelinesSymbol();
    void setMorelinesSymbol(Character symbol);

}
