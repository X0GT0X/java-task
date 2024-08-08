package org.springframework.samples.petclinic.weather.wttr;

import java.util.List;

public class CheckWeatherResponse {

	List<Weather> weather;

	public CheckWeatherResponse(List<Weather> weather) {
		this.weather = weather;
	}

	public List<Weather> getWeather() {
		return weather;
	}

	public CheckWeatherResponse() {
	}

}
