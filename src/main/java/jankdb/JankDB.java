package jankdb;

import java.io.IOException;

import jankdb.server.SocketServer;

public class JankDB{
    public static void main(String[] args) throws IOException{
        SocketServer server = new SocketServer(8080);
        
        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nServer is shutting down...");
            server.stopServer();
        }));

        try {
            server.StartServer();
        } catch (Exception e) {
            e.printStackTrace();
            server.stopServer();
        }
    }
}