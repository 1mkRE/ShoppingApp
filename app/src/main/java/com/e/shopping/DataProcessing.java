package com.e.shopping;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataProcessing {


    public String[] manage(String txt_message, Integer Id){
        int i;

        try {
            JSONObject json_message_object = new JSONObject(txt_message);
            JSONArray json_message_array = json_message_object.getJSONArray("Shopping");
            JSONObject jo = json_message_array.getJSONObject(Id);
            int x = jo.length();
            String[] objectArray = new String[x];

            for(i = 0; i < x;i ++ ){
                if(i == 0){
                    objectArray[i] = jo.getString("Id")+":";
                }
                else
                {
                    objectArray[i] = jo.getString("Part"+ Integer.toString(i));
                }
            }
            return objectArray;
        } catch (JSONException e) {
            return null;
        }
    }
}
