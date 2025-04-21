package jankdb.cli;

import jankdb.helpers.CLICommandRegistry;
import jankdb.helpers.CommandContext;

import java.io.File;

public class TablesCommand extends REPLCommand {
    @Override
    public void Execute(String[] args, CommandContext ctx) {
        if(IsValidCommand(1, args,CLICommandRegistry.CommandSizeRules.TABLES, ctx)){
            File folder = new File("src/main/resources/store");
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
            if (files == null || files.length == 0) {
                ctx.println("No tables found.");
                return;
            }
            
            ctx.println("Available tables:");
            for (File f : files) {
                ctx.println("- " + f.getName().replace(".txt", ""));
            }
        }

    }

    @Override
    public String Help() {
        return CLICommandRegistry.CommandGuides.TABLES;
    }
}