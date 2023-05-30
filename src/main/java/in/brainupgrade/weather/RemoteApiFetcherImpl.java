package in.brainupgrade.weather;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.node.ObjectNode;

import in.brainupgrade.weather.model.currentWeather.City;
import in.brainupgrade.weather.model.currentWeather.WeatherForecast;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RemoteApiFetcherImpl implements RemoteApiFetcher {

	@Value("${city.service.provider}")
	private String cityURL;
	@Value("${weather.service.provider}")
	private String weatherURL;
	RestTemplate restTemplate = new RestTemplate();

	public Optional<WeatherForecast> fetchWeatherForecastFromRemoteApi(City city) {
		log.info("Weather info requested for the city: " + city.getTitle());
		// String weatherUrl = "https://www.metaweather.com/api/location/" + city.getWoeid();
		try {
			// Optional<WeatherForecast> result = Optional
			// 		.ofNullable(restTemplateWeather.getForObject(weatherUrl, WeatherForecast.class));
			ObjectNode result = restTemplate.getForObject(cityURL, ObjectNode.class, city.getWoeid());
			// String address = result.get("results").get(0).get("formatted_address").textValue();
			WeatherForecast weatherForecast =  new WeatherForecast();
			weatherForecast.setLattLong(city.getLattLong());
			weatherForecast.setSunRise("");

			log.info(String.format("Returning the result for the city %s", city.getTitle()));
			return null;
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
