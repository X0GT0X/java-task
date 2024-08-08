package org.springframework.samples.petclinic.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.weather.wttr.CheckWeatherResponse;
import org.springframework.samples.petclinic.weather.wttr.WttrInClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class WttrInWeatherChecker implements WeatherChecker {

	private final WttrInClient client;

	@Autowired
	public WttrInWeatherChecker(WttrInClient client) {
		this.client = client;
	}

	@Override
	public CheckWeatherResult check(String city, LocalDate date) {
		try {
			var response = client.checkWeatherIn(city);

			if (response == null || response.getWeather() == null) {
				return CheckWeatherResult.error(new CheckWeatherError("Weather for given city not found"));
			}

			var weather = mapResponseToWeatherForDate(response, date, city);

			if (weather == null) {
				return CheckWeatherResult.error(new CheckWeatherError("Weather for given date not found"));
			}

			return CheckWeatherResult.success(weather);
		}
		catch (Exception e) {
			return CheckWeatherResult.error(new CheckWeatherError(e.getMessage()));
		}
	}

	private Weather mapResponseToWeatherForDate(CheckWeatherResponse response, LocalDate date, String city) {
		var weatherForDay = response.getWeather()
			.stream()
			.filter(weather -> weather.getDate().equals(date))
			.findFirst()
			.orElse(null);

		if (weatherForDay == null) {
			return null;
		}

		var weatherDuringDay = new WeatherDuringDay(weatherForDay.getHourly()
			.stream()
			.map(hourlyWeather -> new HourlyWeatherItem(Integer.parseInt(hourlyWeather.getTempC()),
					Integer.parseInt(hourlyWeather.getHumidity()), convertToLocalTime(hourlyWeather.getTime())))
			.toList());

		return new Weather(date, city, Integer.parseInt(weatherForDay.getMintempC()),
				Integer.parseInt(weatherForDay.getMaxtempC()), Integer.parseInt(weatherForDay.getAvgtempC()),
				weatherDuringDay);
	}

	private LocalTime convertToLocalTime(String time) {
		int minutesPastMidnight = Integer.parseInt(time);

		int hours = minutesPastMidnight / 100;
		int minutes = minutesPastMidnight % 100;

		return LocalTime.of(hours, minutes);
	}

}
