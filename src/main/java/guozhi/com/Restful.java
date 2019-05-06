package guozhi.com;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

//restful协议封装
public class Restful {
    HashMap<String,Object> query = new HashMap<String, Object>();
    public RequestSpecification requestSpecification=given();
    public Response send(){
        //根据系统已有的东西
        requestSpecification=given().log().all();
        query.entrySet().forEach(entry-> {
            requestSpecification.queryParam(entry.getKey(),entry.getValue());
        });
        return requestSpecification.when().request("get","baidu.com");
    }

    //创建一个模版类
    public static String template(String path,HashMap<String,Object> map){
        DocumentContext documentContext= JsonPath.parse(Restful.class //解析出一个文件，形成上下文，然后通过mao进行修改对应的字段值
                .getResourceAsStream(path));
        map.entrySet().forEach(entry-> {
            documentContext.set(entry.getKey(), entry.getValue());
        });
        return documentContext.jsonString() ;
    }
    public  Response templateFromYaml(String path, HashMap<String,Object> map){
        //todo:根据yaml生成接口定义并发送
        return null;
    }
    //todo:支持wsdl soap

    public Response api(String Path,HashMap<String,Object> map){
        //todo:动态调用
        return null;
    }

}
