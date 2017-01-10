package example.scanner;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Hanna on 10.01.2017.
 */

public final class Serializer {
    public static Computer[] deserialize(JSONObject object)
    {
        try {
            Gson gson = new Gson();
            String s =object.getString("data");
            Computer[] computers = gson.fromJson(s, Computer[].class);
            return  computers;
        }catch (Exception ex){
            return null;
        }
    }
}
