package sk.uniza.fri;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import retrofit2.Retrofit;
import sk.uniza.fri.client.DatabaseApiRequest;
import sk.uniza.fri.client.OpenWeatherRequest;


import retrofit2.converter.jackson.JacksonConverterFactory;
import sk.uniza.fri.health.ApiHealthCheck;
import sk.uniza.fri.resources.SettingsResource;

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

        // Swagger documentation available on http://localhost:<your_port>/swagger
        bootstrap.addBundle(new SwaggerBundle<SensorsEmulatorConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(SensorsEmulatorConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
    }

    @Override
    public void run(final SensorsEmulatorConfiguration configuration,
                    final Environment environment) {
        final ApiHealthCheck dbApiHealthCheck = new ApiHealthCheck(configuration.getDatabaseApiURL());
        final ApiHealthCheck weatherApiHealthCheck = new ApiHealthCheck(configuration.getWeatherApiURL());


        environment.healthChecks().register("Database API health check", dbApiHealthCheck);
        environment.healthChecks().register("Weather API health check", weatherApiHealthCheck);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(configuration.getWeatherApiURL())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        OpenWeatherRequest openWeatherRequest = retrofit.create(OpenWeatherRequest.class);

        retrofit = new Retrofit.Builder()
                .baseUrl(configuration.getDatabaseApiURL())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        DatabaseApiRequest databaseApiRequest = retrofit.create(DatabaseApiRequest.class);

        requester = new Requester(openWeatherRequest, databaseApiRequest);
        requester.start();

        final SettingsResource settingsResource = new SettingsResource(requester);


        environment.jersey().register(settingsResource);
    }

}
