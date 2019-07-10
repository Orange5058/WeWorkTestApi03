package guozhi.com.contact;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;


public class Department extends Contact {

    public Response list(String id) {
//        reset();
//        Response response=getDefaultRequestSpecification()
//                .param("id", id)
//                .when().get("https://qyapi.weixin.qq.com/cgi-bin/department/list")
//                .then().log().all().extract().response();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return templateFromYaml("/api/list.yaml", map);

    }

    //创建部门
//    public Response create(String name, String parentid){
////        reset();
//        String body=JsonPath.parse(this.getClass()
//                .getResourceAsStream("/data/create.json"))//将一个文件作为字节流读进来
//                .set("$.name", name)//写入操作，修改name
//                .set("parentid", parentid).jsonString();
//        return getDefaultRequestSpecification()
//                .body(body)
//                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
//                .then().log().all().extract().response();
//
//    }
    public Response create(String name, String paretid) {
        //让用例更清晰

        HashMap<String, Object> map = new HashMap<>();
        //给定一个文件，自动从文件里读取json文件，将文件中的所有结果计算完成，
        map.put("_file", "/data/create.json");
        //创建hashmap，先存放在模板中要修改的值
        map.put("name", name);
        map.put("parentid", paretid);
//        //读数据
//        String body=template("/data/create.json", hashMap);
//        //填充完值之后，先判断body是否有内容，如果有内容，立即填充，也就是将body填充为api的body
//        hashMap.put("_body",body);
        return templateFromYaml("/api/create.yaml", map);
    }

    //
    public Response create(HashMap<String, Object> map) {
//        DocumentContext documentContext=JsonPath.parse(this.getClass()
//                .getResourceAsStream("/data/create.json"));//将一个文件作为字节流读进来
//        map.entrySet().forEach(entry-> {
//            documentContext.set(entry.getKey(), entry.getValue());
//        });
//        return getDefaultRequestSpecification()
//                .body(documentContext.jsonString())
//                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
//                .then().log().all().extract().response();
        map.put("_file", "/data/create.json");
        return templateFromYaml("/api/create.yaml", map);

    }

    //删除部门
    public Response delete(String id) {
////        reset();
//        Response response=getDefaultRequestSpecification()
//                .queryParam("id",id)
//                .when().get("https://qyapi.weixin.qq.com/cgi-bin/department/delete")
//                .then().log().all().extract().response();
//        return response;
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return templateFromYaml("/api/delete.yaml", map);

    }

    //更新部门
    public Response update(String name, String id) {
////        reset();
//        String body = JsonPath.parse(this.getClass()
//                .getResourceAsStream("/data/update.json"))//将一个文件作为字节流读进来
//                .set("$.name", name)//写入操作，修改name
//                .set("parentid", id).jsonString();
//        Response response = getDefaultRequestSpecification()
//                .body(body)
//                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/update")
//                .then().log().all().extract().response();
//        return response;
//    }
        HashMap<String, Object> map = new HashMap<>();
        //给定一个文件，自动从文件里读取json文件，将文件中的所有结果计算完成，
        map.put("_file", "/data/update.json");
        //创建hashmap，先存放在模板中要修改的值
        map.put("name", name);
        map.put("id", id);
//        //读数据
//        String body=template("/data/create.json", hashMap);
//        //填充完值之后，先判断body是否有内容，如果有内容，立即填充，也就是将body填充为api的body
//        hashMap.put("_body",body);
        return templateFromYaml("/api/update.yaml", map);
    }

    //删除部门
    public Response deleteAll() {
//        reset();
        List<Integer> idList = list("").then().log().all().extract().path("department.id");
        idList.forEach(id -> delete(id.toString()));
        return null;
    }

    public Response updateAll(HashMap<String, Object> map) {
        //todo:
        return api("api.json", map);

    }

}
