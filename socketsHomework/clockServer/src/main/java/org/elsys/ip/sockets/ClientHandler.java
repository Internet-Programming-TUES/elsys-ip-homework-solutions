package org.elsys.ip.sockets;

import org.elsys.ip.sockets.clock.ClockFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientHandler extends Thread {
    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientTimeZone = in.readLine();
            while (true) {
                String line = in.readLine().toLowerCase();
                if (line.equals("exit") || line.equals("quit")) {
                    // Break the loop, the socket will close
                    break;
                }
                out.println(handleOperation(line, clientTimeZone));
            }
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        } finally {
            dispose();
        }
    }


    private String handleOperation(String operation, String clientTimeZone) {
        return ClockFactory.getInstance().create(operation, clientTimeZone).perform();
    }

    private void dispose() {
        try {
            if (clientSocket != null) clientSocket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (Throwable t) {
        }
    }
}