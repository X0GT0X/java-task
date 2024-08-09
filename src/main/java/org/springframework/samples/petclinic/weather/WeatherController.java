package org.springframework.samples.petclinic.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;

@Controller
public class WeatherController {

	private final WeatherChecker weatherChecker;

	@Autowired
	public WeatherController(WeatherChecker weatherChecker) {
		this.weatherChecker = weatherChecker;
	}

	@RequestMapping("/check-weather")
	public String checkWeather(Map<String, Object> model, @RequestParam String city,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		var result = weatherChecker.check(city, date);

		if (result.isFailure()) {
			model.put("checkWeatherWarning", result.error.message());
		}
		else {
			model.put("weather", result.weather);
		}

		return "fragments/weatherInformation";
	}

}
