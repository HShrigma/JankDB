package jankdb.cli;

import jankdb.helpers.*;

public class SaveCommand extends REPLCommand {
    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if (!ctx.table.tryLock(ctx.userKey)) {
            ctx.println("Table is currently locked by another user.");
            return;
        }
        try {
            if (IsValidCommand(1, args, CLICommandRegistry.CommandSizeRules.SAVE, ctx)) {
                ctx.println(CLICommandRegistry.ExecutionMessages.SAVE_BEGIN);
                try {
                    ctx.table.Save();
                    ctx.println(CLICommandRegistry.ExecutionMessages.SAVE_SUCCESS);
                } catch (Exception e) {
                    ctx.println(CLICommandRegistry.ExecutionMessages.SAVE_FAIL);
                }
            }
        } finally {
            ctx.table.unlock(ctx.userKey);
        }
    }
    

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.SAVE;
    }
}
