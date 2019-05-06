package guozhi.com.contact;

import guozhi.com.Restful;
import guozhi.com.Wework;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

//继承restful
public class Contact extends Restful {
    String random=String.valueOf(System.currentTimeMillis());
    public Contact() {
            reset();
        }
        public void reset(){
            requestSpecification=given()
                    .log().all()
                    .queryParam("access_token", Wework.getContactToken())
                    .contentType(ContentType.JSON);
        }
}
