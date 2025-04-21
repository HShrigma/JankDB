package jankdb;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.List;

import jankdb.helpers.CommandContext;

import org.junit.jupiter.api.AfterEach;
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
        REPLCommand cmd = repl.getCommands().get(args[0].toUpperCase());
        assertNotNull(cmd, "Command not found: " + args[0]);
        CommandContext ctx = new CommandContext(true, out, repl.getMainTable(), repl.getCommands(), repl.getTables());
        cmd.Execute(args, ctx);
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
        String[] args = { "FOOBAR" };
        REPLCommand cmd = repl.getCommands().get(args[0].toUpperCase());
        assertNull(cmd, "FOOBAR should not be a valid command");
    }

    @Test
    public void testSelectCreatesAndUsesNewTable() {
        executeCmd("SELECT myTable");

        Table myTable = repl.getTables().get("myTable");
        assertNotNull(myTable, "New table 'myTable' should have been created");

        CommandContext ctx = new CommandContext(true, out, myTable, repl.getCommands(), repl.getTables());
        REPLCommand setCmd = repl.getCommands().get("SET");
        setCmd.Execute(new String[] { "SET", "foo", "bar" }, ctx);

        assertEquals(1, myTable.Size());
        assertTrue(myTable.GetRecords().get(0).GetData().containsKey("foo"));
    }

    @Test
    public void testTablesCommandListsTables() {
        executeCmd("SELECT newT");
        repl.getTables().get("newT").Save();

        outputStream.reset();
        REPLCommand tablesCmd = repl.getCommands().get("TABLES");
        CommandContext tablesCtx = new CommandContext(true, out, repl.getMainTable(), repl.getCommands(), repl.getTables());
        tablesCmd.Execute(new String[] { "TABLES" }, tablesCtx);
        String result = outputStream.toString();

        assertTrue(result.contains("main"), "Should contain default 'main' table");
        assertTrue(result.contains("newT"), "Should contain newly created 'newT' table");
    }

    // New Tests for Locking Mechanism

    @Test
    public void testSetCommandBlockedByLock() {
        executeCmd("SET lockedKey lockedValue");  // Session 1
        outputStream.reset();
        executeCmd("SET testKey testValue"); // Session 2

        String result = outputStream.toString();
        assertTrue(result.contains("Table is currently locked by another user"), "SET command should be blocked by lock.");
    }

    @Test
    public void testSetCommandExecutesAfterLockRelease() {
        executeCmd("SET lockedKey lockedValue");  // Session 1
        outputStream.reset();
        executeCmd("SET testKey testValue"); // Session 2
        
        String result = outputStream.toString();
        assertTrue(result.contains("SET"), "SET command should execute after lock release.");
        List<Record> records = repl.getMainTable().GetRecords();
        assertEquals(2, records.size(), "There should be two records in the table.");
    }

    @AfterEach
    public void cleanup() {
        File f = new File("src/main/resources/store/newT.txt");
        if (f.exists())
            f.delete();
    }

}
