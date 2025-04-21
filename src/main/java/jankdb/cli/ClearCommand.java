package jankdb.cli;

import java.io.IOException;
import java.io.PrintWriter;

import jankdb.Table;
import jankdb.helpers.CLICommandRegistry;

public class ClearCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, Table mainTable) {
        if (IsValidCommandSize(1, args, CLICommandRegistry.CommandSizeRules.CLEAR)) {
            // Clears All Table Items
            System.out.println(CLICommandRegistry.ExecutionMessages.CLEAR_BEGIN);
            try {
                mainTable.Flush();
                System.out.println(CLICommandRegistry.ExecutionMessages.CLEAR_SUCCESS);

            } catch (Exception e) {
                System.out.println(CLICommandRegistry.ExecutionMessages.CLEAR_FAIL);
            }
        }
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.CLEAR;
    }

    @Override
    public void ExecuteClientSide(String[] args, Table mainTable, PrintWriter out) throws IOException {
        if (IsValidCommandSizeClientSide(1, args, CLICommandRegistry.CommandSizeRules.CLEAR, out)) {
            // Clears All Table Items
            out.println(CLICommandRegistry.ExecutionMessages.CLEAR_BEGIN);
            try {
                mainTable.Flush();
                out.println(CLICommandRegistry.ExecutionMessages.CLEAR_SUCCESS);

            } catch (Exception e) {
                out.println("ERROR: " + CLICommandRegistry.ExecutionMessages.CLEAR_FAIL);
            }
        }
    }
}
