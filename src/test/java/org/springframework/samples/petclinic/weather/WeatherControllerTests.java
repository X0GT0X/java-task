package org.springframework.samples.petclinic.weather;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
@DisabledInNativeImage
@DisabledInAotMode
public class WeatherControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WeatherChecker weatherChecker;

	@Test
	void testThat_SetsWarning_IfCheckWeatherResultIsFailure() throws Exception {
		var date = LocalDate.of(2024, 8, 8);
		given(weatherChecker.check("Warsaw", date))
			.willReturn(CheckWeatherResult.error(new CheckWeatherError("Some error")));

		mockMvc.perform(get("/check-weather").param("city", "Warsaw").param("date", String.valueOf(date)))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("checkWeatherWarning"))
			.andExpect(view().name("fragments/weatherInformation"));
	}

	@Test
	void testThat_SetsWeather_IfCheckWeatherResultIsSuccessful() throws Exception {
		var date = LocalDate.of(2024, 8, 8);

		given(weatherChecker.check("Warsaw", date)).willReturn(CheckWeatherResult.success(weatherInWarsaw(date)));

		mockMvc.perform(get("/check-weather").param("city", "Warsaw").param("date", String.valueOf(date)))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("weather"))
			.andExpect(view().name("fragments/weatherInformation"));
	}

	private Weather weatherInWarsaw(LocalDate date) {
		var hourlyWeatherItem = new HourlyWeatherItem(20, 60, LocalTime.of(12, 0, 0));

		return new Weather(date, "Warsaw", 10, 20, 15, new WeatherDuringDay(List.of(hourlyWeatherItem)));
	}

}
