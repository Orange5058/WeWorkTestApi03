package guozhi.com;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import guozhi.com.contact.Contact;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

//restful协议封装
public class Api {
    HashMap<String,Object> query = new HashMap<String, Object>();

    public RequestSpecification getDefaultRequestSpecification(){
        return given().log().all();
    }

    //创建一个模版类
    public static String template(String path,HashMap<String,Object> map){
        DocumentContext documentContext= JsonPath.parse(Api.class //解析出一个文件，形成上下文，然后通过mao进行修改对应的字段值
                .getResourceAsStream(path));
        map.entrySet().forEach(entry-> {
            documentContext.set(entry.getKey(), entry.getValue());
        });
        return documentContext.jsonString() ;
    }

    public Response templateFormHar(String path,String pattern, HashMap<String,Object>map){
        //todo:支持har文件读取接口定义并发送
        //从Har中读取请求，进行更新
        DocumentContext documentContext =JsonPath.parse(Api.class
        .getResourceAsStream(path));
        map.entrySet().forEach(entry->{
            documentContext.set(entry.getKey(),entry.getValue());
        });
        String method=documentContext.read("method");
        String url = documentContext.read("url");
        return getDefaultRequestSpecification().when().request(method, url);
    }

    public Response templateFromYaml(String path, HashMap<String,Object> map){
        //fixed:根据yaml生成接口定义并发送

        //读取yaml,如果要换成json，将YAMLFactory()替换JsonFactory()
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            //返回是一个类test environment
            Restful restful= mapper.readValue(WeworkConfig.class.getResourceAsStream(path),Restful.class);
            //如果是get方法将
            if(restful.method.toLowerCase().contains("get")){
                //循环
                map.entrySet().forEach(entry ->{
                    //追加某个值，也就是替换
                    restful.query.replace(entry.getKey(), entry.getValue().toString());
                    System.out.println(restful.query);
                });
            }
            RequestSpecification requestSpecification = getDefaultRequestSpecification();
            restful.query.entrySet().forEach(entry->{
                requestSpecification.queryParam(entry.getKey(),entry.getValue());

            });
            return requestSpecification.log().all().request(restful.method, restful.url)
                    .then().extract().response();
            //写yaml文件
//            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(WeworkConfig.getInstance()));
//            System.out.println(mapper.writeValueAsString(WeworkConfig.getInstance()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    //todo:支持wsdl soap

    public Response api(String Path,HashMap<String,Object> map){
        //todo:动态调用
        return null;
    }

}
