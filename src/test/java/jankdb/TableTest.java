package jankdb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TableTest {

    
    @Test
    public void testTableConstructs() {
        assertNotNull(new Table("testTable"), "Table: Can be created");
    }

    @Test
    public void testTableCanGetEmptyRecords() {
        assertTrue(new Table("testTable").GetRecords().isEmpty(), "Table: Can Get Empty Record");
    }

    @Test
    public void testTableCanAddRecord() {
        Table t = new Table("testTable");
        t.AddRecord(new Record("name=Bob;age=2;"));
        assertEquals(t.GetRecords().get(0).toString(), "name=Bob;age=2;");
    }

    @Test
    public void testTableCanSave() {
        Table t = new Table("testTable");
        t.AddRecord(new Record("name=Bob;age=32;"));
        t.AddRecord(new Record("name=Alice;age=40;"));
        t.Save();
        boolean stored = false;
        try {
            FileReader i = new FileReader("src/main/resources/store/testTable.txt");
            i.close();
            stored = true;
        } catch (Exception e) {
            System.err.println("An error occured");
            e.printStackTrace();
        } finally {
            assertTrue(stored);
        }
    }

    @Test
    public void testTableCanSaveAccurately() {
        Table t = new Table("testTable");
        t.AddRecord(new Record("name=Bob;age=32;"));
        t.AddRecord(new Record("name=Alice;age=40;"));
        t.Save();
        StringBuilder fileData = new StringBuilder();
        StringBuilder expectedData = new StringBuilder();
        for (var record : t.GetRecords()) {
            expectedData.append(record.toString()).append('\n');
        }
        // Read the file contents
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/store/testTable.txt"));
            for (String item : reader.lines().toList()) {
                fileData.append(item).append('\n');
            }
            reader.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            assertEquals(expectedData.toString(), fileData.toString()); // Compare with expected data
        }
    }

    @Test
    public void testTableCanLoadAccurately() {
        Table t = new Table("testTable");
        t.AddRecord(new Record("name=Bob;age=32;"));
        t.AddRecord(new Record("name=Alice;age=40;"));
        t.Save();
        Table t2 = new Table("testTable");
        t2.Load();
        assertEquals(t.GetRecords().toString(), t2.GetRecords().toString());
    }

    @Test
    public void testTableCanRemoveRecords() {
        Table t = new Table("testTable");
        t.AddRecord(new Record("name=Bob;age=32;"));
        t.AddRecord(new Record("name=Alice;age=40;"));
        t.AddRecord(new Record("name=Remove;age=29"));
        t.DeleteRecord(2);

        List<Record> expected = new ArrayList<Record>();
        expected.add(new Record("name=Bob;age=32;"));
        expected.add(new Record("name=Alice;age=40;"));

        assertEquals(expected.toString(), t.GetRecords().toString());
    }

    @Test
    public void testTableCanGetSize() {
        Table t = new Table("testTable");
        assertEquals(0, t.Size());
        t.AddRecord(new Record("name=Bob;age=32;"));
        assertEquals(1, t.Size());
        t.AddRecord(new Record("name=Alice;age=40;"));
        assertEquals(2, t.Size());
        t.AddRecord(new Record("name=Remove;age=29"));
        assertEquals(3, t.Size());
    }

    @Test
    public void testTableCanFindByKey() {
        Table t = new Table("testFind");
        t.AddRecord(new Record("name=Bob;age=32;"));
        t.AddRecord(new Record("name=Alice;age=40;"));
        t.AddRecord(new Record("name=Bob;age=25;"));

        List<Record> result = t.FindByKey("name", "Bob");

        assertEquals(2, result.size(), "FindByKey: Should find 2 records with name=Bob");
        for (Record r : result) {
            assertEquals("Bob", r.GetData().get("name"));
        }
    }

    @Test
    public void testTableFindByKeyWithNoMatchReturnsEmpty() {
        Table t = new Table("testFindEmpty");
        t.AddRecord(new Record("name=Bob;age=32;"));
        List<Record> result = t.FindByKey("name", "Charlie");

        assertTrue(result.isEmpty(), "FindByKey: Should return empty list when no matches");
    }

    @Test
    public void testTableFindByNonexistentKeyReturnsEmpty() {
        Table t = new Table("testFindKeyMissing");
        t.AddRecord(new Record("name=Bob;age=32;"));
        List<Record> result = t.FindByKey("height", "180");

        assertTrue(result.isEmpty(), "FindByKey: Should return empty list when key doesn't exist");
    }

    @Test
    public void testTableCanUpdateRecord() {
        Table t = new Table("testUpdate");
        t.AddRecord(new Record("name=Bob;age=32;"));
        t.AddRecord(new Record("name=Alice;age=40;"));

        Record updated = new Record("name=Charlie;age=28;");
        t.UpdateRecord(1, updated);

        assertEquals("Charlie", t.GetRecords().get(1).GetData().get("name"));
        assertEquals("28", t.GetRecords().get(1).GetData().get("age"));
    }

    @Test
    public void testTableUpdateOutOfBoundsDoesNothing() {
        Table t = new Table("testUpdateOutOfBounds");
        t.AddRecord(new Record("name=Bob;age=32;"));

        Record updated = new Record("name=ShouldNotExist;age=0;");
        t.UpdateRecord(10, updated); // Out of bounds

        assertEquals("Bob", t.GetRecords().get(0).GetData().get("name"));
        assertEquals(1, t.Size());
    }
}