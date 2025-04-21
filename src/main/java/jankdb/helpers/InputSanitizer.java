package jankdb.helpers;

import java.text.Normalizer;

public class InputSanitizer {

    public static String sanitize(String input) {
        // Normalize Unicode
        input = Normalizer.normalize(input, Normalizer.Form.NFC);

        // Remove null bytes and control chars
        input = input.replaceAll("\\p{Cntrl}", "");

        // Strip dangerous symbols
        input = input.replaceAll("[;|&><`]", "");

        // Trim and length-check
        input = input.trim();
        if (input.length() > 1024) {
            System.err.println("Input too long");
            return "";
        }

        return input;
    }
}
