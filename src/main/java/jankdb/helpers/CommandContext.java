package jankdb.helpers;

import java.io.PrintWriter;

import jankdb.Table;

public class CommandContext {
    public final boolean isClient;
    public final PrintWriter clientOut; // null if server
    public final Table table;

    public CommandContext(boolean isClient, PrintWriter clientOut, Table table) {
        this.isClient = isClient;
        this.clientOut = clientOut;
        this.table = table;
    }

    public void println(String message) {
        if (isClient && clientOut != null) {
            clientOut.println(message);
        } else {
            System.out.println(message);
        }
    }
}