package sk.uniza.fri;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class SensorsEmulatorConfiguration extends Configuration {

    @NotEmpty
    private String weatherApiURL;
    @NotEmpty
    private String databaseApiURL;

    @JsonProperty("weatherApiURL")
    public String getWeatherApiURL() {
        return weatherApiURL;
    }
    @JsonProperty("weatherApiURL")
    public void setWeatherApiURL(String weatherApiURL) {
        this.weatherApiURL = weatherApiURL;
    }

    @JsonProperty("databaseApiURL")
    public String getDatabaseApiURL() {
        return databaseApiURL;
    }
    @JsonProperty("databaseApiURL")
    public void setDatabaseApiURL(String databaseApiURL) {
        this.databaseApiURL = databaseApiURL;
    }
}
