package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SymbolShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        HashMap<String, Character> symbols = new HashMap<String, Character>();
        symbols.put("PROMPT", env.getPromptSymbol());
        symbols.put("MORELINES", env.getMorelinesSymbol());
        symbols.put("MULTILINE", env.getMultilineSymbol());

        String[] args = arguments.trim().split("\\s+");

        if(args.length <1){
            env.writeln("No arguments given.");
            return ShellStatus.CONTINUE;
        }else if(args.length == 1){
            Character wantedSymbol = symbols.get(args[0]);
            if(wantedSymbol == null){
                env.writeln("The symbol does not exist.");
                return ShellStatus.CONTINUE;
            }
            env.writeln("The symbol for " + args[0] + " is '" + wantedSymbol + "'");

        } else if (args.length == 2) {
            Character wantedSymbol = symbols.get(args[0]);
            Character newSymbol = args[1].charAt(0);
            if(wantedSymbol == null){
                env.writeln("The symbol does not exist.");
                return ShellStatus.CONTINUE;
            }
            switch (args[0]){
                case "PROMPT" -> env.setPromptSymbol(newSymbol);
                case "MORELINES" -> env.setMorelinesSymbol(newSymbol);
                case "MULTILINE" -> env.setMultilineSymbol(newSymbol);
            }
            env.writeln("The symbol for " + args[0] + " is changed from " +
            "'"+wantedSymbol + "'" + " to " + "'" + newSymbol +"'");

        }else {
            env.writeln("Too many arguments.");
            return ShellStatus.CONTINUE;
        }


        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "symbol";
    }

    @Override
    public List<String> getCommandDescription() {
        ArrayList<String> cmdDescription = new ArrayList<String>();
        cmdDescription.add("With first argument <symbol_name> writes char");
        cmdDescription.add("used for this symbol.");
        cmdDescription.add("With two arguments <symbol_name> <new_char>, ");
        cmdDescription.add("changes the symbol to <new_char>");
        return Collections.unmodifiableList(cmdDescription);
    }
}
