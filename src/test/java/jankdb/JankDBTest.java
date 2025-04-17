package jankdb;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JankDBTest {
    @Test
    public void testDBFileExists() {
        DBFile f = new DBFile();
        assertNotEquals(null, f);
    }

    @Test
    public void testDBFileHasLabelConstructor() {
        DBFile f = new DBFile("");
        assertNotEquals(null, f);
    }

    @Test
    public void testDBFileHasLabelIsValid() {
        DBFile f = new DBFile("test");
        assertEquals("test", f.GetLabel());
    }

    @Test
    public void testDBFileCanSetLabel(){
        DBFile f = new DBFile("test");
        assertEquals("test", f.GetLabel());
        f.SetLabel("new");
        assertEquals("new", f.GetLabel());
    }
    @Test
    public void testDBFileHasFilePathConstructor() {
        DBFile f = new DBFile("", "");
        assertNotEquals(null, f);
    }

    @Test
    public void testDBFileHasFilePathIsValid() {
        DBFile f = new DBFile("test", "file");
        assertEquals("file", f.GetFilePath());
    }

    @Test
    public void testDBFileCanSetFilePath(){
        DBFile f = new DBFile("test", "file");
        assertEquals("file", f.GetFilePath());
        f.SetFilePath("new");
        assertEquals("new", f.GetFilePath());
    }
}