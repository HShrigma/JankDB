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
        assertTrue(result.contains(CLICommandRegistry.Messages.TABLE_LOCKED_ERR) || 
                 result.contains("Table is currently locked by: user1"),
            "Expected lock message but got: " + result);
    }

    @Test
    public void testSetCommandExecutesAfterLockRelease() {
        Table mainTable = tableRepo.getOrCreateTable("main");
        
        // Lock with user1
        assertTrue(mainTable.tryLock("user1"));
        
        // User1 performs SET
        executeCmd("SET lockedKey lockedValue", "user1");
        assertEquals(1, mainTable.Size());
        
        // User2 tries to SET (should fail)
        outputStream.reset();
        executeCmd("SET shouldFail value", "user2");
        String lockedResult = outputStream.toString();
        assertTrue(lockedResult.contains(CLICommandRegistry.Messages.TABLE_LOCKED_ERR) || 
                  lockedResult.contains("Table is currently locked by: user1"),
            "Expected lock message but got: " + lockedResult);
        
        // User1 releases lock
        mainTable.unlock("user1");
        assertNull(mainTable.getLockOwner());
        
        // User2 tries again after lock release
        outputStream.reset();
        executeCmd("SET testKey testValue", "user2");
        String result = outputStream.toString();
        assertTrue(result.contains(CLICommandRegistry.ExecutionMessages.SET_ADD_SUCCESS),
            "Expected: '" + CLICommandRegistry.ExecutionMessages.SET_ADD_SUCCESS + 
            "' but got: '" + result + "'");
        assertEquals(2, mainTable.Size());
    }

    @AfterEach
    public void cleanup() {
        tableRepo.clearAll();
    }
}