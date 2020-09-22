package orq.example.microserviceAdapter.receiversTemp;

import org.springframework.stereotype.Service;

import java.io.IOException;

// Базовый интерфейс для поставщиков температуры

@Service
public interface ReceiverTemp {
    String getTemperature(String lat, String lon) throws IOException;
}
