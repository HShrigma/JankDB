package jankdb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.*;

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
        assertEquals("test", f.GetLabel(), "DBFile: Gets Label");
    }

    @Test
    public void testDBFileCanSetLabel() {
        DBFile f = new DBFile("test");
        assertEquals("test", f.GetLabel());
        f.SetLabel("new");
        assertEquals("new", f.GetLabel(), "DBFile: Sets Label");
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
        assertEquals("", f.GetData(), "DBFile: Has Data");
    }

    @Test
    public void testDBFileaCanWriteData() {
        DBFile f = new DBFile("test", "file");
        String dataStr = "Data written to";
        f.WriteData(dataStr);
        assertEquals(dataStr, f.GetData(), "DBFile: Can Write Data");
    }

    @Test
    public void testDBFileCanStoreFile() {
        DBFile f = new DBFile("test", "store/");
        String dataStr = "Data Stored persistently!";
        f.WriteData(dataStr);
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
        DBFile f = new DBFile("test", "store/");
        String dataStr = "Data Stored persistently!";
        f.WriteData(dataStr);
        f.StoreFile();
        String fileData = "";
        // Read the file contents
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f.GetFullFilePath()));
            fileData = reader.readLine(); // Reads first line (adjust if multiline)
            reader.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            assertEquals(dataStr, fileData); // Compare with expected data
        }
    }
}