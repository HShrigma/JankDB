package jankdb;

import java.io.*;

public class DBFile {
    String label;
    String filePath;
    String extension = ".txt";
    final String DEFAULT_PATH_PREFIX = "src/main/resources/"; 
    StringBuilder data;

    // Constructors ----------------------------------------
    public DBFile() {
        label = "";
        filePath = "";
        data = new StringBuilder();
    }

    public DBFile(String label) {
        this.label = label;
        filePath = "";
        data = new StringBuilder();
    }

    public DBFile(String label, String filePath) {
        this.label = label;
        this.filePath = filePath;
        data = new StringBuilder();
    }

    // END Constructors ------------------------------------
    // Getters & Setters -----------------------------------
    public String GetLabel() {
        return label;
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

    public String GetData() {
        return data.toString();
    }
    // END Getters & Setters -------------------------------

    // Runtime Data I/O ------------------------------------
    public void WriteData(String toWrite) {
        data.append(toWrite);
    }
    // END Runtime Data I/O --------------------------------

    // File I/O --------------------------------------------
    public void StoreFile() {
        try {
            File toStore = new File(GetFullFilePath());
            if (toStore.createNewFile()) {
                System.out.println("Created new file");
            } else {
                System.out.println("File exists already");
            }

            FileWriter os = new FileWriter(toStore);
            os.write(GetData());
            os.close();
        } catch (Exception e) {
            System.err.println("An error occured");
            e.printStackTrace();
        }

    }
    // END File I/O ----------------------------------------
}