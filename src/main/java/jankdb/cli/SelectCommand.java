package jankdb.cli;

import java.util.concurrent.TimeUnit;

import jankdb.Table;
import jankdb.helpers.*;

public class SelectCommand extends REPLCommand {
    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if (IsValidCommand(2, args, CLICommandRegistry.CommandSizeRules.SELECT, ctx)) {
            String tableName = args[1];

            try {
                Table selected = ctx.tableRepo.getOrCreateTable(tableName);

                // Try to acquire lock on new table
                if (!selected.tryLock(ctx.userKey, 2, TimeUnit.SECONDS)) {
                    ctx.println("Cannot select table. It is currently locked by: " +
                            selected.getLockOwner());
                    return;
                }

                // Release old table lock if we had it
                if (ctx.table != null && ctx.table.getLockOwner() != null &&
                        ctx.table.getLockOwner().equals(ctx.userKey)) {
                    ctx.table.unlock(ctx.userKey);
                }

                // Switch tables
                ctx.table = selected;
                ctx.println("Selected table: " + tableName);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                ctx.println("Selection interrupted");
            }
        }
    }

    @Override
    protected boolean requiresWriteLock() {
        return true;
    }
}