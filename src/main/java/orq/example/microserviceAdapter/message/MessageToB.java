package orq.example.microserviceAdapter.message;
import org.springframework.stereotype.Component;

import java.io.IOException;

//POJO сообщения для отправки в Service B

@Component
public class MessageToB {
    protected String txt;
    protected String createdDt;
    protected String currentTemp;

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(String createdDt) {
        this.createdDt = createdDt;
    }

    public String getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(String currentTemp) {
        this.currentTemp = currentTemp;
    }
}
