package org.elsys.ip.servlet;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Stopwatch {
    private final List<Instant> laps = new ArrayList<>();

    public Stopwatch() {
        laps.add(Instant.now());
    }

    public Duration get() {
        return Duration.between(laps.get(0), Instant.now());
    }

    public Duration lap() {
        Instant now = Instant.now();
        Instant lastLap = laps.get(laps.size()-1);
        laps.add(now);
        return Duration.between(lastLap, now);
    }

    public List<Durations> stop() {
        lap();
        List<Durations> result = new ArrayList<>();
        Instant previous = laps.get(0);
        Instant begin = previous;
        for (int i = 1; i< laps.size(); ++i) {
            Instant lap = laps.get(i);
            result.add(new Durations(
                    Duration.between(previous, lap),
                    Duration.between(begin, lap)));
            previous = lap;
        }

        return result;
    }

    public static class Durations {
        private final Duration fromLap;
        private final Duration fromBegin;

        Durations(Duration fromLap, Duration fromBegin) {
            this.fromLap = fromLap;
            this.fromBegin = fromBegin;
        }

        public Duration getFromLap() {
            return fromLap;
        }

        public Duration getFromBegin() {
            return fromBegin;
        }
    }
}
