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
        System.out.println("\n[DEBUG] Validating command: " + String.join(" ", arr));
        System.out.println("[DEBUG] Current lock owner: " + 
                          (ctx.table.getLockOwner() != null ? ctx.table.getLockOwner() : "none"));
        System.out.println("[DEBUG] Current user: " + ctx.userKey);
        
        if (ctx.table == null) {
            ctx.println(CLICommandRegistry.Messages.TABLE_NULL_ERR);
            return false;
        }
        
        if (arr.length != expectedSize) {
            ctx.println(CLICommandRegistry.Messages.INVALID_SIZE_ERR);
            ctx.println(formatMSG);
            return false;
        }
    
        if (requiresWriteLock()) {
            synchronized (ctx.table) {
                // First check if locked by someone else
                if (ctx.table.isLockedByOther(ctx.userKey)) {
                    String msg = "Table locked by: " + ctx.table.getLockOwner();
                    ctx.println(msg);
                    return false;
                }
                
                // If we get here, either:
                // 1. Table is unlocked, or
                // 2. We already own the lock
                if (ctx.table.getLockOwner() == null) {
                    if (!ctx.table.tryLock(ctx.userKey)) {
                        ctx.println("Could not acquire table lock");
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