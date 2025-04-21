package jankdb.cli;

import jankdb.helpers.*;

import jankdb.Record;

public class DelCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if (IsValidCommand(2, args, CLICommandRegistry.CommandSizeRules.DEL, ctx)) {
            // Deletes Key if found
            String key = args[1];
            ctx.println(getInitMSG(key));

            // Search for key
            for (Record record : ctx.table.GetRecords()) {
                // Safely try deleting if found
                if (record.GetData().containsKey(key)) {

                    System.out.println(getKeyFoundMSG(key));
                    try {
                        ctx.table.DeleteRecord(ctx.table.GetRecords().indexOf(record));
                        // On successful deletion
                        System.out.println(CLICommandRegistry.ExecutionMessages.DEL_DELETED_SUCCESS);
                        return;
                    } catch (Exception e) {
                        // On unsuccessful deletion
                        System.err.println(CLICommandRegistry.ExecutionMessages.DEL_DELETED_FAIL);
                        return;
                    }
                }
            }
            // If key isn't found
            System.out.println(getKeyNotFoundMSG(key));
        }
    }

    String getInitMSG(String key) {
        return CLICommandRegistry.ExecutionMessages.DEL_BEGIN_PREFIX
                + key
                + CLICommandRegistry.ExecutionMessages.DEL_BEGIN_SUFFIX;
    }

    String getKeyFoundMSG(String key) {
        return CLICommandRegistry.ExecutionMessages.DEL_FOUND_PREFIX + key
                + CLICommandRegistry.ExecutionMessages.DEL_FOUND_SUFFIX;
    }

    String getKeyNotFoundMSG(String key) {
        return CLICommandRegistry.ExecutionMessages.GENERIC_NOT_FOUND_PREFIX
                + key
                + CLICommandRegistry.ExecutionMessages.GENERIC_NOT_FOUND_SUFFIX;
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.DEL;
    }
}
