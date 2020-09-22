package orq.example.microserviceAdapter.receiversTemp;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


// Получение текущей температуры с Climacell формат API

@Component
public class Climacell extends AbstractWeatherProvider {

    @PostConstruct
    @Override
    protected void initMethod() {
        name = "climacell";
        builderUrl = new StringBuilder()
                .append("https://api.climacell.co/v3/weather/realtime?")
                .append("apikey=7GjPGnPPYcKlFnIgpFUHP7I3kyo2Agz9")
                .append("&unit_system=si")
                .append("&fields=temp");
    }

    @Override
    protected String parseJsonForTemp(String jsonFormat) {
        JSONObject jo = new JSONObject(jsonFormat);
        JSONObject jsonObject = jo.getJSONObject("temp");
        Double temp = jsonObject.getDouble("value");
        return temp.toString();
    }

}
