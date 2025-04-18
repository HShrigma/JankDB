package jankdb;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DBFile {
    String label;
    String filePath;
    String extension = ".txt";
    final String DEFAULT_PATH_PREFIX = "src/main/resources/";

    List<String> data;

    // Constructors ----------------------------------------
    public DBFile() {
        label = "";
        filePath = "";
        data = new ArrayList<String>();
    }

    public DBFile(String label) {
        this.label = label;
        filePath = "";
        data = new ArrayList<String>();
    }

    public DBFile(String label, String filePath) {
        this.label = label;
        this.filePath = filePath;
        data = new ArrayList<String>();
    }

    // END Constructors ------------------------------------
    // Getters & Setters -----------------------------------
    public String GetLabel() {
        return label + extension;
    }

    public void SetLabel(String value) {
        label = value;
    }

    public String GetFilePath() {
        return filePath;
    }

    public void SetFilePath(String value) {
        filePath = value;
    }

    public String GetFullFilePath() {
        return DEFAULT_PATH_PREFIX + filePath + label + extension;
    }

    public List<String> GetData() {
        return data;
    }
    // END Getters & Setters -------------------------------

    // Runtime Data I/O ------------------------------------
    public void AddData(String toWrite) {
        data.add(toWrite);
    }
    public void DeleteData(){
        data = new ArrayList<String>();
    }
    // END Runtime Data I/O --------------------------------

    // File I/O --------------------------------------------

    String DataToString() {
        StringBuilder dts = new StringBuilder();
        for (String string : data) {
            dts.append(string);
            dts.append('\n');
        }
        return dts.toString();
    }

    public void StoreFile() {
        try {
            File toStore = new File(GetFullFilePath());
            toStore.createNewFile();
            
            FileWriter os = new FileWriter(toStore);
            os.write(DataToString());
            os.close();
        } catch (Exception e) {
            System.err.println("An error occured");
            e.printStackTrace();
        }
    }

    public void EmptyFile() {
        try {
            File toStore = new File(GetFullFilePath());
            toStore.createNewFile();
            PrintWriter pw = new PrintWriter(GetFullFilePath());
            pw.close();
        } catch (Exception e) {
            System.err.println("An error occured");
            e.printStackTrace();
        }
    }
    // END File I/O ----------------------------------------
}