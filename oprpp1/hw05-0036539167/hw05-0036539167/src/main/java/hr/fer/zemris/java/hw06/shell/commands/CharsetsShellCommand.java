package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharsetsShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        arguments = arguments.trim();
        if(!arguments.isEmpty()) {
            env.writeln("Too many arguments.");
            return  ShellStatus.CONTINUE;
        }
        Charset.availableCharsets().forEach((s, c) -> env.writeln(s));

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "charsets";
    }

    @Override
    public List<String> getCommandDescription() {
        ArrayList<String> cmdDescription = new ArrayList<String>();
        cmdDescription.add("Command charsets takes no arguments and lists names of supported charsets for your Java platform.");
        cmdDescription.add("A single charset name is written per line.");
        return Collections.unmodifiableList(cmdDescription);
    }
}