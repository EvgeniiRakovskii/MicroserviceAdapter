package orq.example.microserviceAdapter;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import orq.example.microserviceAdapter.receiversTemp.ReceiverTemp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class Application {

    @Autowired
    private List<ReceiverTemp> receiverTemps;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletRegistrationBean camelServletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new CamelHttpTransportServlet(), "/*");
        registration.setName("CamelServlet");
        return registration;
    }

    //Хэш карта с поставщиками температуры и их именами
    @Bean("sourcesOfWeather")
    public Map<String, ReceiverTemp> getSourcesOfWeather() {
        Map<String, ReceiverTemp> sourcesOfWeather = new HashMap<>();

        for (ReceiverTemp receiverTemp : receiverTemps) {
            sourcesOfWeather.put(receiverTemp.getName(), receiverTemp);
        }
        return sourcesOfWeather;
    }

}