package jankdb.cli;

import jankdb.Table;
import jankdb.helpers.CLICommandRegistry;

public class GetCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, Table mainTable) {
        if (IsValidCommandSize(2, args, CLICommandRegistry.CommandSizeRules.GET)) {
            // Prints value of key if found
            String key = args[1];
            System.out.println(CLICommandRegistry.ExecutionMessages.GET_BEGIN_PREFIX + key
                    + CLICommandRegistry.ExecutionMessages.GET_BEGIN_SUFFIX);
            // if not found 
            if (mainTable.FindByKey(key).isEmpty()) {
                System.out.println(CLICommandRegistry.ExecutionMessages.GENERIC_NOT_FOUND_PREFIX + key
                        + CLICommandRegistry.ExecutionMessages.GENERIC_NOT_FOUND_SUFFIX);
            } else { // if found
                System.out.println(CLICommandRegistry.ExecutionMessages.GET_FOUND + key);
                mainTable.FindByKey(key).forEach(match -> {
                    System.out.println(match.toString());
                });
            }
        }
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.GET;
    }
}
