
package sk.uniza.fri.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cnt",
    "cityList"
})
public class WeatherList {

    @JsonProperty("cnt")
    private Integer cnt;
    @JsonProperty("list")
    private List<CityWeather> cityList = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("cnt")
    public Integer getCnt() {
        return cnt;
    }

    @JsonProperty("cnt")
    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    @JsonProperty("list")
    public List<CityWeather> getCityList() {
        return cityList;
    }

    @JsonProperty("list")
    public void setCityList(java.util.List<CityWeather> cityList) {
        this.cityList = cityList;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
