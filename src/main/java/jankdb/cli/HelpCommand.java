package jankdb.cli;

import jankdb.helpers.*;

public class HelpCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if (IsValidCommand(1, args, CLICommandRegistry.CommandSizeRules.HELP, ctx)) {
            // Prints all command Guidelines
            ctx.println(getHelpMSG(ctx));
        }
    }

    String getHelpMSG(CommandContext ctx) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key: ctx.commands.keySet()) {
            stringBuilder.append(ctx.commands.get(key).Help()).append('\n');
        }
        return stringBuilder.toString();
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.HELP;
    }
}
