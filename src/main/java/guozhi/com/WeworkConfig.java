package guozhi.com;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.HashMap;

public class WeworkConfig {

/*    public String agentId="1000005";
    public String secret="1JPyY9GvPLZfpvxEDjok-Xt_9v7HIBYJhZUoO6EgNGY";
    public String corpid = "wwd6da61649bd66fea";
    public String contactSecret="C7uGOrNyxWWzwBsUyWEbLQdOqoWPz4hNvxj9RIFv-4U";
*/
//
    public String agentId="3010011";
    public String secret="6mBGd2dBCkTiNDRfb--S0zLTs7gXhpHgRl76L-7RrJ4";
    public String corpid = "ww76731601e6910ff6";
    public String contactSecret="b45NOjFUNr90sVcpZQ26qtOEB8LVlmrl2AFxLxrH-mc";
    public String current="test";
    public HashMap<String, HashMap<String, String>> env;

    private static WeworkConfig weworkConfig;
    public static WeworkConfig getInstance(){
        if(weworkConfig==null){
            weworkConfig=load("/conf/WeworkConfig.yaml");
//            System.out.println(weworkConfig);
//            System.out.println(weworkConfig.agentId);
            weworkConfig=new WeworkConfig();
        }
        return weworkConfig;
    }

    public static WeworkConfig load(String path){
        //todo:read from yaml or json
        //读取yaml,如果要换成json，将YAMLFactory()替换JsonFactory()
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            //返回是一个类test environment
           return mapper.readValue(WeworkConfig.class.getResourceAsStream(path),WeworkConfig.class);
              //写yaml文件
//            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(WeworkConfig.getInstance()));
//            System.out.println(mapper.writeValueAsString(WeworkConfig.getInstance()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
