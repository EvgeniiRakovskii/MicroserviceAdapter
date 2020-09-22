package orq.example.microserviceAdapter.message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import orq.example.microserviceAdapter.receiversTemp.ReceiverTemp;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//Реализовал паттерн адаптер для преобразования сообщения из Service А в  Service В

@Component
public class AdapterFromAtoB extends MessageToB {
    private static ReceiverTemp receiverTemp;

    private MessageA messageA;

    public AdapterFromAtoB(MessageA messageA) {
        this.messageA = messageA;
    }

    @Autowired
    public void setReceiverTemp(@Qualifier("climacell") ReceiverTemp receiverTemp) {
        this.receiverTemp = receiverTemp;
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
    public String getCurrentTemp() throws IOException {
        String latitude = messageA.getCoordinates().get("latitude");
        String longitude = messageA.getCoordinates().get("longitude");
        return receiverTemp.getTemperature(latitude, longitude);
    }

}
