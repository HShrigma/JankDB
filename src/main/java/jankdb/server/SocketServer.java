package jankdb.server;

import java.io.*;
import java.net.*;

import jankdb.REPLCLIManager;

public class SocketServer {

    // Port field
    int port;

    // Socket fields
    ServerSocket server;
    Socket client;

    // Response Handling
    PrintWriter out;
    BufferedReader in;

    // Constructor
    public SocketServer(int portToListen) {
        port = portToListen;
    }

    // Control flow
    public void StartServer() throws IOException {
        try {
            // Initialise server socket to port
            server = new ServerSocket(port);
            System.out.println("Started Listening on Port: " + port);
            StartClient();

        } catch (Exception e) {
            System.err.println("An error occured while starting server");
            e.printStackTrace();
        } finally {
            StopClient();
            server.close();
        }
    }

    void StopClient() throws IOException {
        in.close();
        out.close();
        client.close();
    }

    void StartClient() throws IOException {
        // Add client
        client = server.accept();
        System.out.println("Client accepted");
        // Initialise R/W
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        REPLCLIManager clientCLI = new REPLCLIManager();
        clientCLI.StartClientSide(out, in);
    }
}