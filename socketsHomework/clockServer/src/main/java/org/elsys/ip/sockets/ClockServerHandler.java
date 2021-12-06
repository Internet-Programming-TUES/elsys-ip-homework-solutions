package org.elsys.ip.sockets;

import java.io.IOException;
import java.net.ServerSocket;

public class ClockServerHandler {
    private ServerSocket serverSocket;
    private final int port;

    public ClockServerHandler(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            new ClientHandler(serverSocket.accept()).start();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }
}