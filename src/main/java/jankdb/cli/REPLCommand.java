package jankdb.cli;

import jankdb.Table;

public abstract class REPLCommand {
        public abstract void execute(String[] args, Table mainTable);
    
    public String name() {
        return this.getClass().getSimpleName().replace("Command", "").toUpperCase();
    }

    public String help() {
        return "No help available for " + name();
    }
}