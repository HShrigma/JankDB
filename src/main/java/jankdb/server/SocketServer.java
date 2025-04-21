package jankdb.server;

import java.io.*;
import java.net.*;
import java.util.UUID;
import jankdb.*;

public class SocketServer {
    private final int port;
    private final TableRepository tableRepo;
    private ServerSocket server;

    public SocketServer(int portToListen) {
        this.port = portToListen;
        this.tableRepo = new TableRepository(); // Single shared repository
    }

    public void StartServer() throws IOException {
        try {
            server = new ServerSocket(port);
            System.out.println("Started Listening on Port: " + port);
            
            while (true) {  // Continuous accept loop
                Socket client = server.accept();
                handleClient(client);
            }
        } finally {
            if (server != null) server.close();
        }
    }

    private void handleClient(Socket client) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(client.getInputStream()));
        
        String userKey = UUID.randomUUID().toString();
        REPLCLIManager clientCLI = new REPLCLIManager(tableRepo); // Inject repository
        
        try {
            clientCLI.StartClientSide(out, in, userKey);
        } finally {
            client.close();
        }
    }
}