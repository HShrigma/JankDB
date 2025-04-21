package jankdb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Table {
    private final String label;
    private List<Record> records;
    private final DBFile dbFile;
    private final String path = "store/";
    private final ReentrantLock tableLock = new ReentrantLock(true); // Fair lock
    private String lockOwner = null;

    // Constructors ----------------------------------------
    public Table(String label) {
        this.label = label;
        records = new ArrayList<Record>();
        dbFile = new DBFile(this.label, path);
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

    public void Flush() {
        records = new ArrayList<Record>();
    }
    // END DBFile methods ----------------------------------------

    // Table Locking ----------------------------------------------
    // In Table.java
    public synchronized boolean tryLock(String userKey) {
        System.out.println("[LOCK] Attempting lock by: " + userKey);
        System.out.println("[LOCK] Current owner: " + lockOwner);
        
        if (lockOwner == null || lockOwner.equals(userKey)) {
            lockOwner = userKey;
            System.out.println("[LOCK] Lock acquired by: " + userKey);
            return true;
        }
        System.out.println("[LOCK] Lock denied to: " + userKey);
        return false;
    }
    



    public boolean tryLock(String userKey, long timeout, TimeUnit unit)
            throws InterruptedException {
        if (tableLock.tryLock(timeout, unit)) {
            try {
                if (lockOwner == null || lockOwner.equals(userKey)) {
                    lockOwner = userKey;
                    return true;
                }
            } finally {
                if (lockOwner == null) {
                    tableLock.unlock();
                }
            }
        }
        return false;
    }


    public synchronized void unlock(String userKey) {
        System.out.println("[UNLOCK] Attempt by: " + userKey);
        System.out.println("[UNLOCK] Current owner: " + lockOwner);

        if (lockOwner != null && lockOwner.equals(userKey)) {
            lockOwner = null;
            System.out.println("[UNLOCK] Successfully unlocked by: " + userKey);
        }
    }

    public synchronized boolean isLockedByOther(String userKey) {
        System.out.println("[LOCK CHECK] Current owner: " + lockOwner);
        System.out.println("[LOCK CHECK] Requested by: " + userKey);
        return lockOwner != null && !lockOwner.equals(userKey);
    }

    public String getLockOwner() {
        tableLock.lock();
        try {
            return lockOwner;
        } finally {
            tableLock.unlock();
        }
    }

    // Helper method for read operations
    public <T> T readWithLock(java.util.function.Function<Table, T> reader) {
        tableLock.lock();
        try {
            return reader.apply(this);
        } finally {
            tableLock.unlock();
        }
    }

    // Helper method for write operations
    public <T> T writeWithLock(String userKey, java.util.function.Function<Table, T> writer) {
        if (!tryLock(userKey)) {
            throw new IllegalStateException("Could not acquire write lock");
        }
        try {
            return writer.apply(this);
        } finally {
            unlock(userKey);
        }
    }
    // END Table Locking ----------------------------------------------
}