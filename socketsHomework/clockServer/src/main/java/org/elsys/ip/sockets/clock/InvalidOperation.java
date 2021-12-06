package org.elsys.ip.sockets.clock;

public class InvalidOperation implements ClockOperation {
    @Override
    public String perform() {
        return "invalid input";
    }
}
