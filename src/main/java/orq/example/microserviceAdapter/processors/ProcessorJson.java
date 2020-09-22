package orq.example.microserviceAdapter.processors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import orq.example.microserviceAdapter.jsonserializers.StepSerializer;
import orq.example.microserviceAdapter.message.AdapterFromAtoB;
import orq.example.microserviceAdapter.message.MessageA;
import orq.example.microserviceAdapter.message.MessageToB;
import javax.annotation.PostConstruct;

// Обработчик Json для Camel route

@Component
public class ProcessorJson implements Processor {

    private Gson gson;

    @PostConstruct
    private void initMethod() {
        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(AdapterFromAtoB.class, new StepSerializer());
        this.gson = gsonBuilder.create();
    }

    @Override
    public void process(Exchange exchange) {

        //создаем POJO для полученного сообщение от Service A
        MessageA messageA = gson.fromJson(exchange.getIn().getBody(String.class), MessageA.class);

        //создаем POJO для отправки в Service B
        MessageToB messageToB = new AdapterFromAtoB(messageA);

        //Преобразуем POJO Message to Service B в Json и помещаем его в тело Exchange Сamel route
        String json = gson.toJson(messageToB);
        exchange.getIn().setBody(json);
    }
}
