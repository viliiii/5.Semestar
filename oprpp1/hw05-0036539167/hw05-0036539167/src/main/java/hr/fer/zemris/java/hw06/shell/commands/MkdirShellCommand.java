package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MkdirShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        Path path;
        arguments = arguments.trim();
        try {
            path = Paths.get(arguments);
        }catch (Exception e){
            env.writeln("Error: " + arguments);
            return ShellStatus.CONTINUE;
        }
        if(Files.exists(path)){
            env.writeln("Directory already exists: " + path);
            return ShellStatus.CONTINUE;
        }

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            env.writeln("Could not create directory: " + path + " , IO exception.");
        }


        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "mkdir";
    }

    @Override
    public List<String> getCommandDescription() {
        ArrayList<String> cmdDescription = new ArrayList<String>();
        cmdDescription.add("The mkdir command takes a single argument: directory name, ");
        cmdDescription.add(" and creates the appropriate directory" +
                "structure.");
        return Collections.unmodifiableList(cmdDescription);
    }
}
