package jankdb.helpers;

public final class CLICommandRegistry {

    private CLICommandRegistry(){
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
        public static final String EXIT = "EXIT";
    }

    public static final class Messages {
        public static final String GREETING = "Welcome to JankDB!";
        public static final String GOODBYE = "Thank you for using JankDB!\nThe program will now exit";
        public static final String REQ_COMMAND = "Please enter a command:\n(use command \"HELP\" to get a list of commands)";
        public static final String INVALID_SIZE_ERR = "Invalid size of command!";
    }

    public static final class CommandSizeRules{
        public static final String SET = "SET must have 2 arguments formatted as: SET <KEY> <VALUE>";
        public static final String GET = "GET must have 1 argument formatted as: GET <KEY>";
        public static final String DEL = "DEL must have 1 argument formatted as: DEL <KEY>";
        public static final String KEYS = "KEYS has no arguments!";
        public static final String SAVE = "SAVE has no arguments!";
        public static final String CLEAR = "CLEAR has no arguments!";
        public static final String HELP = "HELP has no arguments!";
        public static final String EXIT = "EXIT has no arguments!";
    }
    public static final class CommandGuides{
        public static final String SET = "SET updates the <Key> value to <Value> (if found).";
        public static final String GET = "GET returns the <Key> value (if found).";
        public static final String DEL = "DEL deletes the associated <Key>:Value pair (if found).";
        public static final String KEYS = "KEYS returns all keys in the database.";
        public static final String SAVE = "SAVE saves all current operations to persistent storage.";
        public static final String CLEAR = "CLEAR removes all entries in the database.";
        public static final String HELP = "HELP lists all available actions.";
        public static final String EXIT = "EXIT terminates the current session.";
    }
}