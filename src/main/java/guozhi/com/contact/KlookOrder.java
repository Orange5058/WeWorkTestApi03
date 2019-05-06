package guozhi.com.contact;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;

public class KlookOrder extends Contact{

    public Response create(HashMap<String,Object> map){
        reset();
        DocumentContext documentContext= JsonPath.parse(this.getClass()
                .getResourceAsStream("/data/create.json"));//将一个文件作为字节流读进来
        map.entrySet().forEach(entry-> {
            documentContext.set(entry.getKey(), entry.getValue());
        });
        return requestSpecification
                .body(documentContext.jsonString())
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
                .then().log().all().extract().response();

    }
}
