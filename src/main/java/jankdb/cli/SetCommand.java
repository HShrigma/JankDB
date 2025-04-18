package jankdb.cli;

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
                AddToTable(key, value, mainTable);
            } else {
                UpdateTable(found, key, value, mainTable);
            }
        }
    }

    void AddToTable(String key, String value, Table mainTable) {
        System.out.println(CLICommandRegistry.ExecutionMessages.SET_ADD_PREFIX + key
                + CLICommandRegistry.ExecutionMessages.SET_ADD_SUFFIX);

        try {
            Record r = new Record(key + "=" + value + ";");
            mainTable.AddRecord(r);
            System.out.println(CLICommandRegistry.ExecutionMessages.SET_ADD_SUCCESS);
        } catch (Exception f) {

            System.out.println(CLICommandRegistry.ExecutionMessages.SET_ADD_FAIL);
        }
    }

    void UpdateTable(List<Record> found, String key, String value, Table mainTable) {
        try {
            for (Record record : found) {
                if (mainTable.GetRecords().contains(record)) {
                    System.out.println(CLICommandRegistry.ExecutionMessages.SET_UPDATE_PREFIX + key
                            + CLICommandRegistry.ExecutionMessages.SET_UPDATE_SUFFIX);
                    int index = mainTable.GetRecords().indexOf(record);
                    record.AddKvP(key, value);
                    mainTable.UpdateRecord(index, record);
                    System.out.println(CLICommandRegistry.ExecutionMessages.SET_UPDATE_SUCCESS);
                }
            }
        } catch (Exception e) {
            System.out.println(CLICommandRegistry.ExecutionMessages.SET_UPDATE_FAIL);
        }
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.GET;
    }
}