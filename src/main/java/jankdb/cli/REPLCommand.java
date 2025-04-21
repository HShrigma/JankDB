package jankdb.cli;

import jankdb.helpers.CLICommandRegistry;
import jankdb.helpers.CommandContext;

public abstract class REPLCommand {
    public abstract void Execute(String[] args, CommandContext ctx);

    public String name() {
        return this.getClass().getSimpleName().replace("Command", "").toUpperCase();
    }

    public String Help() {
        return "No help available for " + name();
    }

    protected boolean IsValidCommand(int expectedSize, String[] arr, String formatMSG, CommandContext ctx) {
        if (arr.length == expectedSize && ctx.table != null)
            return true;

        if (ctx.table == null)
            ctx.println(CLICommandRegistry.Messages.TABLE_NULL_ERR);

        if (arr.length != expectedSize) {
            ctx.println(CLICommandRegistry.Messages.INVALID_SIZE_ERR);
            ctx.println(formatMSG);
        }
        return false;
    }
}