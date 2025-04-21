package jankdb.cli;

import java.io.IOException;
import java.io.PrintWriter;

import jankdb.Table;
import jankdb.helpers.CLICommandRegistry;

public class HelpCommand extends REPLCommand {

    @Override
    public void Execute(String[] args, Table mainTable) {
        if (IsValidCommandSize(1, args, CLICommandRegistry.CommandSizeRules.HELP)) {
            // Prints all command Guidelines
            System.out.println(buildResponseStream());
        }
    }

    @Override
    public void ExecuteClientSide(String[] args, Table mainTable, PrintWriter out) throws IOException {
        if (IsValidCommandSizeClientSide(1, args, CLICommandRegistry.CommandSizeRules.HELP, out)) {
            out.println(buildResponseStream());
        }
    }

    String buildResponseStream() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CLICommandRegistry.CommandGuides.GET).append('\n');
        stringBuilder.append(CLICommandRegistry.CommandGuides.SET).append('\n');
        stringBuilder.append(CLICommandRegistry.CommandGuides.DEL).append('\n');
        stringBuilder.append(CLICommandRegistry.CommandGuides.KEYS).append('\n');
        stringBuilder.append(CLICommandRegistry.CommandGuides.SAVE).append('\n');
        stringBuilder.append(CLICommandRegistry.CommandGuides.CLEAR).append('\n');
        stringBuilder.append(CLICommandRegistry.CommandGuides.HELP).append('\n');
        stringBuilder.append(CLICommandRegistry.CommandGuides.EXIT).append('\n');
        return stringBuilder.toString();
    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.HELP;
    }
}
