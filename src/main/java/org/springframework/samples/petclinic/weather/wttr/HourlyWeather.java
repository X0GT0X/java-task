package org.springframework.samples.petclinic.weather.wttr;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HourlyWeather {

	String humidity;

	String tempC;

	String time;

}
