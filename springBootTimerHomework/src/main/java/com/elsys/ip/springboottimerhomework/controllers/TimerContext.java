package com.elsys.ip.springboottimerhomework.controllers;

import java.util.Timer;
import java.util.TimerTask;

public class TimerContext {
	private String id;
	private String name;
	private Integer timeInSeconds;

	private Timer timer;

	public TimerContext(String id, String name, Integer timeInSeconds) {
		this.id = id;
		this.name = name;
		this.timeInSeconds = timeInSeconds;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getTimeInSeconds() {
		return timeInSeconds;
	}

	public void startTimer() {
		timer = new Timer(name);
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				synchronized (timeInSeconds) {
					if (timeInSeconds > 0) {
						timeInSeconds -= 1;
					} else {
						cancel();
						timeInSeconds = 0;
					}
				}
			}
		}, 0, 1000);
	}

	public boolean isCompleted() {
		synchronized (timeInSeconds) {
			return timeInSeconds == 0;
		}
	}
}
