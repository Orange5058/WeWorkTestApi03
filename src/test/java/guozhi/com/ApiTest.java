package guozhi.com;

import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class ApiTest {


    @Test
    void request(){
        RequestSpecification req =given().log().all();
        req.queryParam("id",1);
        req.queryParam("d","dddd");
        req.get("http://www.baiduu.com");

    }
}