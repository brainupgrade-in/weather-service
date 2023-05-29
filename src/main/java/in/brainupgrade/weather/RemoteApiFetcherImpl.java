package in.brainupgrade.weather;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import in.brainupgrade.weather.model.currentWeather.City;
import in.brainupgrade.weather.model.currentWeather.WeatherForecast;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RemoteApiFetcherImpl implements RemoteApiFetcher {

	public Optional<WeatherForecast> fetchWeatherForecastFromRemoteApi(City city) {
		log.info("Weather info requested for the city: " + city.getTitle());
		String weatherUrl = "https://www.metaweather.com/api/location/" + city.getWoeid();
		try {
			RestTemplate restTemplateWeather = new RestTemplate();
			Optional<WeatherForecast> result = Optional
					.ofNullable(restTemplateWeather.getForObject(weatherUrl, WeatherForecast.class));
			log.info(String.format("Returning the result for the city %s", city.getTitle()));
			return result;
		} catch (RestClientException ex) {
			log.error("Error during fetching WeatherForecast from remote API.", ex);
		} finally {
			log.debug("Completed fetching WeatherForecast from remote API.");
		}
		return Optional.empty();
	}

	public Optional<City[]> fetchCitiesFromRemoteApi(String cityInput) {
		log.info("Fetching cities for the given input: " + cityInput);
		String cityUrl = "https://www.metaweather.com/api/location/search/?query=" + cityInput.toLowerCase();
		try {
			RestTemplate restTemplateCity = new RestTemplate();
			Optional<City[]> result = Optional.ofNullable(restTemplateCity.getForObject(cityUrl, City[].class));
			log.info(String.format("Returning the result for the input %s", cityInput));
			return result;

		} catch (RestClientException ex) {
			log.error("Error during fetching City[] from remote API.", ex);
		} finally {
			log.debug("Completed fetching City[] from remote API.");
		}
		return Optional.empty();
	}
}
