package guozhi.com;

import java.util.HashMap;

public class WeworkConfig {

/*    public String agentId="1000005";
    public String secret="1JPyY9GvPLZfpvxEDjok-Xt_9v7HIBYJhZUoO6EgNGY";
    public String corpid = "wwd6da61649bd66fea";
    public String contactSecret="C7uGOrNyxWWzwBsUyWEbLQdOqoWPz4hNvxj9RIFv-4U";
*/

    public String agentId="3010040";
    public String secret="ZdGzrarC-KoIV78r2f3oszXOLzDU-sxUcklAFDuV1pM";
    public String corpid = "ww76731601e6910ff6";
    public String contactSecret="b45NOjFUNr90sVcpZQ26qryYVHsiYeCMv0tpFXOUG90";

//    public String current="test";
//    public HashMap<String, HashMap<String, String>> env;

    private static WeworkConfig weworkConfig;
    public static WeworkConfig getInstance(){
        if(weworkConfig==null){
//            weworkConfig=load("/conf/WeworkConfig.yaml");
//            System.out.println(weworkConfig);
//            System.out.println(weworkConfig.agentId);
            weworkConfig=new WeworkConfig();
        }
        return weworkConfig;
    }

    public static void load(String path){
        //todo:read from yaml or json
    }
}
