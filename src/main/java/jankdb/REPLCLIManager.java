package jankdb;

import java.io.*;
import java.util.*;
import jankdb.cli.*;
import jankdb.helpers.*;

public class REPLCLIManager {
    private final TableRepository tableRepo;
    private final Map<String, REPLCommand> commands;
    private Table currentTable;

    public REPLCLIManager(TableRepository tableRepo) {
        this.tableRepo = tableRepo;
        this.currentTable = tableRepo.getOrCreateTable("main");
        this.commands = registerCommands();
    }

    private Map<String, REPLCommand> registerCommands() {
        Map<String, REPLCommand> cmds = new HashMap<>();
        cmds.put(CLICommandRegistry.BaseCommands.GET, new GetCommand());
        cmds.put(CLICommandRegistry.BaseCommands.SET, new SetCommand());
        cmds.put(CLICommandRegistry.BaseCommands.DEL, new DelCommand());
        cmds.put(CLICommandRegistry.BaseCommands.KEYS, new KeysCommand());
        cmds.put(CLICommandRegistry.BaseCommands.SAVE, new SaveCommand());
        cmds.put(CLICommandRegistry.BaseCommands.CLEAR, new ClearCommand());
        cmds.put(CLICommandRegistry.BaseCommands.HELP, new HelpCommand());
        cmds.put(CLICommandRegistry.BaseCommands.EXIT, new ExitCommand());
        cmds.put(CLICommandRegistry.BaseCommands.SELECT, new SelectCommand());
        cmds.put(CLICommandRegistry.BaseCommands.TABLES, new TablesCommand());
        return cmds;
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
        if (split.length == 0) return;

        String cmdName = split[0].toUpperCase();
        REPLCommand cmd = commands.get(cmdName);

        if (cmd != null) {
            CommandContext ctx = new CommandContext(
                isClient, out, currentTable, commands, tableRepo, clientKey);

            if (cmdName.equals(CLICommandRegistry.BaseCommands.SELECT)) {
                if (split.length == 2) {
                    Table newTable = tableRepo.getOrCreateTable(split[1]);
                    if (!newTable.tryLock(clientKey)) {
                        out.println("Table locked by: " + newTable.getLockOwner());
                        return;
                    }
                    if (currentTable.getLockOwner() != null && 
                        currentTable.getLockOwner().equals(clientKey)) {
                        currentTable.unlock(clientKey);
                    }
                    currentTable = newTable;
                    ctx.table = currentTable;
                }
            }

            cmd.Execute(split, ctx);

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

    private void InitDB() {
        try {
            currentTable.Load();
        } catch (Exception e) {
            System.out.println(CLICommandRegistry.Messages.TABLE_NOT_FOUND);
            currentTable.Save();
        } finally {
            System.out.println(CLICommandRegistry.Messages.TABLE_FOUND);
        }
    }

    private String[] SplitCommand(String command) {
        return command.trim().split("\\s+");
    }

    public Map<String, REPLCommand> getCommands() {
        return commands;
    }
}