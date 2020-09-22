package orq.example.microserviceAdapter.errors;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

/*
Обработчик ошибки при пустом сообщении
 */
@Component
public class MessageErrorService {
    public void messageIsEmpty(Exchange exchange) {
        exchange.getIn().setBody("Message is empty");
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "text/plain");
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
    }
}
