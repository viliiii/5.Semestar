package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HelpShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        arguments = arguments.trim();

        String[] args = arguments.split("\\s+");

        if(arguments.isEmpty()){
            env.commands().keySet().forEach(env::writeln);
        }else if(args.length == 1){
            ShellCommand command = env.commands().get(args[0]);
            if (command == null){
                env.writeln("Command not found.");
                return ShellStatus.CONTINUE;
            }
            env.writeln(command.getCommandName());

            List<String> list = command.getCommandDescription();

            for(String s: list) {
                env.writeln(s);
            }
        }


        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public List<String> getCommandDescription() {
        ArrayList<String> cmdDescription = new ArrayList<String>();
        cmdDescription.add("Lists all the commands if started with no arguments.");
        cmdDescription.add("With argument <command_name> , writes description for");
        cmdDescription.add(" specified command.");
        return Collections.unmodifiableList(cmdDescription);
    }
}
