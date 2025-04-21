package jankdb.cli;

import jankdb.Table;
import jankdb.helpers.*;

public class SelectCommand extends REPLCommand {
    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if (IsValidCommand(2, args, CLICommandRegistry.CommandSizeRules.SELECT, ctx)) {
            String tableName = args[1];
            Table selected = ctx.tables.computeIfAbsent(tableName, Table::new);
    
            // Optionally try to lock the new table
            if (!selected.tryLock(ctx.userKey)) {
                ctx.println("Cannot select table. It is currently locked by another user.");
                return;
            }
    
            ctx.table = selected;
            ctx.println("Selected table: " + tableName);
        }
    }
    
    

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.SELECT;
    }
}