package org.springframework.samples.petclinic.weather.wttr;

import java.time.LocalDate;
import java.util.List;

public class Weather {

	LocalDate date;

	String avgtempC;

	String mintempC;

	String maxtempC;

	List<HourlyWeather> hourly;

	public Weather(LocalDate date, String avgtempC, String mintempC, String maxtempC, List<HourlyWeather> hourly) {
		this.date = date;
		this.avgtempC = avgtempC;
		this.mintempC = mintempC;
		this.maxtempC = maxtempC;
		this.hourly = hourly;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getAvgtempC() {
		return avgtempC;
	}

	public String getMintempC() {
		return mintempC;
	}

	public String getMaxtempC() {
		return maxtempC;
	}

	public List<HourlyWeather> getHourly() {
		return hourly;
	}

}
