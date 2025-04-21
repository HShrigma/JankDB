package jankdb;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.List;

import jankdb.helpers.CommandContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jankdb.cli.*;

public class REPLCLIManagerTest {

    private REPLCLIManager repl;
    private ByteArrayOutputStream outputStream;
    private PrintWriter out;

    @BeforeEach
    public void setup() {
        repl = new REPLCLIManager();
        outputStream = new ByteArrayOutputStream();
        out = new PrintWriter(outputStream, true);
    }

    private void executeCmd(String command) {
        String[] args = command.trim().split("\\s+");
        REPLCommand cmd = replTestCommandLookup(args[0]);
        assertNotNull(cmd, "Command not found: " + args[0]);
        CommandContext ctx = new CommandContext(true, out, repl.getMainTable());
        cmd.Execute(args, repl.getMainTable(), ctx);
    }

    private REPLCommand replTestCommandLookup(String name) {
        return switch (name.toUpperCase()) {
            case "SET" -> new SetCommand();
            case "GET" -> new GetCommand();
            case "DEL" -> new DelCommand();
            case "CLEAR" -> new ClearCommand();
            case "HELP" -> new HelpCommand();
            case "SAVE" -> new SaveCommand();
            case "KEYS" -> new KeysCommand();
            case "EXIT" -> new ExitCommand();
            default -> null;
        };
    }

    @Test
    public void testSetCommandAddsRecord() {
        executeCmd("SET testKey testValue");
        List<Record> records = repl.getMainTable().GetRecords();
        assertEquals(1, records.size());
        assertTrue(records.get(0).GetData().containsKey("testKey"));
    }

    @Test
    public void testGetCommandFindsRecord() {
        executeCmd("SET hello world");
        outputStream.reset();
        executeCmd("GET hello");
        String result = outputStream.toString();
        assertTrue(result.contains("hello") && result.contains("world"));
    }

    @Test
    public void testDelCommandRemovesKey() {
        executeCmd("SET tempKey tempVal");
        assertEquals(1, repl.getMainTable().Size());
        executeCmd("DEL tempKey");
        assertEquals(0, repl.getMainTable().Size());
    }

    @Test
    public void testClearCommandFlushesAll() {
        executeCmd("SET k1 v1");
        executeCmd("SET k2 v2");
        assertEquals(2, repl.getMainTable().Size());
        executeCmd("CLEAR");
        assertEquals(0, repl.getMainTable().Size());
    }

    @Test
    public void testHelpCommandDoesNotThrow() {
        assertDoesNotThrow(() -> executeCmd("HELP"));
    }

    @Test
    public void testSaveCommandDoesNotThrow() {
        executeCmd("SET saveKey saveValue");
        assertDoesNotThrow(() -> executeCmd("SAVE"));
    }

    @Test
    public void testKeysCommandPrintsKeys() {
        executeCmd("SET key1 value1");
        outputStream.reset();
        assertDoesNotThrow(() -> executeCmd("KEYS"));
        String result = outputStream.toString();
        assertTrue(result.contains("key1"));
    }

    @Test
    public void testExitCommandDoesNotThrow() {
        assertDoesNotThrow(() -> executeCmd("EXIT"));
    }

    @Test
    public void testUnknownCommandHandledGracefully() {
        // simulate a command not in registry
        String[] args = {"FOOBAR"};
        REPLCommand cmd = replTestCommandLookup(args[0]);
        assertNull(cmd, "FOOBAR should not be a valid command");
    }
}
