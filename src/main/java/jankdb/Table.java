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
    

}