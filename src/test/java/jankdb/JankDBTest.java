package jankdb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class JankDBTest {
    @Test
    public void testDBFileExists() {
        DBFile f = new DBFile();
        assertNotNull(f, "DBFile: Exists");
    }

    @Test
    public void testDBFileHasLabelConstructor() {
        DBFile f = new DBFile("");
        assertNotEquals(null, f, "DBFile: Has Label Constructor");
    }

    @Test
    public void testDBFileHasLabelIsValid() {
        DBFile f = new DBFile("test");
        assertEquals("test.txt", f.GetLabel(), "DBFile: Gets Label");
    }

    @Test
    public void testDBFileCanSetLabel() {
        DBFile f = new DBFile("test");
        assertEquals("test.txt", f.GetLabel());
        f.SetLabel("new");
        assertEquals("new.txt", f.GetLabel(), "DBFile: Sets Label");
    }

    @Test
    public void testDBFileHasFilePathConstructor() {
        DBFile f = new DBFile("", "");
        assertNotEquals(null, f, "DBFile: Has File Path Constructor");
    }

    @Test
    public void testDBFileHasFilePathIsValid() {
        DBFile f = new DBFile("test", "./");
        assertEquals("./", f.GetFilePath(), "DBFile: Gets File Path");
    }

    @Test
    public void testDBFileCanSetFilePath() {
        DBFile f = new DBFile("test", "./");
        assertEquals("./", f.GetFilePath());
        f.SetFilePath("/");
        assertEquals("/", f.GetFilePath(), "DBFile: Sets File Path");
    }

    @Test
    public void testDBFileHasFullFilePathIsValid() {
        DBFile f = new DBFile("test", "new/");
        assertEquals("src/main/resources/new/test.txt", f.GetFullFilePath(), "DBFile: Gets Full File path");
    }

    @Test
    public void testDBFileaHasData() {
        DBFile f = new DBFile("test", "file");
        boolean hasData = false;
        try {
            hasData = f.GetData().isEmpty();
        } finally {
            assertTrue(hasData, "DBFile: Data is not null");
        }
    }

    @Test
    public void testDBFileaCanAddData() {
        DBFile f = new DBFile("test", "file");
        String dataStr = "Data written to";
        f.AddData(dataStr);
        assertEquals(dataStr, f.GetData().get(0), "DBFile: Can Add Data");
    }

    @Test
    public void testDBFileCanStoreFile() {
        DBFile f = new DBFile("test", "store/");
        String dataStr = "Data Stored persistently!";
        f.AddData(dataStr);
        f.StoreFile();
        boolean fileFound = false;
        try {
            FileReader fReader = new FileReader(f.GetFullFilePath());
            fileFound = true;
            fReader.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            fileFound = false;
        } finally {
            assertTrue(fileFound, "DBFile: Has successfuly Stored File at " + f.GetFullFilePath());
        }
    }

    @Test
    public void testDBFileStoresAccurately() {
        DBFile f = new DBFile("test1", "store/");
        List<String> dataStrs = new ArrayList<String>();
        dataStrs.add("Data 1!");
        dataStrs.add("Data 2!");

        f.AddData(dataStrs.get(0));
        f.AddData(dataStrs.get(1));

        f.StoreFile();
        String fileData = "";
        String expectedData = dataStrs.get(0) + dataStrs.get(1);
        // Read the file contents
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f.GetFullFilePath()));
            for (String item : reader.lines().toList()) {
                fileData += item;
            }
            reader.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            assertEquals(expectedData, fileData); // Compare with expected data
        }
    }

    @Test
    public void testDBFileCanDeleteData() {
        DBFile f = new DBFile("test2", "store/");
        List<String> dataStrs = new ArrayList<String>();
        dataStrs.add("Data 1!");
        dataStrs.add("Data 2!");

        f.AddData(dataStrs.get(0));
        f.AddData(dataStrs.get(1));

        f.StoreFile();
        f.EmptyFile();
        String fileData = "";
        // Read the file contents
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f.GetFullFilePath()));
            for (String item : reader.lines().toList()) {
                fileData += item;
            }
            reader.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            assertEquals("", fileData); // Compare with expected data
        }
    }

    @Test
    public void testRecordCanBeCreatedEmpty() {
        Record r = new Record();
        assertTrue(r.GetData().isEmpty(), "Record should be empty on default constructor");
    }

    @Test
    public void testRecordCanBeCreatedFromMap() {
        Map<String, String> input = new HashMap<>();
        input.put("name", "Alice");
        input.put("age", "30");
        Record r = new Record(input);
        assertEquals("Alice", r.GetData().get("name"));
        assertEquals("30", r.GetData().get("age"));
    }

    @Test
    public void testRecordSerializesToStringCorrectly() {
        Map<String, String> input = new HashMap<>();
        input.put("key", "value");
        Record r = new Record(input);
        String s = r.toString();
        assertEquals("key=value;", s);
    }

    @Test
    public void testRecordDeserializesFromString() {
        String input = "name=Bob;age=25;";
        Record r = new Record(input);
        assertEquals("Bob", r.GetData().get("name"));
        assertEquals("25", r.GetData().get("age"));
    }

    @Test
    public void testRecordSerializationIsReversible() {
        Map<String, String> input = new HashMap<>();
        input.put("x", "100");
        input.put("y", "200");
        Record r1 = new Record(input);
        String serialized = r1.toString();
        Record r2 = new Record(serialized);
        assertEquals(r1.GetData(), r2.GetData(), "Deserialized record should match original");
    }

    @Test
    public void testRecordHandlesTrailingSemicolonGracefully() {
        Record r = new Record("foo=bar;baz=qux;");
        assertEquals("bar", r.GetData().get("foo"));
        assertEquals("qux", r.GetData().get("baz"));
    }

    @Test
    public void testRecordIgnoresEmptyFields() {
        Record r = new Record("key1=value1;asdsadsa;123123;; ;key2=value2;");
        assertEquals("value1", r.GetData().get("key1"));
        assertEquals("value2", r.GetData().get("key2"));
    }

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