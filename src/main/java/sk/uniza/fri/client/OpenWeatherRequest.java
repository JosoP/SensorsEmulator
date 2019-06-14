package sk.uniza.fri.client;


import retrofit2.Call;
import retrofit2.http.*;
import sk.uniza.fri.api.CityWeather;
import sk.uniza.fri.api.WeatherList;

import java.util.List;


public interface OpenWeatherRequest {
    /**
     * Request to get weather information about qroup of cities from Open Weather API.
     *
     * @param IDs IDs of cities which about information is requested.
     *
     * @return Call object for this request execution. If everything works fine, in body of response message will be
     * {@link WeatherList} object filled with information about requested cities.
     */
    @GET("/data/2.5/group?units=metric&appid=660955c02abd1ab4f655aced38437e7b")
    Call<WeatherList> getCityGroup(@Query("id") String IDs);

    /**
     * Request to get weather information about one city from Open Weather API.
     *
     * @param ID ID of the city about which information is requested.
     *
     * @return Call object for this request execution. If everything works fine, in body of response message will be
     * {@link CityWeather} object filled with information about requested city.
     */
    @GET("/data/2.5/weather?units=metric&appid=660955c02abd1ab4f655aced38437e7b")
    Call<CityWeather> getOneCity(@Query("id") String ID);
}
