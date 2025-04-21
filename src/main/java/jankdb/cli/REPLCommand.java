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
        // Check if table is null
        if (ctx.table == null) {
            ctx.println(CLICommandRegistry.Messages.TABLE_NULL_ERR);
            return false;
        }

        // Check command format
        if (arr.length != expectedSize) {
            ctx.println(CLICommandRegistry.Messages.INVALID_SIZE_ERR);
            ctx.println(formatMSG);
            return false;
        }

        if (requiresWriteLock()) {
            synchronized (ctx.table) {
                if (ctx.table.isLockedByOther(ctx.userKey)) {
                    ctx.println("Table is currently locked by: " + ctx.table.getLockOwner());
                    return false;
                }
                // Only try to lock if we don't already own it
                if (ctx.table.getLockOwner() == null || !ctx.table.getLockOwner().equals(ctx.userKey)) {
                    if (!ctx.table.tryLock(ctx.userKey)) {
                        ctx.println("Failed to acquire lock on table");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // Override this in commands that modify data
    protected boolean requiresWriteLock() {
        return false;
    }

}