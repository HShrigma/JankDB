package jankdb.cli;

import jankdb.helpers.*;

import jankdb.Record;

public class DelCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, CommandContext ctx) {
        try {
            if (IsValidCommand(2, args, CLICommandRegistry.CommandSizeRules.DEL, ctx)) {
                String key = args[1];
                ctx.println(getInitMSG(key));
                
                boolean deleted = false;
                for (Record record : ctx.table.GetRecords()) {
                    if (record.GetData().containsKey(key)) {
                        ctx.println(getKeyFoundMSG(key));
                        ctx.table.DeleteRecord(ctx.table.GetRecords().indexOf(record));
                        ctx.println(CLICommandRegistry.ExecutionMessages.DEL_DELETED_SUCCESS);
                        deleted = true;
                        break;
                    }
                }
                
                if (!deleted) {
                    ctx.println(getKeyNotFoundMSG(key));
                }
            }
        } catch (Exception e) {
            ctx.println(CLICommandRegistry.ExecutionMessages.DEL_DELETED_FAIL);
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
    @Override
    protected boolean requiresWriteLock(){
        return true;
    }
}
