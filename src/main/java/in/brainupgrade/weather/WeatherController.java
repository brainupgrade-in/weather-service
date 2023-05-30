package in.brainupgrade.weather;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import in.brainupgrade.weather.model.currentWeather.City;
import in.brainupgrade.weather.model.currentWeather.WeatherForecast;
import in.brainupgrade.weather.model.openmeteo.OpenMeteoService;

@RestController
public class WeatherController {

	@Autowired
	private OpenMeteoService openMeteoService;

	@PostMapping(value = "/get-weather",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Optional<WeatherForecast> fetchWeatherForecastFromRemoteApi(@RequestBody City city) {
		return openMeteoService.fetchWeatherForecastFromRemoteApi(city);
	}

	@PostMapping(value = "/get-cities")
	@ResponseBody
	public Optional<City[]> fetchCitiesFromRemoteApi(@RequestBody String cityInput) {
		return openMeteoService.fetchCitiesFromRemoteApi(cityInput);
	}
}
