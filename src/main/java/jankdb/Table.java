package jankdb;

import java.util.ArrayList;
import java.util.List;

public class Table {
    String label;
    List<Record> records;
    DBFile dbFile;
    final String path = "store/";

    // Constructors ----------------------------------------
    public Table(String label) {
        this.label = label;
        records = new ArrayList<Record>();
        dbFile = new DBFile(label, path);
    }
    // END Constructors ----------------------------------------

    // Getters & Setters ----------------------------------------
    public List<Record> GetRecords() {
        return records;
    }
    // END Getters & Setters ----------------------------------------

    // Records methods----------------------------------------
    public void AddRecord(Record r) {
        records.add(r);
    }
    // END Record methods----------------------------------------

}