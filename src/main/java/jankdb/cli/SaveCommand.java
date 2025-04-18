package jankdb.cli;

import jankdb.Table;
import jankdb.helpers.CLICommandRegistry;

public class SaveCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, Table mainTable) {
        if (IsValidCommandSize(1, args, CLICommandRegistry.CommandSizeRules.SAVE)) {
            // Saves DB to persistent storage
            System.out.println(CLICommandRegistry.ExecutionMessages.SAVE_BEGIN);
            try {
                mainTable.Save();
                System.out.println(CLICommandRegistry.ExecutionMessages.SAVE_SUCCESS);
            } catch (Exception e) {
                System.out.println(CLICommandRegistry.ExecutionMessages.SAVE_FAIL);
            }
        }
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.SAVE;
    }
}
