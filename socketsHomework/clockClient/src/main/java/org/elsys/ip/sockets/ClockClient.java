package org.elsys.ip.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClockClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final String host;
    private final int port;

    public ClockClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startConnection() throws IOException {
        try {
            clientSocket = new Socket(host, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            AtomicBoolean closed = new AtomicBoolean(false);
            Thread thread = new Thread(new InputReader(in, () -> {
                System.out.println("server disconnect");
                System.exit(0);
            }));
            thread.start();

            out.println(ZoneId.systemDefault().getRules().getStandardOffset(Instant.now()).getId());

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if (closed.get()) {
                    break;
                }
                out.println(line);
            }
        } catch (UnknownHostException ex) {
            System.err.println("invalid host");
            System.exit(3);
        } catch (ConnectException ex) {
            System.err.println("connection not possible");
            System.exit(4);
        } finally {
            if (clientSocket != null) clientSocket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        }
    }

    static class InputReader implements Runnable {
        private final BufferedReader in;
        private final Runnable closeClient;

        public InputReader(BufferedReader in, Runnable closeClient) {
            this.in = in;
            this.closeClient = closeClient;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String line = in.readLine();
                    if (line == null) { //Connection is closed
                        closeClient.run();
                        break;
                    }
                    System.out.println(line);
                }
            } catch (IOException ioException) {
                System.err.println(ioException.getMessage());
                ioException.printStackTrace();
                closeClient.run();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String host = null;
        Integer port = null;

        try {
            if (args.length != 1) {
                throw new IllegalArgumentException("Too many arguments");
            }
            String[] splitArgs = args[0].split(":");
            if (splitArgs.length == 2) {
                host = splitArgs[0];
                String portString = splitArgs[1];
                port = Integer.parseInt(portString);
                if (port <= 0 || port > 65535) {
                    throw new IllegalArgumentException("port is not correct");
                }
            }
        } catch (IllegalArgumentException ex) {
            System.err.println("invalid arguments");
            System.exit(1);
        }

        ClockClient client = new ClockClient(host, port);
        client.startConnection();
    }
}
