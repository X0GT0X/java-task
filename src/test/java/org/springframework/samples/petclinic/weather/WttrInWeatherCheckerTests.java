package org.springframework.samples.petclinic.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.weather.wttr.CheckWeatherResponse;
import org.springframework.samples.petclinic.weather.wttr.HourlyWeather;
import org.springframework.samples.petclinic.weather.wttr.Weather;
import org.springframework.samples.petclinic.weather.wttr.WttrInClient;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(WttrInClient.class)
public class WttrInWeatherCheckerTests {

	@Autowired
	WttrInClient client;

	@Autowired
	private MockRestServiceServer server;

	@Test
	void testThat_ReturnsSuccessResult_WithWeather() throws JsonProcessingException {
		server.expect(requestTo("/Warsaw?format=j1"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(weatherInWarsawJSON(), MediaType.APPLICATION_JSON));

		var weatherChecker = new WttrInWeatherChecker(client);

		var result = weatherChecker.check("Warsaw", LocalDate.of(2024, 8, 8));

		assertThat(result.isSuccessful()).isTrue();

		var weather = result.weather;
		assertThat(weather.getMinTemperature()).isEqualTo(12);
		assertThat(weather.getMaxTemperature()).isEqualTo(32);
		assertThat(weather.getAvgTemperature()).isEqualTo(22);
		assertThat(weather.getAvgHumidity()).isEqualTo(70);
		assertThat(weather.getCity()).isEqualTo("Warsaw");
		assertThat(weather.getFormattedDate()).isEqualTo("Aug 8, 2024");
		assertThat(weather.getWeatherDuringDay().hourly().size()).isEqualTo(3);
	}

	@Test
	void testThat_ReturnsFailureResult_IfWeatherNotAvailableForGivenDate() throws JsonProcessingException {
		server.expect(requestTo("/Warsaw?format=j1"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(weatherInWarsawJSON(), MediaType.APPLICATION_JSON));

		var weatherChecker = new WttrInWeatherChecker(client);

		var result = weatherChecker.check("Warsaw", LocalDate.of(2024, 8, 10));

		assertThat(result.isFailure()).isTrue();
		assertThat(result.error.message()).isEqualTo("Weather for given date not found");
	}

	@Test
	void testThat_ReturnsFailureResult_IfWeatherNotAvailableForGivenCity() {
		server.expect(requestTo("/Neverland?format=j1"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

		var weatherChecker = new WttrInWeatherChecker(client);

		var result = weatherChecker.check("Neverland", LocalDate.of(2024, 8, 10));

		assertThat(result.isFailure()).isTrue();
		assertThat(result.error.message()).isEqualTo("Weather for given city not found");
	}

	@Test
	void testThat_ReturnsFailureResult_IfExceptionOccurred() {
		server.expect(requestTo("/Warsaw?format=j1")).andExpect(method(HttpMethod.GET)).andRespond(response -> {
			throw new ResourceAccessException("Some error");
		});

		var weatherChecker = new WttrInWeatherChecker(client);

		var result = weatherChecker.check("Warsaw", LocalDate.of(2024, 8, 10));

		assertThat(result.isFailure()).isTrue();
		assertThat(result.error.message()).isEqualTo("Some error");
	}

	private CheckWeatherResponse weatherInWarsaw() {
		var hourlyWeather = new ArrayList<HourlyWeather>();
		hourlyWeather.add(new HourlyWeather("70", "12", "600"));
		hourlyWeather.add(new HourlyWeather("60", "32", "1200"));
		hourlyWeather.add(new HourlyWeather("80", "22", "1800"));

		var weather = new ArrayList<Weather>();
		weather.add(new Weather(LocalDate.of(2024, 8, 8), "22", "12", "32", hourlyWeather));

		return new CheckWeatherResponse(weather);
	}

	private String weatherInWarsawJSON() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		return mapper.writeValueAsString(weatherInWarsaw());
	}

}
