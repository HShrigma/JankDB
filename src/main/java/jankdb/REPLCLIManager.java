package jankdb;

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

    public void Start() {
        System.out.println(CLICommandRegistry.Messages.GREETING);
        Run();
    }

    void Run() {
        Scanner scanner = new Scanner(System.in);
        String command = InputSanitizer.sanitize(GetCommand(scanner));
        while (IsNotExit(command)) {
            if (!command.equals("")) {
                ParseCommand(command);
            }
            command = GetCommand(scanner);
        }
        scanner.close();
    }

    String GetCommand(Scanner scanner) {
        System.out.println();
        return scanner.nextLine();
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
