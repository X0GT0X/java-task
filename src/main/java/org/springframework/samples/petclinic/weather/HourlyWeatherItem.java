package org.springframework.samples.petclinic.weather;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record HourlyWeatherItem(int temperature, int humidity, LocalTime time) {
	public String getFormattedTime() {
		var formatter = DateTimeFormatter.ofPattern("HH:mm");

		return time.format(formatter);
	}
}
