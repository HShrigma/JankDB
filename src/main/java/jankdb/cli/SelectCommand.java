package jankdb.cli;

import jankdb.Table;
import jankdb.helpers.*;

public class SelectCommand extends REPLCommand {
    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if (IsValidCommand(2, args, CLICommandRegistry.CommandSizeRules.SELECT, ctx)) {
            String tableName = args[1];
    
            // Get table from registry
            Table selected = ctx.tables.computeIfAbsent(tableName, Table::new);
            ctx.table = selected;
    
            // Register it in REPL tables map if not already there
            ctx.tables.putIfAbsent(tableName, selected);
    
            ctx.println("Selected table: " + tableName);
        }
    }
    

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.SELECT;
    }
}