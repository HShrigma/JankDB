package jankdb.cli;

import java.util.List;

import jankdb.Record;
import jankdb.helpers.*;

public class SetCommand extends REPLCommand {
    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if (!ctx.table.tryLock(ctx.userKey)) {
            ctx.println("Table is currently locked by another user.");
            return;
        }
        try {
            if (IsValidCommand(3, args, CLICommandRegistry.CommandSizeRules.SET, ctx)) {
                String key = args[1];
                String value = args[2];
                List<Record> found = ctx.table.FindByKey(key);
                if (found.isEmpty()) {
                    AddToTableDebug(key, value, ctx);
                } else {
                    UpdateTableDebug(found, key, value, ctx);
                }
            }
        } finally {
            ctx.table.unlock(ctx.userKey);
        }
    }

    void AddToTableDebug(String key, String value, CommandContext ctx) {
        ctx.println(getAddInitMSG(key));
        try {
            // Add new record as serialized string
            ctx.table.AddRecord(new Record(key + "=" + value + ";"));

            // On successful add
            ctx.println(CLICommandRegistry.ExecutionMessages.SET_ADD_SUCCESS);
        } catch (Exception f) {

            // On failed add print error message
            ctx.println(CLICommandRegistry.ExecutionMessages.SET_ADD_FAIL);
        }
    }

    void UpdateTableDebug(List<Record> found, String key, String value, CommandContext ctx) {
        try {
            for (Record record : found) {
                if (ctx.table.GetRecords().contains(record)) {
                    ctx.println(getAddInitMSG(key));

                    int index = ctx.table.GetRecords().indexOf(record);
                    record.AddKvP(key, value);
                    ctx.table.UpdateRecord(index, record);

                    ctx.println(CLICommandRegistry.ExecutionMessages.SET_UPDATE_SUCCESS);
                }
            }
        } catch (Exception e) {
            ctx.println(CLICommandRegistry.ExecutionMessages.SET_UPDATE_FAIL);
        }
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
    public String Help() {
        return CLICommandRegistry.CommandGuides.SET;
    }
}