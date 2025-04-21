package jankdb.cli;

import jankdb.Record;
import jankdb.helpers.*;

public class KeysCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if (IsValidCommand(1, args, CLICommandRegistry.CommandSizeRules.KEYS, ctx)) {
            ctx.table.readWithLock(table -> {
                ctx.println(CLICommandRegistry.ExecutionMessages.KEYS_BEGIN);
                boolean found = false;
                
                for (Record record : table.GetRecords()) {
                    for (String key : record.GetData().keySet()) {
                        ctx.println("key: " + key);
                        found = true;
                    }
                }
                
                ctx.println(found ? 
                    CLICommandRegistry.ExecutionMessages.KEYS_SUCCESS :
                    CLICommandRegistry.ExecutionMessages.KEYS_NONE);
                return null;
            });
        }
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.KEYS;
    }
}
