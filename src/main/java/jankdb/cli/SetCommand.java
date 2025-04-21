package jankdb.cli;

import java.util.List;
import jankdb.Record;
import jankdb.helpers.*;

public class SetCommand extends REPLCommand {
    @Override
    public void Execute(String[] args, CommandContext ctx) {
        try {
            if (!IsValidCommand(3, args, CLICommandRegistry.CommandSizeRules.SET, ctx)) {
                return;
            }
    
            String key = args[1];
            String value = args[2];
            List<Record> found = ctx.table.FindByKey(key);
    
            try {
                if (found.isEmpty()) {
                    addToTable(key, value, ctx);
                } else {
                   updateTable(found, key, value, ctx);
                }
            } finally {
                // DON'T unlock here - let the next command handle it
                // Lock is maintained until another command needs it
            }
        } catch (Exception e) {
            ctx.println("ERROR: Failed to execute SET command");
        }
    }

    private void addToTable(String key, String value, CommandContext ctx) {
        ctx.println(getAddInitMSG(key));
        ctx.table.AddRecord(new Record(key + "=" + value + ";"));
        ctx.println(CLICommandRegistry.ExecutionMessages.SET_ADD_SUCCESS);
    }

    private void updateTable(List<Record> found, String key, String value, CommandContext ctx) {
        ctx.println(getUpdateInitMSG(key));
        for (Record record : found) {
            int index = ctx.table.GetRecords().indexOf(record);
            record.AddKvP(key, value);
            ctx.table.UpdateRecord(index, record);
        }
        ctx.println(CLICommandRegistry.ExecutionMessages.SET_UPDATE_SUCCESS);
    }

    String getAddInitMSG(String key) {
        return CLICommandRegistry.ExecutionMessages.SET_ADD_PREFIX + key
                + CLICommandRegistry.ExecutionMessages.SET_ADD_SUFFIX;
    }

    String getUpdateInitMSG(String key) {
        return CLICommandRegistry.ExecutionMessages.SET_UPDATE_PREFIX + key
                + CLICommandRegistry.ExecutionMessages.SET_UPDATE_SUFFIX;
    }

    @Override
    protected boolean requiresWriteLock() {
        return true;
    }
}