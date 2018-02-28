
package com.ceo.fucklj.util;

import com.ceo.fucklj.Env;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonHelper {

    private static final boolean DEBUG = false;

    private static final String TAG = "JsonHelper";

    public static String getStringJo(JSONObject jo, String key) {
        String ret = null;
        try {
            if (!jo.isNull(key)) {
                ret = jo.getString(key);
            }
        } catch (JSONException e) {
            //
        }
        if (DEBUG) {
            Log.d(TAG, key + ":" + ret);
        }
        return ret;
    }

    public static int getIntJo(JSONObject jo, String key) {
        int ret = 0;
        try {
            if (!jo.isNull(key)) {
                ret = jo.getInt(key);
            }
        } catch (JSONException e) {
            //
        }
        if (DEBUG) {
            Log.d(TAG, key + ":" + ret);
        }
        return ret;
    }

    public static long getLongJo(JSONObject jo, String key) {
        long ret = 0;
        try {
            if (!jo.isNull(key)) {
                ret = jo.getLong(key);
            }
        } catch (JSONException e) {
            //
        }
        if (DEBUG) {
            Log.d(TAG, key + ":" + ret);
        }
        return ret;
    }

    public static boolean getBooleanJo(JSONObject jo, String key) {
        boolean ret = false;
        try {
            if (!jo.isNull(key)) {
                ret = jo.getBoolean(key);
            }
        } catch (JSONException e) {
            //
        }
        if (DEBUG) {
            Log.d(TAG, key + ":" + ret);
        }
        return ret;
    }

    public static JSONObject getJsonObjectJo(JSONObject jo, String key) {
        JSONObject ret = null;
        try {
            if (!jo.isNull(key)) {
                ret = jo.getJSONObject(key);
            }
        } catch (JSONException e) {
            //
        }
        if (DEBUG) {
            Log.d(TAG, key + ":" + ret);
        }
        return ret;
    }

    public static JSONArray getJsonArrayJo(JSONObject jo, String key) {
        JSONArray ret = null;
        try {
            if (!jo.isNull(key)) {
                ret = jo.getJSONArray(key);
            }
        } catch (JSONException e) {
            //
        }
        if (DEBUG) {
            Log.d(TAG, key + ":" + ret);
        }
        return ret;
    }

    public static <T> List<T> JsonArrayToList(JSONArray ja) {
        List<T> ret = new ArrayList<T>();
        if (ja == null) {
            return ret;
        }

        for (int i = 0; i < ja.length(); i++) {
            try {
                Object obj = ja.get(i);
                if (obj != null) {
                    ret.add((T) obj);
                }
            } catch (JSONException e) {
                //
            }
        }
        return ret;
    }

    public static void putStringJo(JSONObject jo, String key, String value) {
        try {
            jo.put(key, value);
        } catch (JSONException e) {
            //
        }
        if (DEBUG) {
            Log.d(TAG, key + ":" + key + " value:" + value);
        }
    }

    public static void putIntJo(JSONObject jo, String key, int value) {
        try {
            jo.put(key, value);
        } catch (JSONException e) {
            //
        }
        if (DEBUG) {
            Log.d(TAG, key + ":" + key + " value:" + value);
        }
    }

    public static void putLongJo(JSONObject jo, String key, long value) {
        try {
            jo.put(key, value);
        } catch (JSONException e) {
            //
        }
        if (DEBUG) {
            Log.d(TAG, key + ":" + key + " value:" + value);
        }
    }

    public static void putJsonObjectJo(JSONObject jo, String key, JSONObject value) {
        try {
            jo.put(key, value);
        } catch (JSONException e) {
            //
        }
        if (DEBUG) {
            Log.d(TAG, key + ":" + key + " value:" + value);
        }
    }

    public static void putJsonArrayJo(JSONObject jo, String key, JSONArray value) {
        try {
            jo.put(key, value);
        } catch (JSONException e) {
            //
        }
        if (DEBUG) {
            Log.d(TAG, key + ":" + key + " value:" + value);
        }
    }

    public static void putJsonObjectJa(JSONArray ja, JSONObject value) {
        ja.put(value);
        if (DEBUG) {
            Log.d(TAG, "value:" + value);
        }
    }
}
