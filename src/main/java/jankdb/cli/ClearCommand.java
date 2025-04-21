package jankdb.cli;

import jankdb.helpers.CLICommandRegistry;
import jankdb.helpers.CommandContext;

public class ClearCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, CommandContext ctx) {
        try {
            if (IsValidCommand(1, args, CLICommandRegistry.CommandSizeRules.CLEAR, ctx)) {
                ctx.table.writeWithLock(ctx.userKey, table -> {
                    ctx.println(CLICommandRegistry.ExecutionMessages.CLEAR_BEGIN);
                    table.Flush();
                    ctx.println(CLICommandRegistry.ExecutionMessages.CLEAR_SUCCESS);
                    return null;
                });
            }
        } catch (Exception e) {
            ctx.println("ERROR: " + CLICommandRegistry.ExecutionMessages.CLEAR_FAIL);
        }
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.CLEAR;
    }

    @Override
    protected boolean requiresWriteLock() {
        return true;
    }
}
