package org.springframework.samples.petclinic.weather;

public class CheckWeatherResult {

	public Weather weather;

	public CheckWeatherError error;

	public static CheckWeatherResult success(Weather weather) {
		return new CheckWeatherResult(weather);
	}

	public static CheckWeatherResult error(CheckWeatherError error) {
		return new CheckWeatherResult(error);
	}

	private CheckWeatherResult(Weather weather) {
		this.weather = weather;
	}

	private CheckWeatherResult(CheckWeatherError error) {
		this.error = error;
	}

	public boolean isSuccessful() {
		return error == null && weather != null;
	}

	public boolean isFailure() {
		return !isSuccessful();
	}

}
