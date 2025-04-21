package jankdb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import jankdb.helpers.*;
import jankdb.cli.*;

public class REPLCLIManager {

    Table mainTable;
    Map<String, REPLCommand> commands;

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
        RunServerSide();
    }

    void RunServerSide() {
        InitDB();
        Scanner scanner = new Scanner(System.in);
        String command = GetCommand(scanner);
        while (IsNotExit(command)) {
            if (!command.equals("")) {
                ParseCommand(command);
            }
            command = GetCommand(scanner);
        }
        scanner.close();
    }

    public void StartClientSide(PrintWriter out, BufferedReader in) throws IOException {
        out.println(CLICommandRegistry.Messages.GREETING);
        RunClientSide(out, in);
    }

    void RunClientSide(PrintWriter out, BufferedReader in) throws IOException {
        InitDB();
        String command = GetCommandClientSide(out, in);
        while (IsNotExit(command)) {
            if (!command.equals("")) {
                ParseCommandClientSide(command, out, in);
            }
            command = GetCommandClientSide(out, in);
        }
    }

    String GetCommand(Scanner scanner) {
        System.out.println(CLICommandRegistry.Messages.REQ_COMMAND);
        System.out.println();
        return InputSanitizer.sanitize(scanner.nextLine());
    }

    String GetCommandClientSide(PrintWriter out, BufferedReader in) throws IOException {
        out.println(CLICommandRegistry.Messages.REQ_COMMAND);
        out.println();
        return InputSanitizer.sanitize(in.readLine());
    }

    boolean IsNotExit(String command) {
        return !command.equals(CLICommandRegistry.BaseCommands.EXIT);
    }

    void ParseCommand(String command) {
        String[] split = SplitCommand(command);
        if (split.length == 0)
            return;

        REPLCommand cmd = commands.get(split[0]);
        if (cmd != null) {
            cmd.Execute(split, mainTable);
        } else {
            System.out.println("Unknown command: " + split[0]);
        }
    }

    void ParseCommandClientSide(String command, PrintWriter out, BufferedReader in) throws IOException {
        String[] split = SplitCommand(command);
        if (split.length == 0)
            return;

        REPLCommand cmd = commands.get(split[0]);
        if (cmd != null) {
            cmd.ExecuteClientSide(split, mainTable, out);
        } else {
            out.println("Unknown command: " + split[0]);
        }
    }

    void InitDB() {
        try {
            mainTable.Load();
        } catch (Exception e) {
            System.out.println(CLICommandRegistry.Messages.TABLE_NOT_FOUND);
            mainTable.Save();
        } finally {
            System.out.println(CLICommandRegistry.Messages.TABLE_FOUND);
        }
    }

    String[] SplitCommand(String command) {
        return command.trim().split("\\s+");
    }
}
