package guozhi.com;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarRequest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;

//restful协议封装
public class Api {
    HashMap<String, Object> query = new HashMap<String, Object>();

    public Api(){
        useRelaxedHTTPSValidation();
    }

    public RequestSpecification getDefaultRequestSpecification() {
        return given().log().all();
    }

    //创建一个模版类
    public static String template(String path, HashMap<String, Object> map) {
        DocumentContext documentContext = JsonPath.parse(Api.class //解析出一个文件，形成上下文，然后通过mao进行修改对应的字段值
                .getResourceAsStream(path));
        map.entrySet().forEach(entry -> {
            documentContext.set(entry.getKey(), entry.getValue());
        });
        return documentContext.jsonString();
    }


    public Response getResponseFormHar(String path, String pattern, HashMap<String, Object> map) {
        //todo:支持har文件读取接口定义并发送
        //从Har中读取请求，进行更新
//        DocumentContext documentContext = JsonPath.parse(Api.class
//                .getResourceAsStream(path));
//        map.entrySet().forEach(entry -> {
//            documentContext.set(entry.getKey(), entry.getValue());
//        });
//        String method = documentContext.read("method");
//        String url = documentContext.read("url");
//        return getDefaultRequestSpecification().when().request(method, url);
        Restful restful=getApiFromHar(path,pattern);
        restful=updateApiFromMap(restful,map);
        return getResponseFromRestful(restful);

    }

    public Response templateFromSwagger(String path, String pattern, HashMap<String, Object> map) {
        //todo: 支持从swagger自动生成接口定义并发送
        //todo: 分析swagger codegen
        //从har中读取请求，进行更新
        DocumentContext documentContext = JsonPath.parse(Api.class
                .getResourceAsStream(path));
        map.entrySet().forEach(entry -> {
            documentContext.set(entry.getKey(), entry.getValue());
        });

        String method = documentContext.read("method");
        String url = documentContext.read("url");
        return getDefaultRequestSpecification().when().request(method, url);
    }

    public  Restful getApiFromHar(String path,String pattern){
        HarReader harReader = new HarReader();
        try {
            Har har = harReader.readFromFile(new File(
                    URLDecoder.decode(
                    getClass().getResource(path).getPath(),"utf-8")));
            HarRequest request = new HarRequest();
            Boolean match=false;
            for(HarEntry entry:har.getLog().getEntries()){
                request = entry.getRequest();
                if(request.getUrl().matches(pattern)){
                    match=true;
                    break;
                }
            }
            if(match==false){
                request=null;
                throw new Exception("ddddd");

            }
            Restful restful = new Restful();
            restful.method=request.getMethod().name().toLowerCase();
            //todo:去掉url中的query部分
            restful.url = request.getUrl();
            //getQueryString是个list
            request.getQueryString().forEach(q->{
                restful.query.put(q.getName(),q.getValue());
            });
            restful.body=request.getPostData().getText();
            return restful;
        } catch (HarReaderException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    public Restful getApiFromYaml(String path){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(WeworkConfig.class.getResourceAsStream(path),Restful.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public  Restful updateApiFromMap(Restful restful, HashMap<String, Object> map){
        if(map==null){
            return restful;
        }
        if (restful.method.toLowerCase().contains("get")) {
            //循环
            map.entrySet().forEach(entry -> {
                //追加某个值，也就是替换
                restful.query.replace(entry.getKey(), entry.getValue().toString());
                System.out.println(restful.query);
            });
        }

        if (restful.method.toLowerCase().contains("post")) {
            //从map进行读取字段
            if (map.containsKey("_body")) {
                restful.body = map.get("_body").toString();
            }
            if (map.containsKey("_file")) {
                String filepath = map.get("_file").toString();
                map.remove("_file");
                restful.body = template(filepath, map);
            }
        }
        return restful;
    }
    public  Response getResponseFromRestful(Restful restful){
        RequestSpecification requestSpecification =getDefaultRequestSpecification();
        if (restful.query != null){
            restful.query.entrySet().forEach(entry ->{
                requestSpecification.queryParam(entry.getKey(),entry.getValue());
            });
        }
        if (restful.body != null) {
            requestSpecification.body(restful.body);
        }
        return requestSpecification.log().all()
                .when().request(restful.method, restful.url)
                .then().log().all().extract().response();
    }

    public Response getResponseFromYaml(String path, HashMap<String, Object> map) {
        //fixed:根据yaml生成接口定义并发送

            Restful restful = getApiFromYaml(path);
            restful= updateApiFromMap(restful, map);
            RequestSpecification requestSpecification = getDefaultRequestSpecification();
            if (restful.query != null) {
                restful.query.entrySet().forEach(entry -> {
                    requestSpecification.queryParam(entry.getKey(), entry.getValue());

                });
            }
            if (restful.body != null) {
                requestSpecification.body(restful.body);
            }
        String[] url=updateUrl(restful.url);
//         System.out.println("============="+url);

        return requestSpecification.log().all()
                .header("Host", url[0])
                .when().request(restful.method, url[1])
//                .when().request(restful.method, restful.url)
                .then().log().all()
                .extract().response();

    }
    private String[] updateUrl(String url) {
        //fixed: 多环境支持，替换url，更新host的header
        HashMap<String, String> hosts=WeworkConfig.getInstance().env.get(WeworkConfig.getInstance().current);
        String host="";
        String urlNew="";
        for(Map.Entry<String, String> entry : hosts.entrySet()){
            if(url.contains(entry.getKey())){
                host=entry.getKey();
                urlNew=url.replace(entry.getKey(), entry.getValue());
            }
        }
        return new String[]{host, urlNew};
    }

    //todo:支持wsdl soap
    public Response api(String Path, HashMap<String, Object> map) {
        //todo:动态调用
        return null;
    }

}
