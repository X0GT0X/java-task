package org.springframework.samples.petclinic.weather.wttr;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class Weather {

	LocalDate date;

	String avgtempC;

	String mintempC;

	String maxtempC;

	List<HourlyWeather> hourly;

}
