package jankdb.helpers;

public final class CLICommandRegistry {

    private CLICommandRegistry() {
        throw new AssertionError("CLI Command registry is a helper class not to be instantiated");
    }

    public static final class BaseCommands {
        public static final String SET = "SET";
        public static final String GET = "GET";
        public static final String DEL = "DEL";
        public static final String KEYS = "KEYS";
        public static final String SAVE = "SAVE";
        public static final String CLEAR = "CLEAR";
        public static final String HELP = "HELP";
        public static final String SELECT = "SELECT";
        public static final String TABLES = "TABLES";
        public static final String EXIT = "EXIT";
    }

    public static final class Messages {
        public static final String GREETING = "Welcome to JankDB!";
        public static final String GOODBYE = "Thank you for using JankDB!\nThe program will now exit";
        public static final String REQ_COMMAND = "Please enter a command:\n(use command \"HELP\" to get a list of commands)";
        public static final String INVALID_SIZE_ERR = "Invalid size of command!";
        public static final String TABLE_NOT_FOUND = "Table not created yet.\nCreating table main...";
        public static final String TABLE_FOUND = "Table successfully loaded. Session has started";
        public static final String UNRECOGNIZED_COMMAND_ERR = "Command not recognized! Use \"HELP\" to see all valid commands.";
        public static final String TABLE_NULL_ERR = "Error: No table is sellected!\n Use \"TABLES\" to see available tables \nand \"SELECT\" to choose/create a table.";
        public static final String TABLE_LOCKED_ERR = "[lock] Table is locked by another session.";
    }

    public static final class CommandSizeRules {
        public static final String SET = "SET must have 2 arguments formatted as: SET <KEY> <VALUE>";
        public static final String GET = "GET must have 1 argument formatted as: GET <KEY>";
        public static final String DEL = "DEL must have 1 argument formatted as: DEL <KEY>";
        public static final String KEYS = "KEYS has no arguments!";
        public static final String SAVE = "SAVE has no arguments!";
        public static final String CLEAR = "CLEAR has no arguments!";
        public static final String HELP = "HELP has no arguments!";
        public static final String EXIT = "EXIT has no arguments!";
        public static final String SELECT = "SELECT must have 1 argument formatted as: SELECT <tableName>";
        public static final String TABLES = "TABLES has on arguments!";
    }

    public static final class CommandGuides {
        public static final String SET = "SET updates the <Key> value to <Value> (if found).\nIf not found, adds the <Key>:<Value> Pair to the database";
        public static final String GET = "GET returns the <Key> value (if found).";
        public static final String DEL = "DEL deletes the associated <Key>:Value pair (if found).";
        public static final String KEYS = "KEYS returns all keys in the database.";
        public static final String SAVE = "SAVE saves all current operations to persistent storage.";
        public static final String CLEAR = "CLEAR removes all entries in the database.";
        public static final String HELP = "HELP lists all available actions.";
        public static final String EXIT = "EXIT terminates the current session.";
        public static final String SELECT = "SELECT <tableName> - Select or create a table";
        public static final String TABLES = "TABLES - List all tables in the database";

    }

    public static final class ExecutionMessages {
        public static final String SET_BEGIN = "Updating DB...";
        public static final String SET_ADD_PREFIX = "Could not find <Key> And <Value>: ";
        public static final String SET_ADD_SUFFIX = ". Adding to DB!";
        public static final String SET_ADD_SUCCESS = "Successfully added to DB!";
        public static final String SET_ADD_FAIL = "An error occured while attempting to add key. Please try again.";
        public static final String SET_UPDATE_PREFIX = "Found <Key>: ";
        public static final String SET_UPDATE_SUFFIX = ". Value is now: ";
        public static final String SET_UPDATE_SUCCESS = "Successfully updated DB!";
        public static final String SET_UPDATE_FAIL = "An error occured while updating DB. Please try again.";
        public static final String GET_BEGIN_PREFIX = "Attempting to find <Key>: ";
        public static final String GET_BEGIN_SUFFIX = "...";
        public static final String GET_FOUND = "Found <Key>: ";
        public static final String DEL_BEGIN_PREFIX = "Searching for Key ";
        public static final String DEL_BEGIN_SUFFIX = " to delete...";
        public static final String DEL_FOUND_PREFIX = "Found Key: ";
        public static final String DEL_FOUND_SUFFIX = "!\nDeleting...";
        public static final String DEL_DELETED_SUCCESS = "Deletion was successful";
        public static final String DEL_DELETED_FAIL = "An error occured while attempting to delete key. Please try again.";
        public static final String GENERIC_NOT_FOUND_PREFIX = "Could not find Key: ";
        public static final String GENERIC_NOT_FOUND_SUFFIX = "!\nAre you sure this key exists? Try using the KEYS command to verify.";
        public static final String KEYS_BEGIN = "Retrieving all keys...";
        public static final String KEYS_SUCCESS = "Successfully retrieved all keys!";
        public static final String KEYS_NONE = "No keys found!";
        public static final String SAVE_BEGIN = "Saving all changes to persistent storage...";
        public static final String SAVE_SUCCESS = "All changes successfully saved!";
        public static final String SAVE_FAIL = "An error occured while saving your data. Please try again.";
        public static final String CLEAR_BEGIN = "Clearing all Database entries...";
        public static final String CLEAR_SUCCESS = "All Database entries cleared successfully!\nRemember to SAVE all changes!";
        public static final String CLEAR_FAIL = "An error occured while saving your data. Please try again!";
        public static final String HELP = "Listing all commands...";
        public static final String EXIT = "Session terminated.";
    }
}