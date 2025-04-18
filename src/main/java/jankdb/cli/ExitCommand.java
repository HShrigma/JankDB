package jankdb.cli;

import jankdb.Table;
import jankdb.helpers.CLICommandRegistry;

public class ExitCommand extends REPLCommand{

    @Override
    public void Execute(String[] args, Table mainTable) {
        if (IsValidCommandSize(1, args, CLICommandRegistry.CommandSizeRules.EXIT)) {
            // prints goodbye
            System.out.println(CLICommandRegistry.ExecutionMessages.EXIT);
            System.out.println(CLICommandRegistry.Messages.GOODBYE);
        }
    }
    @Override
    public String Help(){
        return CLICommandRegistry.CommandGuides.EXIT;
    }
}