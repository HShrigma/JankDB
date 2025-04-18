package jankdb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class RecordTest {

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
    void testDeserialization() {
        Record r = new Record("hello=world;foo=bar;");
        Map<String, String> data = r.GetData();

        assertEquals("world", data.get("hello"));
        assertEquals("bar", data.get("foo"));
    }

    @Test
    void testSerialization() {
        Record r = new Record();
        r.AddKvP("a", "1");
        r.AddKvP("b", "2");

        String output = r.toString();
        assertTrue(output.contains("a=1;"));
        assertTrue(output.contains("b=2;"));
    }

    @Test
    void testDeleteKey() {
        Record r = new Record("x=y;");
        r.DeleteByKey("x");
        assertTrue(r.GetData().isEmpty());
    }

    @Test
    void testInvalidSanitizeInputDoesNotBreak() {
        Record r = new Record("invalid=string;still=works;;weird==stuff;");
        assertNotNull(r.GetData());
        assertTrue(r.GetData().containsKey("invalid"));
        assertTrue(r.GetData().containsKey("still"));
        assertFalse(r.GetData().containsKey("weird"));
    }
}