package hr.fer.zemris.java.hw06.shell;

public class MyShell {
/*
    java -cp target\classes src/main/java/hr/fer/zemris/java/hw06/shell/MyShell.java
*/

    public static String extractCommand(String line){

        return line.split(" ")[0];
    }

    /**
     * @param line the command line
     * @return the command line without function name, arguments
     * are seperated by spaces
     */
    public static String extractArgs(String line){
        StringBuilder sb = new StringBuilder();

        String[] split = line.trim().split(" ");
        for(int i=1; i<split.length; i++){
            if(i == split.length - 1){
                sb.append(split[i]);
            }else {
                sb.append(split[i]).append(" ");
            }

        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Shell shell = new Shell();

        shell.writeln("Welcome to MyShell v 1.0");
        String line;
        ShellStatus status = ShellStatus.CONTINUE;
        do{
            System.out.print(shell.getPromptSymbol() + " ");
            line = shell.readLine();
            String commandName = extractCommand(line);
            String arguments = extractArgs(line);

            ShellCommand command = shell.commands().get(commandName);
            if(command == null){
                shell.writeln("No command named: " + commandName);
            }else {
                status = command.executeCommand(shell, arguments);
            }
        }while (status != ShellStatus.TERMINATE);
    }
}
