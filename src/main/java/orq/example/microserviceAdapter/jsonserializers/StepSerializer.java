package orq.example.microserviceAdapter.jsonserializers;

import com.google.gson.*;
import org.springframework.stereotype.Component;
import orq.example.microserviceAdapter.message.MessageToB;

import java.lang.reflect.Type;

//Настройка сериализации для корректного преобразования POJO AdapterFromAtoB в json

@Component
public class StepSerializer implements JsonSerializer<MessageToB> {
    public JsonElement serialize(MessageToB src,
                                 Type typeOfSrc,
                                 JsonSerializationContext context) {
        Gson gson = new Gson();
        JsonObject step = new JsonObject();
        step.add("txt", gson.toJsonTree(src.getTxt()));
        step.add("currentTemp", gson.toJsonTree(src.getCurrentTemp()));
        step.add("createDt", gson.toJsonTree(src.getCreatedDt()));
        return step;
    }
}