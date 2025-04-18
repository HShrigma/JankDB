package jankdb;

// import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import jankdb.helpers.*;

public class REPLCLIManager {

    Table mainTable;

    public REPLCLIManager() {
        mainTable = new Table("main");
    }

    public void Start() {
        System.out.println(CLICommandRegistry.Messages.GREETING);
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
        return command.split(" ");
    }

    void ProcessGetCommand(String[] splitCommand) {
        if (IsValidCommandSize(2, splitCommand, CLICommandRegistry.CommandSizeRules.GET)) {
            // Prints value of key if found
            String key = splitCommand[1];
            System.out.println(CLICommandRegistry.ExecutionMessages.GET_BEGIN_PREFIX + key
                    + CLICommandRegistry.ExecutionMessages.GET_BEGIN_SUFFIX);
            // if not found
            if (mainTable.FindByKey(key).isEmpty()) {
                System.out.println(CLICommandRegistry.ExecutionMessages.GENERIC_NOT_FOUND_PREFIX + key
                        + CLICommandRegistry.ExecutionMessages.GENERIC_NOT_FOUND_SUFFIX);
            } else { //if found
                System.out.println(CLICommandRegistry.ExecutionMessages.GET_FOUND + key);
                mainTable.FindByKey(key).forEach(match -> {
                    System.out.println(match.toString());
                });
            }
        }
    }

    void ProcessSetCommand(String[] splitCommand) {
        if (IsValidCommandSize(3, splitCommand, CLICommandRegistry.CommandSizeRules.SET)) {
            // Sets Key to Value if found
            String key = splitCommand[1];
            String value = splitCommand[2];
            List<Record> found = mainTable.FindByKey(key);
            if (found.isEmpty()) {
                AddToTable(key, value);
            } else {
                UpdateTable(found, key, value);
            }
        }
    }

    void AddToTable(String key, String value) {
        System.out.println(CLICommandRegistry.ExecutionMessages.SET_ADD_PREFIX + key
                + CLICommandRegistry.ExecutionMessages.SET_ADD_SUFFIX);

        try {
            Record r = new Record(key + "=" + value + ";");
            mainTable.AddRecord(r);
            System.out.println(CLICommandRegistry.ExecutionMessages.SET_ADD_SUCCESS);
        } catch (Exception f) {

            System.out.println(CLICommandRegistry.ExecutionMessages.SET_ADD_FAIL);
        }
    }

    void UpdateTable(List<Record> found, String key, String value) {
        try {
            for (Record record : found) {
                if (mainTable.GetRecords().contains(record)) {
                    System.out.println(CLICommandRegistry.ExecutionMessages.SET_UPDATE_PREFIX + key
                            + CLICommandRegistry.ExecutionMessages.SET_UPDATE_SUFFIX);
                    int index = mainTable.GetRecords().indexOf(record);
                    record.AddKvP(key, value);
                    mainTable.UpdateRecord(index, record);
                    System.out.println(CLICommandRegistry.ExecutionMessages.SET_UPDATE_SUCCESS);
                }
            }
        } catch (Exception e) {
            System.out.println(CLICommandRegistry.ExecutionMessages.SET_UPDATE_FAIL);
        }
    }

    void ProcessDeleteCommand(String[] splitCommand) {
        if (IsValidCommandSize(2, splitCommand, CLICommandRegistry.CommandSizeRules.DEL)) {
            // Deletes Key if found
            String delKey = splitCommand[1];
            System.out.println(CLICommandRegistry.ExecutionMessages.DEL_BEGIN_PREFIX
                    + delKey
                    + CLICommandRegistry.ExecutionMessages.DEL_BEGIN_SUFFIX);

            // search for key
            for (Record record : mainTable.GetRecords()) {
                // safely try deleting if found
                if (record.GetData().containsKey(delKey)) {
                    System.out.println(CLICommandRegistry.ExecutionMessages.DEL_FOUND_PREFIX + delKey
                            + CLICommandRegistry.ExecutionMessages.DEL_FOUND_SUFFIX);
                    try {
                        record.DeleteByKey(delKey);
                        System.out.println(CLICommandRegistry.ExecutionMessages.DEL_DELETED_SUCCESS);
                        return;
                    } catch (Exception e) {
                        System.err.println(CLICommandRegistry.ExecutionMessages.DEL_DELETED_FAIL);
                        return;
                    }
                }
            }
            System.out.println(CLICommandRegistry.ExecutionMessages.GENERIC_NOT_FOUND_PREFIX
                    + delKey
                    + CLICommandRegistry.ExecutionMessages.GENERIC_NOT_FOUND_SUFFIX);
        }
    }

    void ProcessKeysCommand(String[] splitCommand) {
        if (IsValidCommandSize(1, splitCommand, CLICommandRegistry.CommandSizeRules.KEYS)) {
            // Lists all Keys
            System.out.println(CLICommandRegistry.ExecutionMessages.KEYS_BEGIN);
            // Get keys
            List<Record> records = mainTable.GetRecords();
            boolean found = false;
            // print each key
            if (!records.isEmpty()) {
                for (Record record : records) {
                    Map<String, String> KvPs = record.GetData();
                    for (String key : KvPs.keySet()) {
                        System.out.println("key: " + key);
                        found = true;
                    }
                }
            }
            if (found) {
                System.out.println(CLICommandRegistry.ExecutionMessages.KEYS_SUCCESS);
                return;
            }

            System.out.println(CLICommandRegistry.ExecutionMessages.KEYS_NONE);
        }
    }

    void ProcessSaveCommand(String[] splitCommand) {
        if (IsValidCommandSize(1, splitCommand, CLICommandRegistry.CommandSizeRules.SAVE)) {
            // Saves DB to persistent storage
            System.out.println(CLICommandRegistry.ExecutionMessages.SAVE_BEGIN);
            try {
                mainTable.Save();
                System.out.println(CLICommandRegistry.ExecutionMessages.SAVE_SUCCESS);
            } catch (Exception e) {
                System.out.println(CLICommandRegistry.ExecutionMessages.SAVE_FAIL);
            }

        }
    }

    void ProcessClearCommand(String[] splitCommand) {
        if (IsValidCommandSize(1, splitCommand, CLICommandRegistry.CommandSizeRules.CLEAR)) {
            // Clears All Table Items
            System.out.println(CLICommandRegistry.ExecutionMessages.CLEAR_BEGIN);
            try {
                mainTable.Flush();
                System.out.println(CLICommandRegistry.ExecutionMessages.CLEAR_SUCCESS);

            } catch (Exception e) {
                System.out.println(CLICommandRegistry.ExecutionMessages.CLEAR_FAIL);
            }
        }
    }

    void ProcessHelpCommand(String[] splitCommand) {
        if (IsValidCommandSize(1, splitCommand, CLICommandRegistry.CommandSizeRules.HELP)) {
            // prints All command Guidelines
            System.out.println(CLICommandRegistry.ExecutionMessages.HELP);
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
            System.out.println(CLICommandRegistry.ExecutionMessages.EXIT);
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