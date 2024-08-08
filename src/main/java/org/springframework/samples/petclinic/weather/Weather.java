package org.springframework.samples.petclinic.weather;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Weather {

	private final LocalDate date;

	private final String city;

	private final int minTemperature;

	private final int maxTemperature;

	private final int avgTemperature;

	private final WeatherDuringDay weatherDuringDay;

	public Weather(LocalDate date, String city, int minTemperature, int maxTemperature, int avgTemperature,
			WeatherDuringDay weatherDuringDay) {
		this.date = date;
		this.city = city;
		this.minTemperature = minTemperature;
		this.maxTemperature = maxTemperature;
		this.avgTemperature = avgTemperature;
		this.weatherDuringDay = weatherDuringDay;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getCity() {
		return city;
	}

	public String getFormattedDate() {
		var formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");

		return formatter.format(date);
	}

	public int getMinTemperature() {
		return minTemperature;
	}

	public int getMaxTemperature() {
		return maxTemperature;
	}

	public int getAvgTemperature() {
		return avgTemperature;
	}

	public WeatherDuringDay getWeatherDuringDay() {
		return weatherDuringDay;
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
