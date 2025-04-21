package jankdb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import jankdb.cli.*;
import jankdb.helpers.*;

public class REPLCLIManager {

    private final Table mainTable;
    private final Map<String, REPLCommand> commands;

    public REPLCLIManager() {
        mainTable = new Table("main");
        commands = new HashMap<>();

        // Register commands
        commands.put(CLICommandRegistry.BaseCommands.GET, new GetCommand());
        commands.put(CLICommandRegistry.BaseCommands.SET, new SetCommand());
        commands.put(CLICommandRegistry.BaseCommands.DEL, new DelCommand());
        commands.put(CLICommandRegistry.BaseCommands.KEYS, new KeysCommand());
        commands.put(CLICommandRegistry.BaseCommands.SAVE, new SaveCommand());
        commands.put(CLICommandRegistry.BaseCommands.CLEAR, new ClearCommand());
        commands.put(CLICommandRegistry.BaseCommands.HELP, new HelpCommand());
        commands.put(CLICommandRegistry.BaseCommands.EXIT, new ExitCommand());
    }

    public void StartServerSide() {
        System.out.println(CLICommandRegistry.Messages.GREETING);
        try {
            Run(null, null, false, null);
        } catch (IOException e) {
            System.err.println("REPLCLIManager:StartServerSide: Error trying to start on server side");
            e.printStackTrace();
        }
    }

    public void StartClientSide(PrintWriter out, BufferedReader in) throws IOException {
        StartClientSide(out, in, null);
    }

    public void StartClientSide(PrintWriter out, BufferedReader in, String clientKey) throws IOException {
        out.println(CLICommandRegistry.Messages.GREETING);
        Run(out, in, true, clientKey);
    }

    private void Run(PrintWriter out, BufferedReader in, boolean isClient, String clientKey) throws IOException {
        InitDB();

        Scanner scanner = null;
        if (!isClient) {
            scanner = new Scanner(System.in);
        }

        String command = getCommand(isClient, out, in, scanner);
        while (IsNotExit(command)) {
            if (!command.isEmpty()) {
                executeCommand(command, isClient, out, clientKey);
            }
            command = getCommand(isClient, out, in, scanner);
        }

        if (scanner != null)
            scanner.close();
    }

    private String getCommand(boolean isClient, PrintWriter out, BufferedReader in, Scanner scanner)
            throws IOException {
        if (isClient) {
            out.println(CLICommandRegistry.Messages.REQ_COMMAND);
            out.println();
            return InputSanitizer.sanitize(in.readLine());
        } else {
            System.out.println(CLICommandRegistry.Messages.REQ_COMMAND);
            System.out.println();
            return InputSanitizer.sanitize(scanner.nextLine());
        }
    }

    private boolean IsNotExit(String command) {
        return !command.equalsIgnoreCase(CLICommandRegistry.BaseCommands.EXIT);
    }

    private void executeCommand(String command, boolean isClient, PrintWriter out, String clientKey) {
        String[] split = SplitCommand(command);
        if (split.length == 0)
            return;

        REPLCommand cmd = commands.get(split[0].toUpperCase());

        if (cmd != null) {
            CommandContext ctx = new CommandContext(isClient, out, mainTable);
            cmd.Execute(split, mainTable, ctx);

            if (!isClient) {
                System.out.println("[server log] Executed: " + command);
            } else if (clientKey != null) {
                System.out.println("[server log] Client " + clientKey + " executed: " + command);
            }

        } else {
            if (isClient) {
                out.println("Unknown command: " + split[0]);
            } else {
                System.out.println("Unknown command: " + split[0]);
            }
        }
    }

    public Table getMainTable() {
        return mainTable;
    }

    private void InitDB() {
        try {
            mainTable.Load();
        } catch (Exception e) {
            System.out.println(CLICommandRegistry.Messages.TABLE_NOT_FOUND);
            mainTable.Save();
        } finally {
            System.out.println(CLICommandRegistry.Messages.TABLE_FOUND);
        }
    }

    private String[] SplitCommand(String command) {
        return command.trim().split("\\s+");
    }
}
