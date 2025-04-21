package jankdb.server;

import java.io.*;
import java.net.*;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jankdb.*;

public class SocketServer {
    private final int port;
    private final TableRepository tableRepo;
    private ServerSocket server;
    private ExecutorService threadPool;
    private volatile boolean isRunning = true;

    public SocketServer(int portToListen) {
        this.port = portToListen;
        this.tableRepo = new TableRepository();
        this.threadPool = Executors.newCachedThreadPool();
    }

    public void StartServer() throws IOException {
        try {
            server = new ServerSocket(port);
            System.out.println("Started Listening on Port: " + port);

            while (isRunning) {
                Socket client = server.accept();
                threadPool.execute(() -> {
                    try {
                        handleClient(client);
                    } catch (IOException e) {
                        System.err.println("Error handling client: " + e.getMessage());
                    }
                });
            }
        } finally {
            shutdown();
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

    public void shutdown() {
        isRunning = false;
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }

        if (server != null) {
            try {
                server.close();
            } catch (IOException e) {
                System.err.println("Error closing server: " + e.getMessage());
            }
        }
    }

    public void stopServer() {
        shutdown();
    }
}