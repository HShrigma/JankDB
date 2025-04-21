package jankdb.cli;

import java.io.IOException;
import java.io.PrintWriter;
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

    @Override
    public void ExecuteClientSide(String[] args, Table mainTable, PrintWriter out) throws IOException {
        if (IsValidCommandSizeClientSide(1, args, CLICommandRegistry.CommandSizeRules.KEYS, out)) {
            // Lists all Keys
            out.println(CLICommandRegistry.ExecutionMessages.KEYS_BEGIN);
            // Get keys
            List<Record> records = mainTable.GetRecords();
            boolean found = false;
            // print each key
            if (!records.isEmpty()) {
                for (Record record : records) {
                    Map<String, String> KvPs = record.GetData();
                    for (String key : KvPs.keySet()) {
                        out.println("key: " + key);
                        found = true;
                    }
                }
            }
            if (found) {
                out.println(CLICommandRegistry.ExecutionMessages.KEYS_SUCCESS);
                return;
            }
            out.println(CLICommandRegistry.ExecutionMessages.KEYS_NONE);
        }
    }
}
