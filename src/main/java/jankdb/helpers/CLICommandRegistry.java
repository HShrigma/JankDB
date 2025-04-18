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
}