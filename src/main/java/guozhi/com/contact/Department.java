package guozhi.com.contact;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;


public class Department extends Contact{

    public Response list(String id){
//        reset();
//        Response response=getDefaultRequestSpecification()
//                .param("id", id)
//                .when().get("https://qyapi.weixin.qq.com/cgi-bin/department/list")
//                .then().log().all().extract().response();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return templateFromYaml("/api/list.yaml",map);

    }
    //创建部门
    public Response create(String name, String parentid){
//        reset();
        String body=JsonPath.parse(this.getClass()
                .getResourceAsStream("/data/create.json"))//将一个文件作为字节流读进来
                .set("$.name", name)//写入操作，修改name
                .set("parentid", parentid).jsonString();
        return getDefaultRequestSpecification()
                .body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
                .then().log().all().extract().response();

    }

    public Response create(HashMap<String,Object> map){
//        reset();
        DocumentContext documentContext=JsonPath.parse(this.getClass()
                .getResourceAsStream("/data/create.json"));//将一个文件作为字节流读进来
        map.entrySet().forEach(entry-> {
            documentContext.set(entry.getKey(), entry.getValue());
        });
        return getDefaultRequestSpecification()
                .body(documentContext.jsonString())
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
                .then().log().all().extract().response();

    }
    //删除部门
    public Response delete(String id){
//        reset();
        Response response=getDefaultRequestSpecification()
                .queryParam("id",id)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/department/delete")
                .then().log().all().extract().response();
        return response;

    }
    //更新部门
    public Response update(String name,String id){
//        reset();
        String body=JsonPath.parse(this.getClass()
                .getResourceAsStream("/data/update.json"))//将一个文件作为字节流读进来
                .set("$.name", name)//写入操作，修改name
                .set("parentid", id).jsonString();
        Response response=getDefaultRequestSpecification()
                .body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/update")
                .then().log().all().extract().response();
        return response;
    }
    //删除部门
    public Response deleteAll(){
//        reset();
        List<Integer> idList=list("").then().log().all().extract().path("department.id");
        idList.forEach(id->delete(id.toString()));
        return null;
    }

    public Response updateAll(HashMap<String,Object> map){
        return api("api.json", map);

    }

}
