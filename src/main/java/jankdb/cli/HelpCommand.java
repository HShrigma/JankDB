package jankdb.cli;

import jankdb.Table;
import jankdb.helpers.CLICommandRegistry;

public class HelpCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, Table mainTable) {
        if (IsValidCommandSize(1, args, CLICommandRegistry.CommandSizeRules.HELP)) {
            // prints All command Guidelines
            System.out.println(CLICommandRegistry.ExecutionMessages.HELP);
            System.out.println(CLICommandRegistry.CommandGuides.GET);
            System.out.println(CLICommandRegistry.CommandGuides.SET);
            System.out.println(CLICommandRegistry.CommandGuides.DEL);
            System.out.println(CLICommandRegistry.CommandGuides.KEYS);
            System.out.println(CLICommandRegistry.CommandGuides.SAVE);
            System.out.println(CLICommandRegistry.CommandGuides.CLEAR);
            System.out.println(CLICommandRegistry.CommandGuides.HELP);
            System.out.println(CLICommandRegistry.CommandGuides.EXIT);
        }
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.HELP;
    }
}
