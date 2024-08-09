package org.springframework.samples.petclinic.weather;

import java.time.LocalDate;

public interface WeatherChecker {

	CheckWeatherResult check(String city, LocalDate date);

}
