package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CopyShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        String strFrom = "";
        String strTo = "";
        if(!arguments.contains("\"")){
            String[] split = arguments.split(" ");
            if(split.length != 2){
                env.writeln("Invalid arguments. Expected 2 paths.");
                return ShellStatus.CONTINUE;
            }
            strFrom = split[0];
            strTo = split[1];
        }else{
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
                strTo+=String.valueOf(arr[i]);
            }
        }

        Path from = Paths.get(strFrom);
        Path to = Paths.get(strTo);



        if(Files.isDirectory(to)){
            to = Paths.get(to.toString() + "\\" + from.getFileName());
        }
        if(Files.exists(to)){
            env.writeln("File with path" + to.toString() +
                    "exists. Overwrite it? Y/N" );
            if(Objects.equals(env.readLine(), "N")){
                return ShellStatus.CONTINUE;
            }
        }



        try(FileInputStream inputStream = new FileInputStream(from.toString());
            FileOutputStream outputStream = new FileOutputStream(to.toString());) {
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer);
            }


        } catch (IOException e) {
            env.writeln("Failed to copy file: " + from.toString());
            return ShellStatus.CONTINUE;
        }


        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "copy";
    }

    @Override
    public List<String> getCommandDescription() {
        ArrayList<String> cmdDescription = new ArrayList<String>();
        cmdDescription.add("The copy command expects two arguments: ");
        cmdDescription.add("source file name and destination file name (i.e. paths and" +"names).");
        cmdDescription.add("Id the destination file exists, user will be" +
                "asked for overwriting it.");
        cmdDescription = (ArrayList<String>) Collections.unmodifiableList(cmdDescription);
        return cmdDescription;
    }
}
