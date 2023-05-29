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

@RestController
public class WeatherController {

	@Autowired
	private RemoteApiFetcher remoteApiFetcher;

	@PostMapping(value = "/get-weather",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Optional<WeatherForecast> fetchWeatherForecastFromRemoteApi(@RequestBody City city) {
		return remoteApiFetcher.fetchWeatherForecastFromRemoteApi(city);
	}

	@PostMapping(value = "/get-cities")
	@ResponseBody
	public Optional<City[]> fetchCitiesFromRemoteApi(@RequestBody String cityInput) {
		return remoteApiFetcher.fetchCitiesFromRemoteApi(cityInput);
	}
}
