package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExitShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        return ShellStatus.TERMINATE;
    }

    @Override
    public String getCommandName() {
        return "exit";
    }

    @Override
    public List<String> getCommandDescription() {
        ArrayList<String> cmdDescription = new ArrayList<String>();
        cmdDescription.add("Closes the shell.");
        return Collections.unmodifiableList(cmdDescription);
    }
}
