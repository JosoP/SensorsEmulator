package sk.uniza.fri;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import retrofit2.Retrofit;
import sk.uniza.fri.client.OpenWeatherRequest;


import retrofit2.converter.jackson.JacksonConverterFactory;

public class SensorsEmulatorApplication extends Application<SensorsEmulatorConfiguration> {
    private Requester requester = null;

    public static void main(final String[] args) throws Exception {
        new SensorsEmulatorApplication().run(args);
    }

    @Override
    public String getName() {
        return "SensorsEmulator";
    }

    @Override
    public void initialize(final Bootstrap<SensorsEmulatorConfiguration> bootstrap) {
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http:\\localhost:8085")
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        OpenWeatherRequest openWeatherRequest = retrofit.create(OpenWeatherRequest.class);

        requester = new Requester(openWeatherRequest);
    }

    @Override
    public void run(final SensorsEmulatorConfiguration configuration,
                    final Environment environment) {


        requester.start();
    }

}
