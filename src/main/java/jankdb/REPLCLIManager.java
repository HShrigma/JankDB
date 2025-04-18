package jankdb;

import java.util.Scanner;

import jankdb.helpers.*;

public class REPLCLIManager {

    Table mainTable;

    public REPLCLIManager() {
        mainTable = new Table("main");
    }

    public void Start() {
        System.out.println(CLICommandRegistry.Messages.GREETING);
        mainTable.Load();
        Run();
    }

    void Run() {
        Scanner scanner = new Scanner(System.in);
        String command = GetCommand(scanner);
        // run loop by evaluating command
        while (IsNotExit(command)) {
            // Parse the command
            ParseCommand(command);
            // Get new command
            command = GetCommand(scanner);
        }

        scanner.close();
    }

    String GetCommand(Scanner scanner) {
        System.err.println();
        return scanner.nextLine();
    }

    boolean IsNotExit(String command) {
        return !command.equals(CLICommandRegistry.BaseCommands.EXIT);
    }

    void ParseCommand(String command) {
        String[] split = SplitCommand(command);
        switch (split[0]) {
            case CLICommandRegistry.BaseCommands.GET:
                ProcessGetCommand(split);
                break;
            case CLICommandRegistry.BaseCommands.SET:
                ProcessSetCommand(split);
                break;
            case CLICommandRegistry.BaseCommands.DEL:
                ProcessDeleteCommand(split);
                break;
            case CLICommandRegistry.BaseCommands.KEYS:
                ProcessKeysCommand(split);
                break;
            case CLICommandRegistry.BaseCommands.SAVE:
                ProcessSaveCommand(split);
                break;
            case CLICommandRegistry.BaseCommands.CLEAR:
                ProcessClearCommand(split);
                break;
            case CLICommandRegistry.BaseCommands.HELP:
                ProcessHelpCommand(split);
                break;
            case CLICommandRegistry.BaseCommands.EXIT:
                ProcessExitCommand(split);
                break;
            default:
                break;
        }
    }

    String[] SplitCommand(String command) {
        return command.split(" ");
    }

    void ProcessGetCommand(String[] splitCommand) {
        if (IsValidCommandSize(2, splitCommand, CLICommandRegistry.CommandSizeRules.GET)) {
            // Prints value of key if found
        }
    }

    void ProcessSetCommand(String[] splitCommand) {
        if (IsValidCommandSize(3, splitCommand, CLICommandRegistry.CommandSizeRules.SET)) {
            // Sets Key to Value if found
        }
    }

    void ProcessDeleteCommand(String[] splitCommand) {
        if (IsValidCommandSize(2, splitCommand, CLICommandRegistry.CommandSizeRules.DEL)) {
            // Deletes Key if found
        }
    }

    void ProcessKeysCommand(String[] splitCommand) {
        if (IsValidCommandSize(1, splitCommand, CLICommandRegistry.CommandSizeRules.KEYS)) {
            // Lists all Keys
        }
    }

    void ProcessSaveCommand(String[] splitCommand) {
        if (IsValidCommandSize(1, splitCommand, CLICommandRegistry.CommandSizeRules.SAVE)) {
            // Saves DB to persistent storage
        }
    }

    void ProcessClearCommand(String[] splitCommand) {
        if (IsValidCommandSize(1, splitCommand, CLICommandRegistry.CommandSizeRules.CLEAR)) {
            // Clears All Table Items
            
        }
    }

    void ProcessHelpCommand(String[] splitCommand) {
        if (IsValidCommandSize(1, splitCommand, CLICommandRegistry.CommandSizeRules.HELP)) {
            // prints All command Guidelines
            System.err.println(CLICommandRegistry.CommandGuides.GET);
            System.err.println(CLICommandRegistry.CommandGuides.SET);
            System.err.println(CLICommandRegistry.CommandGuides.DEL);
            System.err.println(CLICommandRegistry.CommandGuides.KEYS);
            System.err.println(CLICommandRegistry.CommandGuides.SAVE);
            System.err.println(CLICommandRegistry.CommandGuides.CLEAR);
            System.err.println(CLICommandRegistry.CommandGuides.HELP);
            System.err.println(CLICommandRegistry.CommandGuides.EXIT);
        }
    }

    void ProcessExitCommand(String[] splitCommand) {
        if (IsValidCommandSize(1, splitCommand, CLICommandRegistry.CommandSizeRules.EXIT)) {
            // prints goodbye
            System.out.println(CLICommandRegistry.Messages.GOODBYE);
        }
    }

    boolean IsValidCommandSize(int expectedSize, String[] arr, String formatMSG) {
        if (arr.length == expectedSize)
            return true;

        System.err.println(CLICommandRegistry.Messages.INVALID_SIZE_ERR);
        System.out.println(formatMSG);

        return false;
    }
}