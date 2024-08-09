package org.springframework.samples.petclinic.weather;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class Weather {

	private final LocalDate date;

	private final String city;

	private final int minTemperature;

	private final int maxTemperature;

	private final int avgTemperature;

	private final WeatherDuringDay weatherDuringDay;

	public String getFormattedDate() {
		var formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");

		return formatter.format(date);
	}

	public double getAvgHumidity() {
		var averageHumidity = getWeatherDuringDay().hourly()
			.stream()
			.map(HourlyWeatherItem::humidity)
			.mapToDouble(h -> h)
			.average();

		if (averageHumidity.isPresent()) {
			return Math.round(averageHumidity.getAsDouble());
		}

		return 0;
	}

}
