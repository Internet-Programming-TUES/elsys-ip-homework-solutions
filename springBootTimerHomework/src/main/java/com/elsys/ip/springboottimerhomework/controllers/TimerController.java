package com.elsys.ip.springboottimerhomework.controllers;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elsys.ip.springboottimerhomework.dto.RequestDTO;
import com.elsys.ip.springboottimerhomework.dto.ResponseDTO;
import com.elsys.ip.springboottimerhomework.timer.TimeFormatException;
import com.elsys.ip.springboottimerhomework.timer.TimeMixin;

@RestController
public class TimerController implements TimeMixin {

	private final Map<String, TimerContext> db = new HashMap<>();

	@PostMapping("/timer")
	public ResponseEntity<?> createTimer(@RequestBody RequestDTO request,
			@RequestHeader(name = "time-format", defaultValue = "time") String timeFormat) {
		try {
			TimerContext timerContext = new TimerContext(UUID.randomUUID().toString(), request.name(),
					createDuration(request).getDuration());
			db.put(timerContext.getId(), timerContext);
			timerContext.startTimer();

			return ResponseEntity.created(URI.create("/timer/" + timerContext.getId())).headers(createHttpHeaders())
					.body(new ResponseDTO(timerContext.getId(), timerContext.getName(),
							timeFormat.equals("time") ? timerContext.getTimeInSeconds() : null,
							timeFormat.equals("seconds") ? timerContext.getTimeInSeconds() : null,
							timeFormat.equals("hours-minutes-seconds") ? timerContext.getTimeInSeconds() / 3600 : null,
							timeFormat.equals("hours-minutes-seconds") ? (timerContext.getTimeInSeconds() / 60) % 60 : null,
							timeFormat.equals("hours-minutes-seconds") ? timerContext.getTimeInSeconds() % 60 : null, null));
		} catch (TimeFormatException | NumberFormatException e) {
			return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
		}
	}

	@GetMapping("/timer/{id}")
	public ResponseEntity createTimer(@PathVariable("id") String id, @RequestParam(name = "long", defaultValue = "false") boolean isLong,
			@RequestHeader(value = "time-format", defaultValue = "time") String timeFormat) throws InterruptedException {
		if (!db.containsKey(id)) {
			return new ResponseEntity<>("Missing timer!", NOT_FOUND);
		}
		TimerContext timerContext = db.get(id);
		if (isLong) {
			for (int i = 0; i < 10; i++) {
				if (timerContext.isCompleted()) {
					break;
				}
				Thread.sleep(1000);
			}
		}

		return ResponseEntity.ok().headers(createHttpHeaders())
				.body(new ResponseDTO(id, timerContext.getName(), timeFormat.equals("time") ? timerContext.getTimeInSeconds() : null,
						timeFormat.equals("seconds") ? timerContext.getTimeInSeconds() : null,
						timeFormat.equals("hours-minutes-seconds") ? timerContext.getTimeInSeconds() / 3600 : null,
						timeFormat.equals("hours-minutes-seconds") ? (timerContext.getTimeInSeconds() / 60) % 60 : null,
						timeFormat.equals("hours-minutes-seconds") ? timerContext.getTimeInSeconds() % 60 : null,
						timerContext.isCompleted()));
	}

	private HttpHeaders createHttpHeaders() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("active-timers",
				String.valueOf(db.entrySet().stream().filter(entry -> !entry.getValue().isCompleted()).count()));

		return responseHeaders;
	}
}
