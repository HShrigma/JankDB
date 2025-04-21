package jankdb.cli;

import java.io.IOException;
import java.io.PrintWriter;

import jankdb.Table;
import jankdb.helpers.CLICommandRegistry;

public class ExitCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, Table mainTable) {
        if (IsValidCommandSize(1, args, CLICommandRegistry.CommandSizeRules.EXIT)) {
            // prints goodbye
            System.out.println(CLICommandRegistry.ExecutionMessages.EXIT);
            System.out.println(CLICommandRegistry.Messages.GOODBYE);
        }
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.EXIT;
    }

    @Override
    public void ExecuteClientSide(String[] args, Table mainTable, PrintWriter out) throws IOException {
        if (IsValidCommandSizeClientSide(1, args, CLICommandRegistry.CommandSizeRules.EXIT, out)) {
            // prints goodbye
            out.println(CLICommandRegistry.ExecutionMessages.EXIT);
            out.println(CLICommandRegistry.Messages.GOODBYE);
        }
    }
}