package componentTest;

import org.apache.camel.*;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.apache.camel.test.spring.junit5.MockEndpointsAndSkip;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import orq.example.microserviceAdapter.Application;


@CamelSpringTest
@TestPropertySource(locations = "classpath:application.properties")
@MockEndpointsAndSkip("{{addressOfB}}") // Запрещаем отправку сообщений на Service B во время теста
@ContextConfiguration(classes = Application.class)
public class CamelSpringAdapterTest {

    //Моковая конечная точка Service B
    @EndpointInject(value = "mock://{{addressOfB}}")
    protected MockEndpoint endpoint;

    //Промежуточная точка в нашем route
    @Produce("direct:modify")
    protected ProducerTemplate testProducer;

    /*
    Тестирование правильного формата входящего сообщения.
    Отправляем сообщение и ждем его получения моковой точкой.
    Проверяем что оно прошло все проверки и корректно преобразовалась.
     */
    @Test
    public void testRightMessage() throws InterruptedException {
        endpoint.expectedMessageCount(1);
        testProducer.requestBody("{\n" + "\"msg\": \"Hello\",\n" +
                "\"lng\": \"ru\",\n" +
                "\"coordinates\": {\n" +
                "\"latitude\": \"54.35\",\n" +
                "\"longitude\": \"52.52\"\n" +
                "}\n" + "}");
        endpoint.assertIsSatisfied();
    }

    //Тестирование пустого входящего сообщения
    //Отправляем сообщение и сравниваем результат с ответом от сервера
    @Test
    public void testEmptyMessage() {

        String wrongMessage = testProducer.requestBody((Object) ("{\n" + "\"msg\": \"\",\n" +
                "\"lng\": \"ru\"\n" + "}"), String.class);
        Assert.assertEquals(wrongMessage, "Message is empty");

    }

    //Тестирование входящего сообщения с неккоректным языковым признаком
    //Отправляем сообщение и сравниваем с ответом от сервера
    @Test
    public void testLanguage() {
        String wrongLanguage = testProducer.requestBody((Object) ("{\n" + "\"msg\": \"Bye bye\",\n" +
                "\"lng\": \"ruu\"\n" + "}"), String.class);
        Assert.assertEquals(wrongLanguage, "");

    }

    //Тестирование входящего сообщения с неправильными параметрами
    //Отправляем сообщение и сравниваем с ответом от сервера
    @Test
    public void testWrongBody() {
        String wrongLanguage = testProducer.requestBody((Object) ("{\n" + "\"msg\": \"Bye bye\",\n" +
                "\"lng\": \"ru\"\n" + "}"), String.class);
        Assert.assertEquals(wrongLanguage, "Something was wrong");

    }


}
