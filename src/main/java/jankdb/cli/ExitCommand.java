package jankdb.cli;

import jankdb.helpers.*;

public class ExitCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if (IsValidCommand(1, args, CLICommandRegistry.CommandSizeRules.EXIT, ctx)) {
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