package jankdb.cli;

import jankdb.Table;
import jankdb.helpers.*;

public class ExitCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, Table mainTable, CommandContext ctx) {
        if (IsValidCommandSize(1, args, CLICommandRegistry.CommandSizeRules.EXIT)) {
            // prints goodbye
            ctx.println(CLICommandRegistry.ExecutionMessages.EXIT);
            ctx.println(CLICommandRegistry.Messages.GOODBYE);
        }
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.EXIT;
    }
}