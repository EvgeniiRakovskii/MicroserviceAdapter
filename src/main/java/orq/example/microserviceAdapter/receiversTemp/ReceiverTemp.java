package orq.example.microserviceAdapter.receiversTemp;

import org.springframework.stereotype.Component;

// Базовый интерфейс для поставщиков температуры

@Component
public interface ReceiverTemp {

    String getTemperature(String lat, String lon);

    String getName();
}
