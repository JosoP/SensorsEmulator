package sk.uniza.fri.client;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import sk.uniza.fri.api.Saying;
import sk.uniza.fri.api.WeatherRecord;
import sk.uniza.fri.api.WeatherRecordList;

import java.util.Set;

public interface DatabaseApiRequest {
    @POST("/backend/weather/data/record")
    Call<String> storeWeatherRecord(@Body WeatherRecord record);

    @POST("/backend/weather/data/recordList")
    Call<Void> storeWeatherRecordList(@Body WeatherRecordList recordList);

    @GET("/backend/persons/cities")
    Call<Set<Long>> getFollowedCitiesIds();

    @GET("/backend/hello-world")
    Call<Saying> getHelloWorld();
}
