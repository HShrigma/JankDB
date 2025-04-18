package jankdb.cli;

import java.util.List;
import java.util.Map;

import jankdb.Record;
import jankdb.Table;
import jankdb.helpers.CLICommandRegistry;

public class KeysCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, Table mainTable) {
        if (IsValidCommandSize(1, args, CLICommandRegistry.CommandSizeRules.KEYS)) {
            // Lists all Keys
            System.out.println(CLICommandRegistry.ExecutionMessages.KEYS_BEGIN);
            // Get keys
            List<Record> records = mainTable.GetRecords();
            boolean found = false;
            // print each key
            if (!records.isEmpty()) {
                for (Record record : records) {
                    Map<String, String> KvPs = record.GetData();
                    for (String key : KvPs.keySet()) {
                        System.out.println("key: " + key);
                        found = true;
                    }
                }
            }
            if (found) {
                System.out.println(CLICommandRegistry.ExecutionMessages.KEYS_SUCCESS);
                return;
            }

            System.out.println(CLICommandRegistry.ExecutionMessages.KEYS_NONE);
        }
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.KEYS;
    }
}
