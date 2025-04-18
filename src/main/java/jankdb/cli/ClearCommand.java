package jankdb.cli;

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
}
