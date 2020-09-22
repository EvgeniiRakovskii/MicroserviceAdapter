package orq.example.microserviceAdapter.message;

import org.springframework.stereotype.Component;

import java.util.Map;

//POJO полученного сообщения из Service A

@Component
public class MessageA {
    String msg;
    String lng;
    Map<String, String> coordinates;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Map<String, String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Map<String, String> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "MessageA{" +
                "msg='" + msg + '\'' +
                ", lng='" + lng + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}
