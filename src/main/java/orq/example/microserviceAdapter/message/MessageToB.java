package orq.example.microserviceAdapter.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import orq.example.microserviceAdapter.receiversTemp.ReceiverTemp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


//POJO сообщения для отправки в Service B

@Component
public class MessageToB {
    private String txt;
    private String createdDt;
    private String currentTemp;
    // Имя платформы с которой получаем температуру, указываем в конфигурационном файле
    private static String sourceOfWeather;
    private static Map<String, ReceiverTemp> sourcesOfWeather;

    public MessageToB(MessageA messageA) {
        System.out.println("Имя погоды : " + sourceOfWeather);
        System.out.println("Источники погоды: " + sourcesOfWeather);
        txt = messageA.getMsg();
        createdDt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        String latitude = messageA.getCoordinates().get("latitude");
        String longitude = messageA.getCoordinates().get("longitude");
        currentTemp = sourcesOfWeather.get(sourceOfWeather).getTemperature(latitude, longitude);
    }

    public MessageToB() {
    }

    @Autowired
    public void setWeatherMap(Map<String, ReceiverTemp> map) {
        sourcesOfWeather = map;
    }

    @Value("${weatherProvider}")
    public void setSourceOfWeather(String sourceOfWeather) {
        this.sourceOfWeather = sourceOfWeather;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(String createdDt) {
        this.createdDt = createdDt;
    }

    public String getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(String currentTemp) {
        this.currentTemp = currentTemp;
    }

    @Override
    public String toString() {
        return "MessageToB{" +
                "txt='" + txt + '\'' +
                ", createdDt='" + createdDt + '\'' +
                ", currentTemp='" + currentTemp + '\'' +
                '}';
    }
}
