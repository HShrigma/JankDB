package jankdb.helpers;

import java.io.PrintWriter;
import java.util.Map;

import jankdb.Table;
import jankdb.cli.REPLCommand;

public class CommandContext {
    public final boolean isClient;
    public final PrintWriter clientOut; // null if server
    public Table table;
    public final Map<String, REPLCommand> commands;
    public final Map<String, Table> tables;
    public String userKey;

    public CommandContext(boolean isClient, PrintWriter clientOut, Table table, Map<String, REPLCommand> commands,
            Map<String, Table> tables, String userKey) {
        this.isClient = isClient;
        this.clientOut = clientOut;
        this.table = table;
        this.commands = commands;
        this.tables = tables;
        this.userKey = userKey;
    }
    public CommandContext(boolean isClient, PrintWriter clientOut, Table table, Map<String, REPLCommand> commands,
            Map<String, Table> tables) {
        this.isClient = isClient;
        this.clientOut = clientOut;
        this.table = table;
        this.commands = commands;
        this.tables = tables;
        this.userKey = null;
    }

    public void println(String message) {
        if (isClient && clientOut != null) {
            clientOut.println(message);
        } else {
            System.out.println(message);
        }
    }
}