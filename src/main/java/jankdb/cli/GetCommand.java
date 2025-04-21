package jankdb.cli;

import jankdb.Table;
import jankdb.helpers.CLICommandRegistry;
import jankdb.helpers.CommandContext;

public class GetCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, Table mainTable, CommandContext ctx) {
        if (IsValidCommandSize(2, args, CLICommandRegistry.CommandSizeRules.GET)) {
            // Prints value of key if found
            String key = args[1];
            ctx.println(getInitMSG(key));
            // If NOT found
            if (mainTable.FindByKey(key).isEmpty()) {
                ctx.println(getKeyNotFoundMSG(key));
            } else { 
                // If found
                ctx.println(getKeyFoundMSG(key, mainTable));
            }
        }
    }

    String getInitMSG(String key) {
        return CLICommandRegistry.ExecutionMessages.GET_BEGIN_PREFIX + key
                + CLICommandRegistry.ExecutionMessages.GET_BEGIN_SUFFIX;
    }

    String getKeyNotFoundMSG(String key) {
        return CLICommandRegistry.ExecutionMessages.GENERIC_NOT_FOUND_PREFIX + key
                + CLICommandRegistry.ExecutionMessages.GENERIC_NOT_FOUND_SUFFIX;
    }

    String getKeyFoundMSG(String key, Table mainTable){
        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append(CLICommandRegistry.ExecutionMessages.GET_FOUND + key).append('\n');

        mainTable.FindByKey(key).forEach(match -> {
            stringBuilder.append(match.toString()).append('\n');
        });
        return stringBuilder.toString();
    }
    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.GET;
    }
}
