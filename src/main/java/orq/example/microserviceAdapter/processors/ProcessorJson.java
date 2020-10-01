package orq.example.microserviceAdapter.processors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import orq.example.microserviceAdapter.message.MessageA;
import orq.example.microserviceAdapter.message.MessageToB;

import javax.annotation.PostConstruct;

/*
Обработчик Json для Camel route.
Принимает входящее сообщение, обрабатывает его, обогащает данными и передает дальше.

Логика работы:
реализовал POJO входного и выходного сообщения, внедрил в MessageToB ссылку на MessageA,
и в конструкторе задал значения переменным
*/
@Component
public class ProcessorJson implements Processor {

    private Gson gson;
    private ApplicationContext context;

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    private void initMethod() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        this.gson = gsonBuilder.create();
    }

    @Override
    public void process(Exchange exchange) {

        //создаем POJO для полученного сообщение от Service A
        MessageA messageA = gson.fromJson(exchange.getIn().getBody(String.class), MessageA.class);

        //создаем POJO сообщения для отправки в ServiceB
        MessageToB messageToB = new MessageToB(messageA);

        //Преобразуем POJO MessageToB в Json и помещаем его в тело Exchange Сamel route
        String json = gson.toJson(messageToB);

        exchange.getIn().setBody(json);
    }
}
