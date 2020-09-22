package orq.example.microserviceAdapter.receiversTemp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

// Получение текущей температуры с Climacell формат API

@Component
public class Climacell implements ReceiverTemp {

    private StringBuilder builderUrl;
    private OkHttpClient client;

    @PostConstruct
    private void initMethod() {
        client = new OkHttpClient();
        builderUrl = new StringBuilder();
        builderUrl.append("https://api.climacell.co/v3/weather/realtime?")
                .append("apikey=7GjPGnPPYcKlFnIgpFUHP7I3kyo2Agz9")
                .append("&unit_system=si")
                .append("&fields=temp");
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
            JSONObject jsonObject = jo.getJSONObject("temp");

            Double temp = jsonObject.getDouble("value");
            return temp.toString();


    }
}
