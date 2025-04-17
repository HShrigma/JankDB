package jankdb;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Record {

    Map<String, String> data;

    // Constructors ------------------------------------
    public Record() {
        data = new HashMap<String, String>();
    }

    public Record(Map<String, String> baseMap) {
        data = baseMap;
    }

    public Record(String Serialized) {
        data = Deserialize(Serialized);
    }
    // END Constructors ------------------------------------

    // Getters & Setters -----------------------------------
    public Map<String, String> GetData() {
        return data;
    }

    public String GetMapSerialized() {
        StringBuilder builder = new StringBuilder();
        for (String item : data.keySet()) {
            builder.append(item);
            builder.append('=');
            builder.append(data.get(item));
            builder.append(';');
        }
        return builder.toString();
    }

    String SanitizeInputSerialized(String input) {
        input = input.trim();
        String[] pairs = input.split(";");
        StringBuilder sanitized = new StringBuilder();

        Pattern pattern = Pattern.compile("^[\\w-]+=[^=;]*;$");
        for (String pair : pairs) {
            pair = pair.trim() + ";";

            if (pattern.matcher(pair).matches()) {
                sanitized.append(pair);
            }
        }
        System.out.println("sanitized to str: " + sanitized.toString());
        return sanitized.toString();
    }

    Map<String, String> Deserialize(String input) {
        Map<String, String> res = new HashMap<String, String>();
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean buildKey = true;

        input = SanitizeInputSerialized(input);

        for (int i = 0; i < input.length(); i++) {
            if (buildKey) {
                switch (input.charAt(i)) {
                    case '=':
                        buildKey = false;
                        break;
                    default:
                        key.append(input.charAt(i));
                        break;
                }
            } else {
                switch (input.charAt(i)) {
                    case ';':
                        buildKey = true;
                        res.put(key.toString(), value.toString());
                        key.setLength(0);
                        value.setLength(0);
                        break;
                    default:
                        value.append(input.charAt(i));
                        break;
                }
            }
        }
        return res;
    }

    @Override
    public String toString() {
        System.out.println(GetMapSerialized());
        return GetMapSerialized();
    }
    // END Getters & Setters -----------------------------------
}