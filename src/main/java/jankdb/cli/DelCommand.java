package jankdb.cli;

import jankdb.Table;
import jankdb.helpers.CLICommandRegistry;

import java.io.IOException;
import java.io.PrintWriter;

import jankdb.Record;

public class DelCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, Table mainTable) {
        if (IsValidCommandSize(2, args, CLICommandRegistry.CommandSizeRules.DEL)) {
            // Deletes Key if found
            String delKey = args[1];
            System.out.println(CLICommandRegistry.ExecutionMessages.DEL_BEGIN_PREFIX
                    + delKey
                    + CLICommandRegistry.ExecutionMessages.DEL_BEGIN_SUFFIX);

            // search for key
            for (Record record : mainTable.GetRecords()) {
                // safely try deleting if found
                if (record.GetData().containsKey(delKey)) {
                    System.out.println(CLICommandRegistry.ExecutionMessages.DEL_FOUND_PREFIX + delKey
                            + CLICommandRegistry.ExecutionMessages.DEL_FOUND_SUFFIX);
                    try {
                        mainTable.DeleteRecord(mainTable.GetRecords().indexOf(record));
                        System.out.println(CLICommandRegistry.ExecutionMessages.DEL_DELETED_SUCCESS);
                        return;
                    } catch (Exception e) {
                        System.err.println(CLICommandRegistry.ExecutionMessages.DEL_DELETED_FAIL);
                        return;
                    }
                }
            }
            System.out.println(CLICommandRegistry.ExecutionMessages.GENERIC_NOT_FOUND_PREFIX
                    + delKey
                    + CLICommandRegistry.ExecutionMessages.GENERIC_NOT_FOUND_SUFFIX);
        }
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.DEL;
    }

    @Override
    public void ExecuteClientSide(String[] args, Table mainTable, PrintWriter out) throws IOException {
        if (IsValidCommandSizeClientSide(2, args, CLICommandRegistry.CommandSizeRules.DEL, out)) {
            // Deletes Key if found
            String delKey = args[1];
            
            out.println(CLICommandRegistry.ExecutionMessages.DEL_BEGIN_PREFIX
                    + delKey
                    + CLICommandRegistry.ExecutionMessages.DEL_BEGIN_SUFFIX);

            // search for key
            for (Record record : mainTable.GetRecords()) {
                // safely try deleting if found
                if (record.GetData().containsKey(delKey)) {
                    out.println(CLICommandRegistry.ExecutionMessages.DEL_FOUND_PREFIX + delKey
                            + CLICommandRegistry.ExecutionMessages.DEL_FOUND_SUFFIX);
                    try {
                        mainTable.DeleteRecord(mainTable.GetRecords().indexOf(record));
                        out.println(CLICommandRegistry.ExecutionMessages.DEL_DELETED_SUCCESS);
                        return;
                    } catch (Exception e) {
                        out.println("ERROR: " + CLICommandRegistry.ExecutionMessages.DEL_DELETED_FAIL);
                        return;
                    }
                }
            }
            out.println(CLICommandRegistry.ExecutionMessages.GENERIC_NOT_FOUND_PREFIX
                    + delKey
                    + CLICommandRegistry.ExecutionMessages.GENERIC_NOT_FOUND_SUFFIX);
        }
    }
}
