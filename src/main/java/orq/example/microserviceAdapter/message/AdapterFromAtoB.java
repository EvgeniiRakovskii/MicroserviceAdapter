package orq.example.microserviceAdapter.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import orq.example.microserviceAdapter.receiversTemp.ReceiverTemp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

//Реализация паттерна Адаптер для преобразования полученного сообщения из ServiceА в ServiceВ

@Component
@Scope("prototype")
public class AdapterFromAtoB extends MessageToB {

    //Платформа с которой получаем температуру, указываем в конфигурационном файле
    private static String sourceOfWeather;
    private static Map<String, ReceiverTemp> sourcesOfWeather;
    private MessageA messageA;

    public AdapterFromAtoB(MessageA messageA) {
        this.messageA = messageA;
    }

    @Autowired
    public void setWeatherMap(Map<String, ReceiverTemp> map) {
        sourcesOfWeather = map;
    }

    @Value("${weatherProvider}")
    public void setSourceOfWeather(String sourceOfWeather) {
        AdapterFromAtoB.sourceOfWeather = sourceOfWeather;
    }

    @Override
    public String getTxt() {
        return messageA.getMsg();
    }

    @Override
    public String getCreatedDt() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    }

    @Override
    public String getCurrentTemp() {

        String latitude = messageA.getCoordinates().get("latitude");
        String longitude = messageA.getCoordinates().get("longitude");
        return sourcesOfWeather.get(sourceOfWeather).getTemperature(latitude, longitude);
    }

}
