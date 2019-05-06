package guozhi.com.contact;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    static Member member;
    @BeforeAll
    static void setUp() {
        member=new Member();
    }

    @ParameterizedTest
    //数据驱动
    @ValueSource(strings = {"guozhi_", "hogwarts_", "testerhome"})
    void create(String name) {
        HashMap<String,Object> map =new HashMap<>();
        //局部封装重复的数据
        String nameNew =name+member.random;
        //封装随机数
        String random=String.valueOf(System.currentTimeMillis()).substring(5+0,5+8);
        map.put("userid",nameNew+member.random);
        map.put("name",nameNew+member.random);
        map.put("mobile","137" + random);
        map.put("email",random + "@qq.com");
        map.put("department", Arrays.asList(1,2));
        member.create (map).then().statusCode(200).body("errcode",equalTo(0));

    }

    @ParameterizedTest
    //数据驱动
    @CsvFileSource(resources = "/data/members.csv")
    void create(String name,String alias) {
        HashMap<String,Object> map =new HashMap<>();
        //局部封装重复的数据
        String nameNew =name+member.random;
        //封装随机数
        String random=String.valueOf(System.currentTimeMillis()).substring(5+0,5+8);
        map.put("userid",nameNew);
        map.put("name",nameNew);
        map.put("alias",alias);
        map.put("mobile","137" + random);
        map.put("email",random + "@qq.com");
        map.put("department", Arrays.asList(1,2));
        member.create (map).then().statusCode(200).body("errcode",equalTo(0));

    }
}