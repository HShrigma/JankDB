package jankdb.cli;

import jankdb.Table;
import jankdb.helpers.*;

public class SaveCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, Table mainTable, CommandContext ctx) {
        if (IsValidCommandSize(1, args, CLICommandRegistry.CommandSizeRules.SAVE)) {
            // Saves DB to persistent storage
            ctx.println(CLICommandRegistry.ExecutionMessages.SAVE_BEGIN);
            try {
                mainTable.Save();
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
