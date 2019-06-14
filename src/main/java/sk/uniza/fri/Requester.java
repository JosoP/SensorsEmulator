package sk.uniza.fri;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.uniza.fri.api.CityWeather;
import sk.uniza.fri.api.Saying;
import sk.uniza.fri.api.Weather;
import sk.uniza.fri.api.WeatherList;
import sk.uniza.fri.client.OpenWeatherRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Requester extends Thread {
    // assumes the current class is called MyLogger
    private final static Logger LOGGER = Logger.getLogger(Requester.class.getName());
    private final static String API_KEY = "660955c02abd1ab4f655aced38437e7b";

    private final OpenWeatherRequest openWeatherRequest;
    private final ArrayList cityIdList;

    public Requester(OpenWeatherRequest openWeatherRequest) {
        this.openWeatherRequest = openWeatherRequest;
        this.cityIdList = new ArrayList<String>();

        this.cityIdList.add("3496831");
        this.cityIdList.add("874560");
        this.cityIdList.add("476350");
        this.cityIdList.add("798544");
    }


    @Override
    public void run() {
        super.run();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.log(Level.INFO, "Requester thread running");
        while(true){


            requestMoreCitiesWeather(cityIdList);
            //requestOneCityWeather("475279");

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                LOGGER.log(Level.INFO,"Requester thread interrupted");
            }
        }
    }

    private void requestMoreCitiesWeather(ArrayList cityIdList) {

        // convert list of ids to one string in which ids are separated by comma
        String cityIds = cityIdList.toString()
                .replace(" ", "")
                .replace("[", "")
                .replace("]", "");
        LOGGER.log(Level.INFO, "City IDs are " + cityIds);

        Call<WeatherList> request = this.openWeatherRequest.getCityGroup(cityIds);

        request.enqueue(new Callback<WeatherList>() {
            @Override
            public void onResponse(Call<WeatherList> call, Response<WeatherList> response) {
                WeatherList weatherList = response.body();
                if(weatherList != null){
                    List<CityWeather> cities = weatherList.getCityList();

                    LOGGER.log(Level.INFO, "Weather from some cities comes");

                    for (CityWeather city : cities){
                        LOGGER.log(Level.INFO,"Temperature in " + city.getName() + " is " + city.getMain().getTemp() + "°c");
                    }
                } else {
                    LOGGER.log(Level.WARNING, "Not possible to parse response from API, because it is null");
                }
            }

            @Override
            public void onFailure(Call<WeatherList> call, Throwable throwable) {

            }
        });
    }

    private void requestOneCityWeather(String cityID) {
        Call<CityWeather> request = this.openWeatherRequest.getOneCity(cityID);

        request.enqueue(new Callback<CityWeather>() {
            @Override
            public void onResponse(Call<CityWeather> call, Response<CityWeather> response) {
                CityWeather cityWeather = response.body();
                if(cityWeather != null){
                    double temperature = cityWeather.getMain().getTemp();

                    LOGGER.log(Level.INFO,"Temperature in " + cityWeather.getName() + " is " + temperature + "°c");
                }
            }

            @Override
            public void onFailure(Call<CityWeather> call, Throwable throwable) {
                LOGGER.log(Level.INFO,"Request for one city weather failed.");
            }
        });
    }
}
