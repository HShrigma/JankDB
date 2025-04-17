package jankdb;

public class DBFile {
    String label;
    public DBFile(){
        label = "";
    }
    public DBFile(String value){
        label = value;
    }
    public String GetLabel() {return label;}
    public void SetLabel(String value) {label = value;}
}