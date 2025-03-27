package utils;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.File;

public class JSONReader {

    public static String get(String key){
        String testDataValue;
        return testDataValue = (String) getJSONData().get(key);//input is the key
    }
    public static JSONObject getJSONData() {
        Object obj = null;
        try {
            File fileName = new File("resources/testData/endPoints.json");
            String json = FileUtils.readFileToString(fileName, "UTF-8");
            obj = new JSONParser().parse(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (JSONObject) obj;
    }

}
