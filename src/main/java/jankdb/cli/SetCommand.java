package jankdb.cli;

import java.util.List;

import jankdb.Table;
import jankdb.Record;
import jankdb.helpers.*;

public class SetCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, Table mainTable, CommandContext ctx) {
        if (IsValidCommandSize(3, args, CLICommandRegistry.CommandSizeRules.SET)) {
            // Sets Key to Value if found
            String key = args[1];
            String value = args[2];
            List<Record> found = mainTable.FindByKey(key);
            if (found.isEmpty()) {
                // Add new value to table if key not found
                AddToTableDebug(key, value, mainTable, ctx);
            } else {
                // Update existing table entry if key found
                UpdateTableDebug(found, key, value, mainTable, ctx);
            }
        }
    }

    void AddToTableDebug(String key, String value, Table mainTable, CommandContext ctx) {
        ctx.println(getAddInitMSG(key));
        try {
            // Add new record as serialized string
            mainTable.AddRecord(new Record(key + "=" + value + ";"));

            // On successful add
            ctx.println(CLICommandRegistry.ExecutionMessages.SET_ADD_SUCCESS);
        } catch (Exception f) {

            // On failed add print error message
            ctx.println(CLICommandRegistry.ExecutionMessages.SET_ADD_FAIL);
        }
    }

    void UpdateTableDebug(List<Record> found, String key, String value, Table mainTable, CommandContext ctx) {
        try {
            for (Record record : found) {
                if (mainTable.GetRecords().contains(record)) {
                    ctx.println(getAddInitMSG(key));

                    int index = mainTable.GetRecords().indexOf(record);
                    record.AddKvP(key, value);                    
                    mainTable.UpdateRecord(index, record);
                    
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
    String getUpdateInitMSG(String key){
        return CLICommandRegistry.ExecutionMessages.SET_UPDATE_PREFIX + key
        + CLICommandRegistry.ExecutionMessages.SET_UPDATE_SUFFIX;
    }
    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.SET;
    }
}