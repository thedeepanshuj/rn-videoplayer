package com.vdocipherdemo;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.util.Iterator;

public class Utils {

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("codingninjas.in");
            return !ipAddr.equals("");
        } catch (Exception e) {
            return false;
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static WritableMap convertJsonToMap(JSONObject jsonObject) throws JSONException {
        WritableMap map = Arguments.createMap();

        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = jsonObject.get(key);

            if (value instanceof JSONObject)
                map.putMap(key, convertJsonToMap((JSONObject) value));
            else if (value instanceof JSONArray)
                map.putArray(key, convertJsonToArray((JSONArray) value));
            else if (value instanceof Boolean)
                map.putBoolean(key, (Boolean) value);
            else if (value instanceof Integer)
                map.putInt(key, (Integer) value);
            else if (value instanceof Double)
                map.putDouble(key, (Double) value);
            else if (value instanceof String)
                map.putString(key, (String) value);
            else
                map.putString(key, value.toString());
        }
        return map;
    }

    private static WritableArray convertJsonToArray(JSONArray jsonArray) throws JSONException {
        WritableArray array = new WritableNativeArray();

        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONObject) {
                array.pushMap(convertJsonToMap((JSONObject) value));
            } else if (value instanceof JSONArray) {
                array.pushArray(convertJsonToArray((JSONArray) value));
            } else if (value instanceof Boolean) {
                array.pushBoolean((Boolean) value);
            } else if (value instanceof Integer) {
                array.pushInt((Integer) value);
            } else if (value instanceof Double) {
                array.pushDouble((Double) value);
            } else if (value instanceof String)  {
                array.pushString((String) value);
            } else {
                array.pushString(value.toString());
            }
        }
        return array;
    }


}
