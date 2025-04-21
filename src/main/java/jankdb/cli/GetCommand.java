package jankdb.cli;

import jankdb.Table;
import jankdb.helpers.CLICommandRegistry;
import jankdb.helpers.CommandContext;

public class GetCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if (IsValidCommand(2, args, CLICommandRegistry.CommandSizeRules.GET, ctx)) {
            // Prints value of key if found
            String key = args[1];
            ctx.println(getInitMSG(key));
            // If NOT found
            if (ctx.table.FindByKey(key).isEmpty()) {
                ctx.println(getKeyNotFoundMSG(key));
            } else { 
                // If found
                ctx.println(getKeyFoundMSG(key, ctx.table));
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

    String getKeyFoundMSG(String key, Table table){
        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append(CLICommandRegistry.ExecutionMessages.GET_FOUND + key).append('\n');

        table.FindByKey(key).forEach(match -> {
            stringBuilder.append(match.toString()).append('\n');
        });
        return stringBuilder.toString();
    }
    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.GET;
    }
}
