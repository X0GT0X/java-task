package org.springframework.samples.petclinic.weather;

import java.util.List;

public record WeatherDuringDay(List<HourlyWeatherItem> hourly) {
}
