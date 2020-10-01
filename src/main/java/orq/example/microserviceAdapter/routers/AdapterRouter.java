package orq.example.microserviceAdapter.routers;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import orq.example.microserviceAdapter.errors.MessageErrorService;
import orq.example.microserviceAdapter.processors.ProcessorHeaders;
import orq.example.microserviceAdapter.processors.ProcessorJson;

// Camel route for adapter

@Component
@Scope("prototype")
public class AdapterRouter extends RouteBuilder {

    // Обработка входного сообщения для Camel route
    private ProcessorJson processorJson;
    // Обработка ошибки для пустого сообщения
    private MessageErrorService messageErrorService;
    // Настройка Headers
    private ProcessorHeaders processorHeaders;

    @Autowired
    public void setProcessorHeaders(ProcessorHeaders processorHeaders) {
        this.processorHeaders = processorHeaders;
    }

    @Autowired
    void setMessageErrorService(MessageErrorService messageErrorService) {
        this.messageErrorService = messageErrorService;
    }

    @Autowired
    public void setProcessorJson(ProcessorJson processorJson) {
        this.processorJson = processorJson;
    }

    @Override
    public void configure() {
        restConfiguration()
                .port("{{portAdapter}}")
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        //Отлавливаю все исключения и оповещаю о них в ответе
        onException(Exception.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .log(LoggingLevel.INFO, "Exception is ${exception.message}!")
                .setBody().constant("Something was wrong");


        //Начало маршрута, принимаем POST запросы по указанному адресу в properties
        rest().post("{{addressAdapter}}").consumes("application/json")
                .to("direct:modify");


        from("direct:modify")

                //Условный оператор
                .choice()
                //фильтрация сообщения с признаком lng отличным от ru
                .when().jsonpath("$.[?(@.lng != 'ru')]").setBody(constant(""))
                //обработка ошибки пустого сообщения
                .when().jsonpath("$.[?(@.msg == '')]").bean(messageErrorService, "messageIsEmpty")
                .otherwise()
                .log(LoggingLevel.INFO, "The input body after filtration is ${body}!")
                //обработка входного сообщения
                .process(processorJson)
                //настройка Headers
                .process(processorHeaders)
                .log(LoggingLevel.INFO, "The output body is ${body}!")
                .to("{{addressOfB}}")
                .transform().simple("");
    }
}

