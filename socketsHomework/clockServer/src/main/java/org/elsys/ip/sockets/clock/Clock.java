package org.elsys.ip.sockets.clock;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Clock implements ClockOperation {

    private static final List<Integer> AVAILABLE_TIME_ZONES = Stream.of(
            "+10:30", "+11:00", "-07:00", "+09:30", "+03:00", "+05:00", "-02:00", "+12:45", "+12:00", "+06:00", "-09:30", "+07:00",
            "+04:30", "+00:00", "-11:00", "+05:30", "-05:00", "-03:00", "+03:30", "+09:00", "+02:00", "-10:00", "-04:00", "+10:00",
            "-01:00", "+06:30", "-09:00", "+01:00", "-06:00", "+14:00", "+04:00", "-03:30", "-12:00", "+13:00", "-08:00", "+08:00",
            "+05:45", "+08:45"
    ).map(Clock::getTimeZoneOffset).collect(Collectors.toList());

    private final int newZone;

    public Clock(List<String> operators, String clientTimeZone) {
        newZone = operators.isEmpty() ? getTimeZoneOffset(clientTimeZone) : getTimeZoneOffset(operators.get(0));
    }

    @Override
    public String perform() {
        if (!AVAILABLE_TIME_ZONES.contains(newZone)) {
            return "invalid time zone";
        }

        return LocalDateTime.now(ZoneOffset.UTC).plusSeconds(newZone).format(getClockFormat());
    }

    private static int getTimeZoneOffset(String timeZone) {
        char sign = timeZone.charAt(0);
        List<Integer> zone = Arrays.stream(timeZone.substring(1).split(":")).map(Integer::parseInt).collect(Collectors.toList());
        int seconds = zone.get(0) * 60 * 60 + zone.get(1) * 60;
        return sign == '+' ? seconds : -1 * seconds;
    }

    private DateTimeFormatter getClockFormat() {
        return DateTimeFormatter.ofPattern("HH:mm");
    }
}
