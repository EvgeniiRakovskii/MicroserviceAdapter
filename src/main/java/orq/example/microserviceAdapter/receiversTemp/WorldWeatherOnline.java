package orq.example.microserviceAdapter.receiversTemp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.IOException;

// Получение текущей температуры с WorldWeatherOnline формат API

@Component
public class WorldWeatherOnline implements ReceiverTemp {

    private StringBuilder builderUrl;
    private OkHttpClient client;

    @PostConstruct
    private void initMethod() {
        builderUrl = new StringBuilder();
        builderUrl.append("http://api.worldweatheronline.com/premium/v1/weather.ashx?")
                .append("key=657c3aabefd447daa1b183120201209&")
                .append("&format=json")
                .append("&num_of_days=1&fx=no&mca=no");
        client = new OkHttpClient();

    }


    public WorldWeatherOnline() {
    }

    @Override
    public String getTemperature(String lat, String lon) throws IOException {
        builderUrl
                .append("&lat=").append(lat)
                .append("&lon=").append(lon);


        Request request = new Request.Builder()
                .url(builderUrl.toString())
                .get()
                .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            System.out.println("Json from WeatherApi is " + responseBody);
            JSONObject jo = new JSONObject(responseBody);
            JSONArray service = jo.getJSONObject("data").getJSONArray("current_condition");
            JSONObject jsonObject = service.getJSONObject(0);
            String temp = jsonObject.getString("temp_C");

            return temp;

    }
}
