package jankdb;

import static org.junit.jupiter.api.Assertions.*;

// import jankdb.cli.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class REPLCLIManagerTest {

    REPLCLIManager repl;

    @BeforeEach
    public void setup() {
        repl = new REPLCLIManager();
    }

    @Test
    public void testSetCommandAddsRecord() {
        repl.ParseCommand("SET testKey testValue");
        assertEquals(1, repl.mainTable.Size());
        assertTrue(repl.mainTable.GetRecords().get(0).GetData().containsKey("testKey"));
    }

    @Test
    public void testGetCommandFindsRecord() {
        repl.ParseCommand("SET hello world");
        // Output goes to stdout, we test by checking existence in table
        repl.ParseCommand("GET hello");
        assertEquals("world", repl.mainTable.GetRecords().get(0).GetData().get("hello"));
    }

    @Test
    public void testDelCommandRemovesKey() {
        repl.ParseCommand("SET tempKey tempVal");
        assertEquals(1, repl.mainTable.Size());

        repl.ParseCommand("DEL tempKey");
        assertFalse(repl.mainTable.Size() == 1);
    }

    @Test
    public void testClearCommandFlushesAll() {
        repl.ParseCommand("SET k1 v1");
        repl.ParseCommand("SET k2 v2");
        assertEquals(2, repl.mainTable.Size());

        repl.ParseCommand("CLEAR");
        assertEquals(0, repl.mainTable.Size());
    }

    @Test
    public void testHelpCommandDoesNotThrow() {
        assertDoesNotThrow(() -> repl.ParseCommand("HELP"));
    }

    @Test
    public void testSaveCommandDoesNotThrow() {
        repl.ParseCommand("SET saveKey saveValue");
        assertDoesNotThrow(() -> repl.ParseCommand("SAVE"));
    }

    @Test
    public void testKeysCommandDoesNotThrow() {
        repl.ParseCommand("SET key1 value1");
        assertDoesNotThrow(() -> repl.ParseCommand("KEYS"));
    }

    @Test
    public void testExitCommandDoesNotThrow() {
        assertDoesNotThrow(() -> repl.ParseCommand("EXIT"));
    }

    @Test
    public void testUnknownCommandHandledGracefully() {
        assertDoesNotThrow(() -> repl.ParseCommand("FOOBAR"));
    }
}
