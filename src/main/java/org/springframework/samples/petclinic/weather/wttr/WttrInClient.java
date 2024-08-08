package org.springframework.samples.petclinic.weather.wttr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WttrInClient {

	public static final String BASE_URL = "https://wttr.in";

	private final RestTemplate restTemplate;

	@Autowired
	public WttrInClient(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.rootUri(BASE_URL).build();
	}

	public CheckWeatherResponse checkWeatherIn(String city) {
		return restTemplate.getForObject("/{city}?format=j1", CheckWeatherResponse.class, city);
	}

}
