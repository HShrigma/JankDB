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

import org.junit.jupiter.api.Test;

public class JankDBTest {
    @Test
    public void testDBFileExists() {
        DBFile f = new DBFile();
        assertNotEquals(null, f, "DBFile: Exists");
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
    public void testRecordExists() {
        assertNotNull(new Record());
    }

    @Test
    public void testRecordHasDataConstructor() {
        assertNotNull(new Record(new HashMap<String, String>()));
    }

    @Test
    public void testRecordHasSerializedConstructor() {
        String serialized = "name=Bob;age=30;";
        assertNotNull(new Record(serialized));
        assertEquals(serialized, new Record(serialized).toString());
    }
}