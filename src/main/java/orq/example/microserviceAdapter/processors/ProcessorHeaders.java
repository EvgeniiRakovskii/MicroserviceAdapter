package orq.example.microserviceAdapter.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import static org.apache.camel.language.constant.ConstantLanguage.constant;


@Component
public class ProcessorHeaders implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getIn();
        message.removeHeader(Exchange.HTTP_URI);
        message.setHeader(Exchange.HTTP_METHOD, constant("POST"));
        message.setHeader("Content-Type", constant("application/json"));
    }
}
