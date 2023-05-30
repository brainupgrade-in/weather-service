package in.brainupgrade.weather.model.openmeteo;

import java.util.Optional;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;

import in.brainupgrade.weather.model.currentWeather.City;
import in.brainupgrade.weather.model.currentWeather.ConsolidatedWeather;
import in.brainupgrade.weather.model.currentWeather.WeatherForecast;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OpenMeteoService {
    
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
			ObjectNode result = restTemplate.getForObject(weatherURL, ObjectNode.class, city.getLatitude(),city.getLongitude());
			// String address = result.get("results").get(0).get("formatted_address").textValue();
            Double latitude = result.get("latitude").doubleValue();
            Double longitude = result.get("longitude").doubleValue();
            String timezone = result.get("timezone").textValue();
            Double temperature = result.get("current_weather").get("temperature").doubleValue();
            String weathercode = result.get("current_weather").get("weathercode").textValue();
            Double tempMax = result.get("daily").get("temperature_2m_max").get(0).doubleValue();
            Double tempMin = result.get("daily").get("temperature_2m_min").get(0).doubleValue();
            String sunset = result.get("daily").get("sunset").get(0).textValue();
            String sunrise = result.get("daily").get("sunrise").get(0).textValue();
            Double uvIndexMax = result.get("daily").get("uv_index_max").get(0).doubleValue();
            Double windspeed = result.get("current_weather").get("windspeed").doubleValue();
            Double winddirection = result.get("current_weather").get("winddirection").doubleValue();
            String time = result.get("current_weather").get("time").textValue();

			WeatherForecast weatherForecast =  new WeatherForecast();
			weatherForecast.setLattLong(city.getLattLong());
			weatherForecast.setTimezone(timezone);
            weatherForecast.setTimezoneName(timezone);
            weatherForecast.setAdditionalProperty("windspeed", windspeed);
            weatherForecast.setAdditionalProperty("winddirection", winddirection);
            weatherForecast.setAdditionalProperty("temperature", temperature);
            weatherForecast.setSunRise(sunrise);
            weatherForecast.setSunSet(sunset);
            
            weatherForecast.setTitle(city.getTitle());
            weatherForecast.setTime(time);
            ConsolidatedWeather consWeather = new ConsolidatedWeather();
            consWeather.setTheTemp(temperature);
            consWeather.setWindDirection(winddirection);
            consWeather.setWindSpeed(windspeed);
            consWeather.setMaxTemp(tempMax);
            consWeather.setMinTemp(tempMin);
            consWeather.setWeatherStateName(weathercode);
            consWeather.setWeatherStateAbbr(weathercode);
            consWeather.setAirPressure(0d);
            List<ConsolidatedWeather> weatherAddon = new ArrayList<ConsolidatedWeather>();
            weatherAddon.add(consWeather);
            weatherForecast.setConsolidatedWeather(weatherAddon);

			log.info(String.format("Returning the result for the city %s", city.getTitle()));
			return Optional.ofNullable(weatherForecast);
		} catch (RestClientException ex) {
			log.error("Error during fetching WeatherForecast from remote API.", ex);
		} finally {
			log.debug("Completed fetching WeatherForecast from remote API.");
		}
		return Optional.empty();
	}

	public Optional<City[]> fetchCitiesFromRemoteApi(String cityInput) {
		log.info("Fetching cities for the given input: " + cityInput);
		try {
			// Optional<City[]> result = Optional.ofNullable(restTemplateCity.getForObject(cityUrl, City[].class));
            ObjectNode response = restTemplate.getForObject(cityURL, ObjectNode.class,cityInput);
            JsonNode results = response.get("results");
            Iterator<JsonNode> elements = results.elements();
            List<City> cities = new ArrayList<City>();
            
            while (elements.hasNext()){
                JsonNode node = elements.next();
                Integer id = node.get("id").intValue();
                String cityname = node.get("name").textValue();
                Double lattitude = node.get("latitude").doubleValue();
                Double longitude = node.get("longitude").doubleValue();
                String country = node.get("country").textValue();
                City city = new City();
                city.setWoeid(id);
                city.setLattLong(lattitude+","+longitude);
                city.setLatitude(lattitude);
                city.setLongitude(longitude);
                city.setTitle(cityname+", "+country);
                city.setLocationType("City");
                cities.add(city);
                
            }
            City[] cits = new City[cities.size()];
			log.info(String.format("Returning the result for the input %s %s", cityInput, cits));
			return Optional.ofNullable(cities.toArray(cits));

		}catch (RestClientException ex) {
			log.error("Error during fetching City[] from remote API.", ex);
		} catch (NullPointerException npe) {
			log.error("Error during fetching City[] from remote API.", npe);
		} finally {
			log.debug("Completed fetching City[] from remote API.");
		}
		return Optional.empty();
	}
}
