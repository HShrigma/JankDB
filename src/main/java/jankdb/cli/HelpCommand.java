package jankdb.cli;

import jankdb.Table;
import jankdb.helpers.*;

public class HelpCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, Table mainTable, CommandContext ctx) {
        if (IsValidCommandSize(1, args, CLICommandRegistry.CommandSizeRules.HELP)) {
            // Prints all command Guidelines
            ctx.println(getHelpMSG());
        }
    }

    String getHelpMSG() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CLICommandRegistry.CommandGuides.GET).append('\n');
        stringBuilder.append(CLICommandRegistry.CommandGuides.SET).append('\n');
        stringBuilder.append(CLICommandRegistry.CommandGuides.DEL).append('\n');
        stringBuilder.append(CLICommandRegistry.CommandGuides.KEYS).append('\n');
        stringBuilder.append(CLICommandRegistry.CommandGuides.SAVE).append('\n');
        stringBuilder.append(CLICommandRegistry.CommandGuides.CLEAR).append('\n');
        stringBuilder.append(CLICommandRegistry.CommandGuides.HELP).append('\n');
        stringBuilder.append(CLICommandRegistry.CommandGuides.EXIT).append('\n');
        return stringBuilder.toString();
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.HELP;
    }
}
