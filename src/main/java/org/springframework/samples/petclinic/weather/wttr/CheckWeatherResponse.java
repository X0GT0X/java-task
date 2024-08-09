package org.springframework.samples.petclinic.weather.wttr;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CheckWeatherResponse {

	List<Weather> weather;

	public CheckWeatherResponse(List<Weather> weather) {
		this.weather = weather;
	}

}
