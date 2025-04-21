package jankdb.cli;

import jankdb.Table;
import jankdb.helpers.*;

public class SelectCommand extends REPLCommand {
    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if (IsValidCommand(2, args, CLICommandRegistry.CommandSizeRules.SELECT, ctx)) {
            String tableName = args[1];
            Table selected = ctx.tableRepo.getOrCreateTable(tableName);
            
            synchronized(selected) {
                if (!selected.tryLock(ctx.userKey)) {
                    ctx.println("Cannot select table. It is currently locked by: " + selected.getLockOwner());
                    return;
                }
                
                if (ctx.table != null && ctx.table.getLockOwner() != null && 
                    ctx.table.getLockOwner().equals(ctx.userKey)) {
                    ctx.table.unlock(ctx.userKey);
                }
                
                ctx.table = selected;
                ctx.println("Selected table: " + tableName);
            }
        }
    }
    
    @Override
    protected boolean requiresWriteLock() {
        return true;
    }
}