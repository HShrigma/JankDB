package jankdb;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class REPLCLIManagerTest {

    REPLCLIManager manager;
    Table table;

    @BeforeEach
    void setUp() {
        manager = new REPLCLIManager();
        manager.mainTable.Flush(); // Clear in-memory
        table = manager.mainTable;
    }

    @Test
    void testAddToTable() {
        manager.AddToTable("foo", "bar");
        List<Record> results = table.FindByKey("foo");
        assertEquals(1, results.size());
        assertEquals("bar", results.get(0).GetData().get("foo"));
    }

    @Test
    void testUpdateTable() {
        manager.AddToTable("x", "1");
        List<Record> matches = table.FindByKey("x");
        manager.UpdateTable(matches, "x", "42");

        Record updated = table.FindByKey("x").get(0);
        assertEquals("42", updated.GetData().get("x"));
    }

    @Test
    void testDeleteCommandDeletesKey() {
        manager.AddToTable("delete_me", "bye");
        String[] cmd = {"DEL", "delete_me"};
        manager.ProcessDeleteCommand(cmd);

        List<Record> matches = table.FindByKey("delete_me");
        assertTrue(matches.isEmpty() || !matches.get(0).GetData().containsKey("delete_me"));
    }

    @Test
    void testGetCommandFindsKey() {
        manager.AddToTable("luna", "cat");

        // Shouldn't crash
        manager.ProcessGetCommand(new String[]{"GET", "luna"});
    }

    @Test
    void testClearCommandWipesEverything() {
        manager.AddToTable("one", "1");
        manager.AddToTable("two", "2");

        assertFalse(table.GetRecords().isEmpty());
        manager.ProcessClearCommand(new String[]{"CLEAR"});
        assertTrue(table.GetRecords().isEmpty());
    }

    @Test
    void testSaveAndLoadCycle() {
        manager.AddToTable("persist", "me");
        manager.ProcessSaveCommand(new String[]{"SAVE"});

        manager.mainTable.Flush(); // simulate exit
        manager.mainTable.Load();

        assertEquals(1, table.FindByKey("persist").size());
    }

    @Test
    void testKeysCommandListsKeys() {
        manager.AddToTable("a", "alpha");
        manager.AddToTable("b", "beta");

        // Shouldn't throw anything
        manager.ProcessKeysCommand(new String[]{"KEYS"});
    }

    @Test
    void testHelpAndExitCommands() {
        manager.ProcessHelpCommand(new String[]{"HELP"});
        manager.ProcessExitCommand(new String[]{"EXIT"});
    }

    @Test
    void testInvalidCommandSize() {
        assertFalse(manager.IsValidCommandSize(2, new String[]{"SET"}, "SET usage..."));
        assertTrue(manager.IsValidCommandSize(2, new String[]{"GET", "key"}, "GET usage..."));
    }

    @Test
    void testInitDBCreatesTableIfMissing() {
        // Should not throw
        manager.InitDB();
    }
}
