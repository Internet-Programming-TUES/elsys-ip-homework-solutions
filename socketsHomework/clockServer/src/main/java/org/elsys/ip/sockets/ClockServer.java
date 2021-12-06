package org.elsys.ip.sockets;

import java.io.IOException;
import java.net.BindException;

public class ClockServer {
    public static void main(String[] args) throws IOException {
        Integer port = null;

        try {
            if (args.length != 1) {
                throw new IllegalArgumentException("Too many arguments");
            }
            port = Integer.parseInt(args[0]);
            if (port <= 0 || port > 65535) {
                throw new IllegalArgumentException("port is not correct");
            }
        } catch (IllegalArgumentException ex) {
            System.err.println("invalid arguments");
            System.exit(1);
        }

        try {
            ClockServerHandler server = new ClockServerHandler(port);
            server.start();
        } catch (BindException ex) {
            System.err.println("port is already in use");
            System.exit(2);
        }
    }
}
