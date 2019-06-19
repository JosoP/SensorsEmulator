package sk.uniza.fri;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.uniza.fri.api.*;
import sk.uniza.fri.client.DatabaseApiRequest;
import sk.uniza.fri.client.OpenWeatherRequest;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Requester extends Thread {
    // assumes the current class is called MyLogger
    private final static Logger LOGGER = Logger.getLogger(Requester.class.getName());
    private final static String OPEN_WEATHER_API_KEY = "660955c02abd1ab4f655aced38437e7b";

    private final OpenWeatherRequest openWeatherRequest;
    private final DatabaseApiRequest databaseApiRequest;
    private ArrayList<CityApiKey> cityKeyList;

    public Requester(OpenWeatherRequest openWeatherRequest, DatabaseApiRequest databaseApiRequest) {
        this.openWeatherRequest = openWeatherRequest;
        this.databaseApiRequest = databaseApiRequest;
        this.cityKeyList = new ArrayList<>();

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

        while (true) {


            boolean wasCitiesLoaded = false;
            while (!wasCitiesLoaded) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    Response<List<CityApiKey>> response = databaseApiRequest.getFollowedCitiesIds().execute();
                    if (response.isSuccessful()) {
                        List<CityApiKey> cityApiKeys = response.body();
                        this.cityKeyList.clear();
                        if (cityApiKeys != null) {
                            this.cityKeyList.addAll(cityApiKeys);
                            LOGGER.log(Level.INFO, "Cities are loaded from server");
                            wasCitiesLoaded = true;
                        } else {
                            LOGGER.log(Level.INFO, "No cities were loaded");
                        }
                    } else {
                        LOGGER.log(Level.INFO, "Cannot load cities from server");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            boolean isDatabaseConnected = requestMoreCitiesWeather();

            while (isDatabaseConnected) {

                try {
                    Thread.sleep(20 * 60000);
                } catch (InterruptedException e) {
                    LOGGER.log(Level.INFO, "Requester thread interrupted");
                }

                isDatabaseConnected = requestMoreCitiesWeather();
            }

            LOGGER.log(Level.INFO, "Connection with server failed. Waiting for reconnect.");
        }
    }

    private boolean requestMoreCitiesWeather() {
        boolean wasSomethingSuccessful = false;
        ArrayList<String> cityIdList = new ArrayList<>();
        for (CityApiKey city : this.cityKeyList) {
            cityIdList.add(city.getCityID().toString());
        }

        // convert list of ids to one string in which ids are separated by comma
        String cityIds = cityIdList.toString()
                .replace(" ", "")
                .replace("[", "")
                .replace("]", "");
        LOGGER.log(Level.INFO, "City IDs are " + cityIds);

        try {
            Response<WeatherList> weatherApiResponse = this.openWeatherRequest.getCityGroup(cityIds).execute();


            if (weatherApiResponse.isSuccessful()) {
                WeatherList weatherList = weatherApiResponse.body();
                if (weatherList != null) {
                    List<CityWeather> cities = weatherList.getCityList();
                    LOGGER.log(Level.INFO, "Weather from some cities comes");

                    for (CityWeather city : cities) {
                        WeatherRecord record = new WeatherRecord(city.getId(),
                                city.getMain().getTemp(),
                                city.getMain().getPressure(),
                                city.getMain().getHumidity(),
                                new Timestamp(System.currentTimeMillis()));

                        String apiKey = null;
                        for (CityApiKey cityKey : Requester.this.cityKeyList) {
                            if (city.getId().equals(cityKey.getCityID())) {
                                apiKey = cityKey.getApiKey();
                            }
                        }


                        LOGGER.log(Level.INFO, "Temperature in " + city.getName() + " is " + city.getMain().getTemp() + "°c");

                        Response<Void> databaseResponse =
                                Requester.this.databaseApiRequest.storeWeatherRecord(record, apiKey).execute();

                        if (databaseResponse.isSuccessful()) {
                            LOGGER.log(Level.WARNING, "Data storing of " + city.getName() + " successful.");
                            wasSomethingSuccessful = true;
                        } else {
                            LOGGER.log(Level.WARNING, "Data storing of " + city.getName() + " failed.");
                        }

                    }

                } else {
                    LOGGER.log(Level.WARNING, "Not possible to parse response from API, because it is null");
                }
            } else {
                LOGGER.log(Level.INFO, "Request for cities'es weather failed.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return wasSomethingSuccessful;
    }

    private void requestOneCityWeather(String cityID) {
        Call<CityWeather> request = this.openWeatherRequest.getOneCity(cityID);

        request.enqueue(new Callback<CityWeather>() {
            @Override
            public void onResponse(Call<CityWeather> call, Response<CityWeather> response) {
                CityWeather cityWeather = response.body();
                if (cityWeather != null) {
                    double temperature = cityWeather.getMain().getTemp();

                    LOGGER.log(Level.INFO, "Temperature in " + cityWeather.getName() + " is " + temperature + "°c");
                }
            }

            @Override
            public void onFailure(Call<CityWeather> call, Throwable throwable) {
                LOGGER.log(Level.INFO, "Request for one city weather failed.");
            }
        });
    }

    public void setCityKeyList(ArrayList cityKeyList) {
        LOGGER.log(Level.INFO, "Followed cities changed. IDs: " + cityKeyList.toString());
        this.cityKeyList = cityKeyList;
    }
}
