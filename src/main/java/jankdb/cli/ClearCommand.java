package jankdb.cli;

import jankdb.Table;
import jankdb.helpers.CLICommandRegistry;
import jankdb.helpers.CommandContext;

public class ClearCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, Table mainTable, CommandContext ctx) {
        if (IsValidCommandSize(1, args, CLICommandRegistry.CommandSizeRules.CLEAR)) {
            // Clears All Table Items
            ctx.println(CLICommandRegistry.ExecutionMessages.CLEAR_BEGIN);
            try {
                mainTable.Flush();
                ctx.println(CLICommandRegistry.ExecutionMessages.CLEAR_SUCCESS);

            } catch (Exception e) {
                ctx.println("ERROR: " + CLICommandRegistry.ExecutionMessages.CLEAR_FAIL);
            }
        }
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.CLEAR;
    }
}
