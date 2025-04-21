package jankdb.helpers;

import jankdb.Table;
import jankdb.TableRepository;
import jankdb.cli.REPLCommand;
import java.io.PrintWriter;
import java.util.Map;

public class CommandContext {
    public final boolean isClient;
    public final PrintWriter clientOut;
    public Table table;
    public final Map<String, REPLCommand> commands;
    public final TableRepository tableRepo;
    public final String userKey;

    public CommandContext(boolean isClient, PrintWriter clientOut, 
                         Table table, Map<String, REPLCommand> commands,
                         TableRepository tableRepo, String userKey) {
        this.isClient = isClient;
        this.clientOut = clientOut;
        this.table = table;
        this.commands = commands;
        this.tableRepo = tableRepo;
        this.userKey = userKey;
    }

    public void println(String message) {
        if (isClient && clientOut != null) {
            clientOut.println(message);
        } else {
            System.out.println(message);
        }
    }
}