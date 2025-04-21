package jankdb;

import java.io.IOException;

import jankdb.server.SocketServer;

public class JankDB{
    public static void main(String[] args) throws IOException{
        try{
            new SocketServer(8080).StartServer();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}