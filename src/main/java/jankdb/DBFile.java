package jankdb;

public class DBFile {
    String label;
    String filePath;

    // Constructors ----------------------------------------
    public DBFile() {
        label = "";
        filePath = "";
    }

    public DBFile(String label) {
        this.label = label;
        filePath = "";
    }

    public DBFile(String label, String filePath) {
        this.label = label;
        this.filePath = filePath;
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
    // END Getters & Setters -------------------------------
}