package org.springframework.samples.petclinic.weather.wttr;

public class HourlyWeather {

	String humidity;

	String tempC;

	String time;

	public HourlyWeather(String humidity, String tempC, String time) {
		this.humidity = humidity;
		this.tempC = tempC;
		this.time = time;
	}

	public String getHumidity() {
		return humidity;
	}

	public String getTempC() {
		return tempC;
	}

	public String getTime() {
		return time;
	}

}
