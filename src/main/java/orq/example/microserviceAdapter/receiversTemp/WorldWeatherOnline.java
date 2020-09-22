package orq.example.microserviceAdapter.receiversTemp;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

// Получение текущей температуры с WorldWeatherOnline формат API

@Component
public class WorldWeatherOnline extends AbstractWeatherProvider{

    @PostConstruct
    @Override
    protected void initMethod() {
        name = "worldWeatherOnline";
        builderUrl = new StringBuilder()
                .append("http://api.worldweatheronline.com/premium/v1/weather.ashx?")
                .append("key=657c3aabefd447daa1b183120201209&")
                .append("&format=json")
                .append("&num_of_days=1&fx=no&mca=no");
    }

    @Override
    protected String parseJsonForTemp(String jsonFormat) {

        JSONObject jo = new JSONObject(jsonFormat);
        JSONArray service = jo.getJSONObject("data").getJSONArray("current_condition");
        JSONObject jsonObject = service.getJSONObject(0);
        String temp = jsonObject.getString("temp_C");
        Double checkDouble = Double.parseDouble(temp);

        return checkDouble.toString();
    }
}
