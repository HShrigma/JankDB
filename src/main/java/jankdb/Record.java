package jankdb;

import java.util.HashMap;
import java.util.Map;

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

    Map<String, String> Deserialize(String input) {
        Map<String, String> res = new HashMap<String, String>();
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean buildKey = true;

        input = input.trim();

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
        return GetMapSerialized();
    }
    // END Getters & Setters -----------------------------------
}