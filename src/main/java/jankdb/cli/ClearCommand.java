package jankdb.cli;

import jankdb.helpers.CLICommandRegistry;
import jankdb.helpers.CommandContext;

public class ClearCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if (IsValidCommand(1, args, CLICommandRegistry.CommandSizeRules.CLEAR, ctx)) {
            // Clears All Table Items
            ctx.println(CLICommandRegistry.ExecutionMessages.CLEAR_BEGIN);
            try {
                ctx.table.Flush();
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
