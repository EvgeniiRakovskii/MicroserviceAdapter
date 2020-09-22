package orq.example.microserviceAdapter.processors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import orq.example.microserviceAdapter.jsonserializers.StepSerializer;
import orq.example.microserviceAdapter.message.AdapterFromAtoB;
import orq.example.microserviceAdapter.message.MessageA;
import orq.example.microserviceAdapter.message.MessageToB;

import javax.annotation.PostConstruct;

/*
Обработчик Json для Camel route.
Принимает входящее сообщение, обрабатывает его, обогащает данными и передает дальше.

Логика работы:
реализовал POJO(MessageA, MessageToB), унаследовал AdapterFromAtoB от MessageB (реализовал паттерн адаптер),
внедрил в него ссылку на MessageA, переопределил в нем геттеры и изменил их,добавив туда данные с MessageA, текущим временем и температурой.
Так же пришлось самостоятельно реализовать Serializer для корректного преобразования POJO AdapterFromAtoB в json.
*/
@Component
public class ProcessorJson implements Processor {

    private Gson gson;
    private StepSerializer stepSerializer;

    @Autowired
    public void setStepSerializer(StepSerializer stepSerializer) {
        this.stepSerializer = stepSerializer;
    }

    @PostConstruct
    private void initMethod() {
        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(AdapterFromAtoB.class, stepSerializer);
        this.gson = gsonBuilder.create();
    }

    @Override
    public void process(Exchange exchange) {

        //создаем POJO для полученного сообщение от Service A
        MessageA messageA = gson.fromJson(exchange.getIn().getBody(String.class), MessageA.class);

        //создаем POJO сообщения для отправки в ServiceB
        MessageToB messageToB = new AdapterFromAtoB(messageA);

        //Преобразуем POJO Message to Service B в Json и помещаем его в тело Exchange Сamel route
        String json = gson.toJson(messageToB);

        exchange.getIn().setBody(json);
    }
}
