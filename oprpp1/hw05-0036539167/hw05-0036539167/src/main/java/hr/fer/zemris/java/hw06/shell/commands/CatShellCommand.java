package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CatShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] split = arguments.split(" ");
        if(split.length <1) {
            env.writeln("No path specified.");
            return ShellStatus.CONTINUE;
        }
        //---
        String strFrom = "";
        boolean inside = false;
        char[] arr = arguments.toCharArray();
        int i=0;
        //from
        for(; i<arr.length; i++) {
            if(arr[i] == '"' && !inside) {
                inside = true;
                ++i;
            }
            if(arr[i] == '"' && inside) {
                inside = false;
                ++i;
                break;
            }
            if(arr[i] == ' ' && !inside) break;
            strFrom+=String.valueOf(arr[i]);
        }

        //skip blanks
        for(; i<arr.length; i++){
            if(arr[i] != ' ') break;
        }

        //to
        String arg = "";

        for(; i<arr.length; i++) {
            if(arr[i] == '"' && !inside) {
                inside = true;
                ++i;
            }
            if(arr[i] == '"' && inside) {
                inside = false;
                ++i;
                break;
            }
            //if(arr[i] == ' ' && !inside) break;
            arg+=String.valueOf(arr[i]);
        }
        
        //---
        String charset = "UTF-8";
        if (arg != "") {
            charset = arg;
        }
        String path = strFrom;
        
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new BufferedInputStream(
                                    Files.newInputStream(Path.of(path))),charset));
            String line;
            while ((line = br.readLine()) != null) {
                env.writeln(line);
            }
        } catch (IOException e) {
            env.writeln("Could not open file.");
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "cat";
    }

    @Override
    public List<String> getCommandDescription() {
        ArrayList<String> cmdDescription = new ArrayList<String>();
        cmdDescription.add("This command opens given file and writes its content to console.");
        cmdDescription.add("The first argument is path to some file and is mandatory");
        cmdDescription.add("The second argument ischarset name that should be used to interpret chars from bytes.");
        cmdDescription = (ArrayList<String>) Collections.unmodifiableList(cmdDescription);
        return cmdDescription;
    }
}
