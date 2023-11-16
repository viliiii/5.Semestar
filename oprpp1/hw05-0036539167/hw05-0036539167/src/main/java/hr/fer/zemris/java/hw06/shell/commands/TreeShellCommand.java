package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeShellCommand implements ShellCommand {

    public static void tree(File path, int lvl, Environment e){
        File[] children = path.listFiles();
        if(children == null) return;

        for (File child : children) {
            e.writeln(" ".repeat(lvl) + child.getName());
            tree(child, lvl+2, e);
        }
    }
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {


        arguments = arguments.replaceAll("\"", "");
        Path path;
        try {
            path = Paths.get(arguments);
        }catch (Exception e){
            env.writeln("Error: " + arguments);
            return ShellStatus.CONTINUE;
        }
        if(!Files.isDirectory(path)){
            env.writeln("Path is not a directory.");
            return ShellStatus.CONTINUE;
        }

        tree(path.toFile(), 0, env);


        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {

        return "tree";
    }

    @Override
    public List<String> getCommandDescription() {
        ArrayList<String> cmdDescription = new ArrayList<String>();
        cmdDescription.add("The tree command expects a single argument: directory name ");
        cmdDescription.add(" and prints a tree (each directory level shifts" + "output two charatcers to the right).");
        cmdDescription = (ArrayList<String>) Collections.unmodifiableList(cmdDescription);
        return cmdDescription;
    }
}
