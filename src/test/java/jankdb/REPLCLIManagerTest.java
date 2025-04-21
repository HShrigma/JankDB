package jankdb;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import org.junit.jupiter.api.*;
import jankdb.cli.*;
import jankdb.helpers.*;

public class REPLCLIManagerTest {
    private REPLCLIManager repl;
    private ByteArrayOutputStream outputStream;
    private PrintWriter out;
    private TableRepository tableRepo;

    @BeforeEach
    public void setup() {
        tableRepo = new TableRepository();
        repl = new REPLCLIManager(tableRepo);
        outputStream = new ByteArrayOutputStream();
        out = new PrintWriter(outputStream, true);
    }

    private void executeCmd(String command, String userKey) {
        String[] args = command.trim().split("\\s+");
        REPLCommand cmd = repl.getCommands().get(args[0].toUpperCase());
        assertNotNull(cmd, "Command not found: " + args[0]);
        CommandContext ctx = new CommandContext(true, out,
                tableRepo.getOrCreateTable("main"), repl.getCommands(), tableRepo, userKey);
        cmd.Execute(args, ctx);
    }

    @Test
    public void testSetCommandBlockedByLock() {
        Table mainTable = tableRepo.getOrCreateTable("main");
        assertTrue(mainTable.tryLock("user1"));

        outputStream.reset();
        executeCmd("SET testKey testValue", "user2");

        String result = outputStream.toString();
        assertTrue(result.contains("Table locked by: user1"), // Match exact output
                "Expected lock message but got: " + result);
    }

    @Test
    public void testSetCommandExecutesAfterLockRelease() {
        System.out.println("\n=== Starting testSetCommandExecutesAfterLockRelease ===");
        
        Table mainTable = tableRepo.getOrCreateTable("main");
        
        // User1 performs SET (acquires lock during command)
        System.out.println("[TEST] User1 SET command");
        executeCmd("SET lockedKey lockedValue", "user1");
        assertEquals(1, mainTable.Size());
        
        // Verify lock is released after command
        assertNull(mainTable.getLockOwner());
        
        // User2 performs SET (should succeed since lock is released)
        System.out.println("[TEST] User2 SET command");
        outputStream.reset();
        executeCmd("SET testKey testValue", "user2");
        
        String result = outputStream.toString();
        System.out.println("[TEST] Output: " + result);
        assertTrue(result.contains(CLICommandRegistry.ExecutionMessages.SET_ADD_SUCCESS),
            "Expected SET success message but got: " + result);
        assertEquals(2, mainTable.Size());
    }
    @AfterEach
    public void cleanup() {
        tableRepo.clearAll();
    }
}