package jankdb.cli;

import jankdb.helpers.*;

public class SaveCommand extends REPLCommand {
    @Override
    public void Execute(String[] args, CommandContext ctx) {
        try {
            if (IsValidCommand(1, args, CLICommandRegistry.CommandSizeRules.SAVE, ctx)) {
                ctx.println(CLICommandRegistry.ExecutionMessages.SAVE_BEGIN);
                ctx.table.Save();
                ctx.println(CLICommandRegistry.ExecutionMessages.SAVE_SUCCESS);
            }
        } catch (Exception e) {
            ctx.println(CLICommandRegistry.ExecutionMessages.SAVE_FAIL);
        }
    }
    

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.SAVE;
    }
    @Override
    protected boolean requiresWriteLock(){
        return true;
    }
}
