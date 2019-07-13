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

    @Test
    void getApiFromHar() {
        Api api =new Api();
        System.out.println(api.getApiFromHar("/api/app.har.json",".*tid=67.*").url);
        System.out.println(api.getApiFromHar("/api/app.har.json",".*tid=41.*").url);
        System.out.println(api.getApiFromHar("/api/app.har.json",".*tid=58.*").url);

    }
    @Test
    void matches(){
        String s="https://work.weixin.qq.com/api/devtools/devhandler.php?tid=41&access_token=v0gp6Hcj1kdTjx4nI7QGHVkVxP7uxOMcsLTcTuhEID3uvgeQnvVyJEzI1iahcFsIFu2Kq_aDpLBZwkElU0LlArRJoad3fBvy-GvTopuA9CRngsvJRBJGFS1hewOCjH8x0tekZ_7cRIYUhUdY5JomcqNfZeJfR2YwNlwj9p7CNv444RLLIRXRMi6lDjiC2gdFaiDMnuche_tOBIW7FDyh-A&agentid=3010011&f=json";
        System.out.println(s.matches(".*tid=41.*"));
    }

    @Test
    void getResponseFormHar() {
        Api api=new Api();
        api.getResponseFormHar("/api/app.har.json",".*tid=67.*",null);

    }
}