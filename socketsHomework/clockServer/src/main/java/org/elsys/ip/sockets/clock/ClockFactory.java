package org.elsys.ip.sockets.clock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClockFactory {
    private static ClockFactory instance;

    private ClockFactory() {
    }

    public static synchronized ClockFactory getInstance() {
        if (instance == null) {
            instance = new ClockFactory();
        }

        return instance;
    }

    public ClockOperation create(String operation, String clientTimeZone) {
        if (!isValidOperation(operation)) {
            return new InvalidOperation();
        }

        List<String> operators = Arrays.asList(operation.split(" "));
        return switch (operators.get(0)) {
            case "time" -> new Clock(new ArrayList<>(operators.subList(1, operators.size())), clientTimeZone);
            default -> new InvalidOperation();
        };
    }

    private boolean isValidOperation(String operation) {
        return operation.matches("^(time|(time [+-]\\d\\d:\\d\\d))$");
    }
}
