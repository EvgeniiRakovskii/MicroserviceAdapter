package orq.example.microserviceAdapter.receiversTemp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/*
Абстрактный класс для поставщиков погоды
Для добавления нового поставщика необходимо:
Создать класс, унаследовать его от AbstractWeatherProvider,
переопределить метод initMethod, пометить его аннтоацией @PostConstruct, присвоить в нем значения полям name, url
переопределить и реализовать метод parseJsonForTemp, который извлекает температуру из ответа с сервера
 */
public abstract class AbstractWeatherProvider implements ReceiverTemp {

    //Этим полям присваиваются значения из классов наследников
    protected StringBuilder builderUrl;
    protected String name;
    protected OkHttpClient client = new OkHttpClient();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTemperature(String lat, String lon) {
        //В случае возникновения исключения отправляем null , чтобы Camel route обработал ошибку
        try {
            String responseBody = sentRequest(lat, lon);
            return parseJsonForTemp(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
           return null;
        }
    }

    //Отправляем запрос на сервер с координатами
    protected String sentRequest(String lat, String lon) throws Exception {
        builderUrl
                .append("&lat=").append(lat)
                .append("&lon=").append(lon);

        Request request = new Request.Builder()
                .url(builderUrl.toString())
                .get()
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    protected abstract String parseJsonForTemp(String jsonFormat);

    protected abstract void initMethod();

}
