package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HexdumpShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        Path path;
        arguments = arguments.trim();
        arguments = arguments.replaceAll("\"", "");
        try {
            path = Paths.get(arguments);
        }catch (Exception e){
            env.writeln("Error: " + arguments);
            return ShellStatus.CONTINUE;
        }
        if(Files.isDirectory(path)){
            env.writeln("Path is a directory. Expected file.");
            return ShellStatus.CONTINUE;
        }
        byte[] bytes = new byte[8192];
        try(FileInputStream inputStream = new FileInputStream(path.toString());) {
            bytes = inputStream.readAllBytes();
        } catch (IOException e) {
            env.writeln("Failed to read file: " + path.toString());
            return ShellStatus.CONTINUE;
        }
        //print
        //----------------------------------------------------------------
        int indLines = bytes.length/32 + 1;
        int indHex = 0;
        int indChar = 0;
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<indLines; i++) {
            String hexLine = String.format("%x", i*16);
            sb.append("0".repeat(8-hexLine.length()));
            sb.append(hexLine);
            sb.append(":");

            for(int j=0; j<8; j++) {
                sb.append(" ");
                if(indHex< bytes.length){
                    sb.append(String.format("%02x", bytes[indHex++]).toUpperCase());
                }else {
                    sb.append("  ");
                }
            }

            sb.append("|");

            for(int j=0; j<8; j++) {
                if(indHex< bytes.length){
                    sb.append(String.format("%02x", bytes[indHex++]).toUpperCase());
                }else {
                    sb.append("  ");
                }
                sb.append(" ");
            }

            sb.append("| ");

            for(int j=0; j<16; j++) {
                if(indChar< bytes.length){
                    if(bytes[indChar] < 32 || bytes[indChar] > 127){
                        sb.append(".");
                        indChar++;
                    }
                    else{
                        sb.append((char)bytes[indChar++]);
                    }

                }else {
                    sb.append(" ");
                }
            }
            sb.append("\n");

        }

        env.writeln(sb.toString());


        //----------------------------------------------------------------
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "hexdump";
    }

    @Override
    public List<String> getCommandDescription() {
        ArrayList<String> cmdDescription = new ArrayList<String>();
        cmdDescription.add("The hexdump command expects a single argument: ");
        cmdDescription.add("file name, and produces hex-output .");
        return Collections.unmodifiableList(cmdDescription);
    }
}
