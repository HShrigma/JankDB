package jankdb.cli;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jankdb.Table;
import jankdb.Record;
import jankdb.helpers.CLICommandRegistry;

public class SetCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, Table mainTable) {
        if (IsValidCommandSize(3, args, CLICommandRegistry.CommandSizeRules.SET)) {
            // Sets Key to Value if found
            String key = args[1];
            String value = args[2];
            List<Record> found = mainTable.FindByKey(key);
            if (found.isEmpty()) {
                System.out.println(AddToTableDebug(key, value, mainTable));
            } else {
                System.out.println(UpdateTableDebug(found, key, value, mainTable));
            }
        }
    }

    String AddToTableDebug(String key, String value, Table mainTable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CLICommandRegistry.ExecutionMessages.SET_ADD_PREFIX + key
                + CLICommandRegistry.ExecutionMessages.SET_ADD_SUFFIX).append('\n');

        try {
            Record r = new Record(key + "=" + value + ";");
            mainTable.AddRecord(r);
            stringBuilder.append(CLICommandRegistry.ExecutionMessages.SET_ADD_SUCCESS).append('\n');
        } catch (Exception f) {

            stringBuilder.append(CLICommandRegistry.ExecutionMessages.SET_ADD_FAIL).append('\n');
        }
        return stringBuilder.toString();
    }

    String UpdateTableDebug(List<Record> found, String key, String value, Table mainTable) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (Record record : found) {
                if (mainTable.GetRecords().contains(record)) {
                    stringBuilder.append(CLICommandRegistry.ExecutionMessages.SET_UPDATE_PREFIX + key
                            + CLICommandRegistry.ExecutionMessages.SET_UPDATE_SUFFIX).append('\n');
                    int index = mainTable.GetRecords().indexOf(record);
                    record.AddKvP(key, value);
                    mainTable.UpdateRecord(index, record);
                    stringBuilder.append(CLICommandRegistry.ExecutionMessages.SET_UPDATE_SUCCESS).append('\n');
                }
            }
        } catch (Exception e) {
            stringBuilder.append(CLICommandRegistry.ExecutionMessages.SET_UPDATE_FAIL).append('\n');
        }
        return stringBuilder.toString();
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.SET;
    }

    @Override
    public void ExecuteClientSide(String[] args, Table mainTable, PrintWriter out) throws IOException {
        if (IsValidCommandSizeClientSide(3, args, CLICommandRegistry.CommandSizeRules.SET, out)) {
            // Sets Key to Value if found
            String key = args[1];
            String value = args[2];
            List<Record> found = mainTable.FindByKey(key);
            if (found.isEmpty()) {
                out.println(AddToTableDebug(key, value, mainTable));
            } else {
                out.println(UpdateTableDebug(found, key, value, mainTable));
            }
        }
    }
}