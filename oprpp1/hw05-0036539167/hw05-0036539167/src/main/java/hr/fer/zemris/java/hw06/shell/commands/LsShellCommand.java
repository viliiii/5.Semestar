package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LsShellCommand implements ShellCommand {
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
        if(!Files.isDirectory(path)){
            env.writeln("Path is not a directory.");
            return ShellStatus.CONTINUE;
        }



        try {
            List<Path> filesInDirectory = Files.list(path).toList();
            StringBuilder sb = new StringBuilder();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for(Path file : filesInDirectory){

                sb.append(Files.isDirectory(file) ? "d" : "-");
                sb.append(Files.isReadable(file) ? "r" : "-");
                sb.append(Files.isWritable(file) ? "w" : "-");
                sb.append(Files.isExecutable(file) ? "x" : "-");

                sb.append(String.format("%10d ", Files.size(file)));

                BasicFileAttributeView faView = Files.getFileAttributeView(
                        file, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
                );
                BasicFileAttributes attributes = faView.readAttributes();
                FileTime fileTime = attributes.creationTime();
                String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
                sb.append(formattedDateTime).append(" ");

                sb.append(file.getFileName());

                env.writeln(sb.toString());
                sb.setLength(0);

            }

        } catch (IOException e) {
            env.writeln("Error: " + e.getMessage());
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "ls";
    }

    @Override
    public List<String> getCommandDescription() {
        ArrayList<String> cmdDescription = new ArrayList<String>();
        cmdDescription.add("Command ls takes a single argument – directory – and writes a directory listing (not recursive).");
        cmdDescription.add("The output consists of 4 columns. First column indicates if current object is directory (d),");
        cmdDescription.add("readable (r)," +
                "writable (w) and executable (x).");
        cmdDescription.add("Second column contains object size in bytes that is right aligned and" +
                "occupies 10 characters.");
        cmdDescription.add("Follows file creation date/time and finally file name.");
        return Collections.unmodifiableList(cmdDescription);
    }
}
