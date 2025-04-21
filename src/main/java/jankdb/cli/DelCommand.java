package jankdb.cli;

import jankdb.helpers.*;

import java.util.List;

import jankdb.Record;

public class DelCommand extends REPLCommand {

 @Override
    public void Execute(String[] args, CommandContext ctx) {
        try {
            if (!IsValidCommand(2, args, CLICommandRegistry.CommandSizeRules.DEL, ctx)) {
                return;
            }
            
            String key = args[1];
            ctx.println(getInitMSG(key));
            
            ctx.table.writeWithLock(ctx.userKey, table -> {
                boolean deleted = false;
                List<Record> records = table.GetRecords();
                
                for (int i = 0; i < records.size(); i++) {
                    if (records.get(i).GetData().containsKey(key)) {
                        ctx.println(getKeyFoundMSG(key));
                        table.DeleteRecord(i);
                        ctx.println(CLICommandRegistry.ExecutionMessages.DEL_DELETED_SUCCESS);
                        deleted = true;
                        break;
                    }
                }
                
                if (!deleted) {
                    ctx.println(getKeyNotFoundMSG(key));
                }
                return null;
            });
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
