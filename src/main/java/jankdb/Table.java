package jankdb;

import java.io.BufferedReader;
import java.io.FileReader;
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
        dbFile.AddData(records.get(records.size() - 1).toString());
    }

    public void DeleteRecord(int index) {
        if (index > -1 && index < records.size()) {
            records.remove(index);
        }
    }

    List<Record> FindByKeyAndValue(String key, String value) {

        List<Record> res = new ArrayList<Record>();
        for (Record record : records) {
            if (record.GetData().keySet().contains(key)) {
                if (record.GetData().get(key).equals(value)) {
                    res.add(record);
                }
            }
        }
        return res;
    }
    public List<Record> FindByKey(String key) {

        List<Record> res = new ArrayList<Record>();
        for (Record record : records) {
            if (record.GetData().keySet().contains(key)) {
                    res.add(record);
            }
        }
        return res;
    }
    public void UpdateRecord(int index, Record newData) {
        if (index > -1 && index < records.size()) {
            records.set(index, newData);
        }
    }

    public int Size() {
        return records.size();
    }
    // END Record methods----------------------------------------

    // DBFile methods ----------------------------------------
    public void Save() {
        dbFile.DeleteData();
        for (Record record : records) {
            dbFile.AddData(record.toString());
        }
        dbFile.EmptyFile();
        dbFile.StoreFile();
    }

    public void Load() {
        try {
            records = new ArrayList<Record>();
            BufferedReader reader = new BufferedReader(new FileReader(dbFile.GetFullFilePath()));
            for (var line : reader.lines().toList()) {
                records.add(new Record(line));
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Table:Load: Error occured when getting data");
        }
    }
    public void Flush(){
        records = new ArrayList<Record>();
    }
    // END DBFile methods ----------------------------------------

}