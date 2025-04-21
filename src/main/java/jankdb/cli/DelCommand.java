package jankdb.cli;

import jankdb.helpers.*;

import jankdb.Record;

public class DelCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if (!ctx.table.tryLock(ctx.userKey)) {
            ctx.println("Table is currently locked by another user.");
            return;
        }
        try {
            if (IsValidCommand(2, args, CLICommandRegistry.CommandSizeRules.DEL, ctx)) {
                String key = args[1];
                ctx.println(getInitMSG(key));
    
                for (Record record : ctx.table.GetRecords()) {
                    if (record.GetData().containsKey(key)) {
                        ctx.println(getKeyFoundMSG(key));
                        try {
                            ctx.table.DeleteRecord(ctx.table.GetRecords().indexOf(record));
                            ctx.println(CLICommandRegistry.ExecutionMessages.DEL_DELETED_SUCCESS);
                            return;
                        } catch (Exception e) {
                            ctx.println(CLICommandRegistry.ExecutionMessages.DEL_DELETED_FAIL);
                            return;
                        }
                    }
                }
                ctx.println(getKeyNotFoundMSG(key));
            }
        } finally {
            ctx.table.unlock(ctx.userKey);
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
