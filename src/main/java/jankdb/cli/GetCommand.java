package jankdb.cli;

import java.util.List;

import jankdb.helpers.CLICommandRegistry;
import jankdb.helpers.CommandContext;

public class GetCommand extends REPLCommand {
    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if (IsValidCommand(2, args, CLICommandRegistry.CommandSizeRules.GET, ctx)) {
            String key = args[1];
            ctx.println(getInitMSG(key));

            ctx.table.readWithLock(table -> {
                List<jankdb.Record> found = table.FindByKey(key);
                if (found.isEmpty()) {
                    ctx.println(getKeyNotFoundMSG(key));
                } else {
                    ctx.println(getKeyFoundMSG(key, found));
                }
                return null;
            });
        }
    }

    String getKeyFoundMSG(String key, List<jankdb.Record> foundRecords) {
        StringBuilder sb = new StringBuilder();
        sb.append(CLICommandRegistry.ExecutionMessages.GET_FOUND + key).append('\n');
        foundRecords.forEach(r -> sb.append(r.toString()).append('\n'));
        return sb.toString();
    }

    String getInitMSG(String key) {
        return CLICommandRegistry.ExecutionMessages.GET_BEGIN_PREFIX + key
                + CLICommandRegistry.ExecutionMessages.GET_BEGIN_SUFFIX;
    }

    String getKeyNotFoundMSG(String key) {
        return CLICommandRegistry.ExecutionMessages.GENERIC_NOT_FOUND_PREFIX + key
                + CLICommandRegistry.ExecutionMessages.GENERIC_NOT_FOUND_SUFFIX;
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.GET;
    }

}
