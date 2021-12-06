package com.elsys.ip.springboottimerhomework.timer;

import com.elsys.ip.springboottimerhomework.dto.RequestDTO;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface TimeMixin {
    class Duration {

        int duration;

        Duration(String duration) {
            Pattern pattern = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d)");
            Matcher matcher = pattern.matcher(duration);
            if (!matcher.matches()) {
                throw new TimeFormatException("Wrong time format!");
            }

            this.duration = getTimeInSeconds(matcher.group(1));
        }

        Duration(Integer hours, Integer minutes, Integer seconds) {
            this.duration = getTimeInSeconds(hours, minutes, seconds);
        }

        public int getDuration() {
            return duration;
        }

        private int getTimeInSeconds(String time) {
            List<Integer> timeGroups = Arrays.stream(time.split(":")).map(Integer::parseInt).collect(Collectors.toList());
            return getTimeInSeconds(timeGroups.get(0), timeGroups.get(1), timeGroups.get(2));
        }

        private int getTimeInSeconds(Integer... timeGroups) {
            return (timeGroups[0] != null ? timeGroups[0] * 60 * 60 : 0) + (timeGroups[1] != null ? timeGroups[1] * 60 : 0) + (timeGroups[2] != null ? timeGroups[2] : 0);
        }
    }

    default Duration createDuration(RequestDTO request) {
        if (request.time() != null) {
            if (request.hours() != null || request.minutes() != null || request.seconds() != null) {
                throw new TimeFormatException("Both time and hours, minutes, seconds present in the request body!");
            } else {
                return new Duration(request.time());
            }
        } else {
            return new Duration(request.hours(), request.minutes(), request.seconds());
        }
    }
}
