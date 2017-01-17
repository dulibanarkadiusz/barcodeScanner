package example.scanner;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Hanna on 10.01.2017.
 */

public final class Serializer {
    public static Object[] deserialize(JSONObject object, Class<?> className )
    {
        try {
            Gson gson = new Gson();
            String s =object.getString("data");
            Object[] objects;
            if(className == Computer.class)
                objects = gson.fromJson(s,Computer[].class);
            else if(className == DropdownElement.class)
                objects = gson.fromJson(s,DropdownElement[].class);
            else
                return null;
            return  objects;
        }catch (Exception ex){
            return null;
        }
    }

    public static String serialize(Object object){
        try{
            Gson gson =  new Gson();
            String j = gson.toJson(object);
            return j;
        }catch (Exception ex){
            return "";
        }
    }
}
