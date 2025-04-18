package jankdb.cli;

import jankdb.Table;
import jankdb.helpers.CLICommandRegistry;

public abstract class REPLCommand {
    public abstract void Execute(String[] args, Table mainTable);

    public String name() {
        return this.getClass().getSimpleName().replace("Command", "").toUpperCase();
    }

    public String Help() {
        return "No help available for " + name();
    }

    protected boolean IsValidCommandSize(int expectedSize, String[] arr, String formatMSG) {
        if (arr.length == expectedSize)
            return true;

        System.err.println(CLICommandRegistry.Messages.INVALID_SIZE_ERR);
        System.out.println(formatMSG);

        return false;
    }
}