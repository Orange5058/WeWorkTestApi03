package guozhi.com.contact;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;

class DepartmentTest {
    Department department;
    String random=String.valueOf(System.currentTimeMillis());

    @BeforeEach
    void setUp() {
        if (department == null) {
            department = new Department();
            department.deleteAll();
        }
    }
    @Test
    void list() {
        //then进行断言，也可以对body进行断言
//        department.list("").then().statusCode(200).body("department.name[0]",equalTo("testhome1234"));
//        department.list("33").then().statusCode(200).body("department.name[0]",equalTo("testhome1234"));
        department.list("1").then().statusCode(200);
//                .body("department[0].name",equalTo("testhome1234"))
//                .body("department[0].id",equalTo(33));

    }

    @Test
    void create() {
        department.create("test0020709", "1").then().body("errcode", equalTo(0));
    }
    @Test
    void createByMap(){
        HashMap<String, Object> map =new HashMap<String ,Object>(){{
            put("name", "guozhi_map"+random);
            put("parentid","1");
        }

        };
//        department.create(map).then().body("errcode",equalTo(0));
    }


    @Test
    void createWithChinese() {
         department.create("果汁"+random,"1").then().body("errcode",equalTo(0));

    }
    @ParameterizedTest
    @CsvFileSource(resources = "/data/createWithDup.csv")
    void createWithDup(String name,Integer expectCode){
        department.create(name+random,"1").then().body("errcode",equalTo(0));
        department.create(name+random,"1").then().body("errcode",equalTo(expectCode));
    }

    @Test
    //为了减少依赖，每个用例都自己创建数据
    void delete() {
        String nameOld="test"+random;
        department.create(nameOld,"1");
        Integer idInt=department.list("").path("department.find{it.name=='"+ nameOld +"'}.id");
        String id =String.valueOf(idInt);
        department.delete(id).then().body("errcode", equalTo(0));
    }

    @Test
    void update() {
        String nameOld="test"+random;
        department.create(nameOld,"3");
        Integer idInt=department.list("").path("department.find{it.name=='"+ nameOld +"'}.id");
        String id =String.valueOf(idInt);
        department.update("test003_xiugai"+random,id).then().body("errcode", equalTo(0));
    }

}