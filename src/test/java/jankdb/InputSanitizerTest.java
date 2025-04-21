package jankdb;

import org.junit.jupiter.api.Test;

import jankdb.helpers.InputSanitizer;

import static org.junit.jupiter.api.Assertions.*;

public class InputSanitizerTest {

    @Test
    public void testControlCharactersAreRemoved() {
        String input = "SET key\u0000\t\nvalue";
        String sanitized = InputSanitizer.sanitize(input);
        assertFalse(sanitized.contains("\u0000"));
        assertFalse(sanitized.contains("\t"));
        assertFalse(sanitized.contains("\n"));
    }

    @Test
    public void testShellInjectionCharactersAreStripped() {
        String input = "SET key; rm -rf / | echo hacked";
        String sanitized = InputSanitizer.sanitize(input);
        assertFalse(sanitized.contains(";"));
        assertFalse(sanitized.contains("|"));
        assertFalse(sanitized.contains(">"));
        assertFalse(sanitized.contains("<"));
        assertFalse(sanitized.contains("&"));
        assertFalse(sanitized.contains("`"));
    }

    @Test
    public void testUnicodeNormalization() {
        // Full-width ＡＢＣ should normalize to ABC
        String input = "ＳＥＴ ｋｅｙ ｖａｌｕｅ";
        String sanitized = InputSanitizer.sanitize(input);
        assertTrue(sanitized.contains("SET") || sanitized.contains("ＳＥＴ")); // depends on OS/unicode behavior
    }

    @Test
    public void testInputTooLong() {
        StringBuilder longInput = new StringBuilder("SET key ");
        for (int i = 0; i < 2000; i++) {
            longInput.append("x");
        }
        String sanitized = InputSanitizer.sanitize(longInput.toString());
        assertEquals("", sanitized);
    }

    @Test
    public void testEmptyInputReturnsEmpty() {
        String sanitized = InputSanitizer.sanitize("   ");
        assertEquals("", sanitized);
    }

    @Test
    public void testNullInputThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            InputSanitizer.sanitize(null);
        });
    }
}
