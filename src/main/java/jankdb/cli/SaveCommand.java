package jankdb.cli;

import jankdb.helpers.*;

public class SaveCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if (IsValidCommand(1, args, CLICommandRegistry.CommandSizeRules.SAVE, ctx)) {
            // Saves DB to persistent storage
            ctx.println(CLICommandRegistry.ExecutionMessages.SAVE_BEGIN);
            try {
                ctx.table.Save();
                ctx.println(CLICommandRegistry.ExecutionMessages.SAVE_SUCCESS);
            } catch (Exception e) {
                ctx.println(CLICommandRegistry.ExecutionMessages.SAVE_FAIL);
            }
        }
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.SAVE;
    }
}
