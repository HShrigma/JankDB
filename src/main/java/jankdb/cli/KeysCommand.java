package jankdb.cli;

import java.util.List;
import java.util.Map;

import jankdb.Record;
import jankdb.helpers.*;

public class KeysCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if (IsValidCommand(1, args, CLICommandRegistry.CommandSizeRules.KEYS, ctx)) {
            // Lists all Keys
            ctx.println(CLICommandRegistry.ExecutionMessages.KEYS_BEGIN);
            // Get keys
            List<Record> records = ctx.table.GetRecords();
            boolean found = false;
            // Print each key
            if (!records.isEmpty()) {
                for (Record record : records) {
                    Map<String, String> KvPs = record.GetData();
                    for (String key : KvPs.keySet()) {
                        ctx.println("key: " + key);
                        found = true;
                    }
                }
            }
            if (found) {
                ctx.println(CLICommandRegistry.ExecutionMessages.KEYS_SUCCESS);
                return;
            }
            // If no keys found
            ctx.println(CLICommandRegistry.ExecutionMessages.KEYS_NONE);
        }
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.KEYS;
    }
}
