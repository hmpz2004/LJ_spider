package com.ceo.fucklj;

import com.ceo.fucklj.util.*;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

public class Main {

    public static String jsonPathChengjiao = "/Users/pangzhou/Development/general_workspace/Reverse/lianjia/HomeLink_unzip/assets/city_info_secd.json";

    public static String chengjiao_quyu_url_format = "https://app.api.lianjia.com/house/chengjiao/search?city_id=110000&group_type=community&limit_offset=%d&limit_count=%d&bizcircle_id=%d&request_ts=%d";

    public static String search_community_url_format = "https://app.api.lianjia.com/house/community/search?city_id=110000&group_type=community&limit_offset=%d&limit_count=%d&bizcircle_id=%d&request_ts=%d";

    public static String SQL_DB_PATH = "jdbc:sqlite:///Users/pangzhou/Development/general_workspace/Reverse/lianjia/d.db";//windows && linux都适用

    public static String a_sha1(String arg6) {
        String v0_2;
        int v0 = 0;
        try {
            MessageDigest v1 = MessageDigest.getInstance("SHA-1");
            v1.update(arg6.getBytes());
            byte[] v1_1 = v1.digest();
            StringBuffer v2 = new StringBuffer();
            while(v0 < v1_1.length) {
                String v3 = Integer.toHexString(v1_1[v0] & 255);
                if(v3.length() < 2) {
                    v2.append(0);
                }

                v2.append(v3);
                ++v0;
            }

            v0_2 = v2.toString();
        }
        catch(NoSuchAlgorithmException v0_1) {
            v0_1.printStackTrace();
            v0_2 = "";
        }

        return v0_2;
    }

    static class ComPara implements Comparator {
        ComPara() {
            super();
        }

//        public static int a(Map.Entry<String, String> arg2, Map.Entry<String, String> arg3) {
//            return arg2.getKey().compareTo(arg3.getKey());
//        }

        public int compare(Object arg3, Object arg4) {
            return ((Map.Entry<String, String>)arg3).getKey().compareTo(((Map.Entry<String, String>)arg4).getKey());
        }
    }

    public static void main(String[] args) {
        fuckChengjiao();
//        fuckCummunity();
    }

    private static void fuckCummunity() {

    }

    private static void fuckChengjiao() {
        byte[] jsonBytes = FileUtils.readFileByte(new File(jsonPathChengjiao));
        String jsonString = new String(jsonBytes);
        JSONObject joTotal = null;
        try {

            joTotal = new JSONObject(jsonString);

            JSONArray jaInfo = JsonHelper.getJsonArrayJo(joTotal, "info");
            LogUtil.logDebug(jaInfo.length());
            JSONObject joIdx0 = (JSONObject) jaInfo.get(0);
            LogUtil.logDebug(joIdx0);
            Iterator iter = joIdx0.keys();
            while (iter.hasNext()) {
                String item = (String) iter.next();
                LogUtil.logDebug(item);
            }
            JSONArray jaDistrict = JsonHelper.getJsonArrayJo(joIdx0, "district");
            LogUtil.logDebug(jaDistrict);
            int cnt = 0;
            for (int i = 0; i < jaDistrict.length(); i++) {
                JSONObject joItem = jaDistrict.getJSONObject(i);
                JSONArray jaBizcircle = JsonHelper.getJsonArrayJo(joItem, "bizcircle");
                LogUtil.logDebug("name : " + joItem.getString("district_name") + " bizcircle size : " + jaBizcircle.length());
                for (int j = 0; j < jaBizcircle.length(); j++) {
                    JSONObject joBizItem = jaBizcircle.getJSONObject(j);
                    LogUtil.logDebug("            " + (j+1) + " " + joBizItem.getString("bizcircle_name") + " " + joBizItem.getString("bizcircle_id"));
                    int biz_id = Integer.valueOf(joBizItem.getString("bizcircle_id"));

                    // 请求该商圈内所有成交数据
                    loopRequestBiz(biz_id);
                    // 请求该商圈内所有小区数据
                    loopRequestCommunity(biz_id);

                    cnt++;
                }
            }
            LogUtil.logDebug("total biz cycle count " + cnt);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        int wangjing_biz_id = 18335711;
//        loopRequestBiz(wangjing_biz_id);
    }

    private static void loopRequestCommunity(int biz_id) {
        int maxLoop = 5000;
        int loopTimes = 0;
        int offset = 0;
        int count = 20;
        int hasMore = 0;
        int totalCount = 0;
        do {
            loopTimes++;
            try {
                int ts = (int) (System.currentTimeMillis() / 1000);

                String responseResult = genUrlAndSendRequest(search_community_url_format, offset, count, biz_id, ts);

                JSONObject joResult = new JSONObject(responseResult);
                JSONObject joData = joResult.getJSONObject("data");
                if (joData.has("total_count")) {
                    int t = joData.getInt("total_count");
                    if (t > totalCount) {
                        totalCount = t;
                    }
                }
                hasMore = joResult.getJSONObject("data").getInt("has_more_data");

                parseXiaoquJsonResultAndInsertDb(responseResult);

                LogUtil.logDebug("totalCount " + totalCount + " hasMore " + hasMore);

                offset = offset + count;
                if (offset > totalCount || hasMore == 0) {
                    LogUtil.logDebug("offset or hasMore not right and break");
                    break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (loopTimes < maxLoop && hasMore == 1);
    }

    private static void loopRequestBiz(int biz_id) {
        int maxLoop = 5000;
        int loopTimes = 0;
        int offset = 0;
        int count = 20;
        int hasMore = 0;
        int totalCount = 0;
        do {
            loopTimes++;
            try {
                int ts = (int) (System.currentTimeMillis() / 1000);

                String responseResult = genUrlAndSendRequest(chengjiao_quyu_url_format, offset, count, biz_id, ts);

                JSONObject joResult = new JSONObject(responseResult);
                JSONObject joData = joResult.getJSONObject("data");
                if (joData.has("total_count")) {
                    int t = joData.getInt("total_count");
                    if (t > totalCount) {
                        totalCount = t;
                    }
                }
                hasMore = joResult.getJSONObject("data").getInt("has_more_data");

                LogUtil.logDebug("totalCount " + totalCount + " hasMore " + hasMore);

                // parse result and insert db
                parseChengjiaoJsonResultAndInsertDb(responseResult);

                offset = offset + count;
                if (offset > totalCount || hasMore == 0) {
                    LogUtil.logDebug("offset or hasMore not right and break");
                    break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (loopTimes < maxLoop && hasMore == 1);
    }

    private static String genUrlAndSendRequest(String url_format, int offset, int count, int biz_id, int ts) {
        ts = 1482252874;
        String url = String.format(url_format, offset, count, biz_id, ts);
        LogUtil.logDebug(url);

        String urlParamString = url.substring(url.indexOf("?") + 1);
        LogUtil.logDebug(urlParamString);

        Map<String, String[]> urlMap = HttpUtil.getParamsMap(urlParamString, "utf-8");
        List<Map.Entry> list = new ArrayList<Map.Entry>(urlMap.entrySet());
        Collections.sort(((List) list), new ComPara());

        StringBuilder sb = new StringBuilder("b6a0f8718f584f83d26daf04c56f8519");
        for (Map.Entry<String, String[]> itemEntry : list) {
            String t = itemEntry.getKey() + "=" + itemEntry.getValue()[0];
            LogUtil.logDebug("   " + t);
            sb.append(t);
        }
        LogUtil.logDebug(sb.toString());
        String sha1Value = a_sha1(sb.toString());
        String baseString = Base64.encode(("20160824_android:" + sha1Value).getBytes());
        LogUtil.logDebug(" base64 " + baseString);

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Page-Schema", "lianjia%3A%2F%2FTradedSearchHouseListActivity");
        headerMap.put("Referer", "lianjia%3A%2F%2FTradedSearchHouseListActivity%3Fcity_id%3D110000%26limit_count%3D20%26limit_offset%3D0");
        headerMap.put("Cookie", "lianjia_udid=351554053596418;;lianjia_ssid=e6dafa48-e33c-43ad-b7fb-89a6abcec187;lianjia_uuid=e0537c7b-0fef-405a-af3b-e7d61218ac67");
        headerMap.put("User-Agent", "HomeLink7.1.1;google Galaxy+Nexus; Android 4.2.2");
        headerMap.put("Lianjia-Channel", "");
        headerMap.put("Lianjia-Device-Id", "351554053596418");
        headerMap.put("Lianjia-Version", "7.1.1");
        headerMap.put("Authorization", baseString);

//            String responseResult = HttpClientUtil.doGet(url, headerMap, "UTF-8");

        String responseResult = HttpsUtil.HttpsGet(url, headerMap);
//        LogUtil.logDebug(responseResult);
        return responseResult;
    }

    private static void parseXiaoquJsonResultAndInsertDb(String resultString) {
        try {
            String sqlDbPath = SQL_DB_PATH;

            //2，连接SQLite的JDBC
            Class.forName("org.sqlite.JDBC");

            //建立一个数据库名zieckey.db的连接，如果不存在就在当前目录下自动创建
            Connection conn = DriverManager.getConnection(sqlDbPath);
            SqliteUtil su = new SqliteUtil(conn);

            su.createTable(SqliteUtil.CREATE_BIZ_CYCLE_XIAOQU_TABLE);

            JSONObject joResult = new JSONObject(resultString);

            JSONArray jaData = joResult.getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < jaData.length(); i++) {
                JSONObject joItem = jaData.getJSONObject(i);
                String community_id = joItem.getString("community_id");
                String community_name = joItem.getString("community_name");
                String cover_pic = "";
                if (joItem.has("cover_pic")) {
                    cover_pic = joItem.getString("cover_pic");
                }
                String district_name = joItem.getString("district_name");
                String bizcircle_name = joItem.getString("bizcircle_name");
                String building_finish_year = "";
                if (joItem.has("building_finish_year")) {
                    building_finish_year = joItem.getString("building_finish_year");
                }
                String building_type = "";
                if (joItem.has("building_type")) {
                    building_type = joItem.getString("building_type");
                }
                String ershoufang_source_count = "";
                if (joItem.has("ershoufang_source_count")) {
                    ershoufang_source_count = joItem.getString("ershoufang_source_count");
                }
                String ershoufang_avg_unit_price = "";
                if (joItem.has("ershoufang_avg_unit_price")) {
                    ershoufang_avg_unit_price = joItem.getString("ershoufang_avg_unit_price");
                }
                String schema = joItem.getString("schema");

                String[] paramArray = new String[]{
                        community_id,
                        community_name,
                        cover_pic,
                        district_name,
                        bizcircle_name,
                        building_finish_year,
                        building_type,
                        ershoufang_source_count,
                        ershoufang_avg_unit_price,
                        schema
                };

                su.insertAutoCre("biz_cycle_xiaoqu", SqliteUtil.CREATE_BIZ_CYCLE_XIAOQU_TABLE_COLS_ARRAY, paramArray);
            }

            // 关闭数据库连接
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseChengjiaoJsonResultAndInsertDb(String resultString) {
        try {
            String sqlDbPath = SQL_DB_PATH;

            //2，连接SQLite的JDBC
            Class.forName("org.sqlite.JDBC");

            //建立一个数据库名zieckey.db的连接，如果不存在就在当前目录下自动创建
            Connection conn = DriverManager.getConnection(sqlDbPath);
            SqliteUtil su = new SqliteUtil(conn);

            su.createTable(SqliteUtil.CREATE_BIZ_CYCLE_CHENGJIAO_TABLE);

            JSONObject joResult = new JSONObject(resultString);

            JSONArray jaData = joResult.getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < jaData.length(); i++) {
                JSONObject joItem = jaData.getJSONObject(i);
                String bizcircle_id = joItem.getString("bizcircle_id");
                String community_id = joItem.getString("community_id");
                String house_code = joItem.getString("house_code");
                String title = joItem.getString("title");
                String kv_house_type = joItem.getString("kv_house_type");
                String cover_pic = "";
                if (joItem.has("cover_pic")) {
                    cover_pic = joItem.getString("cover_pic");
                }
                String frame_id = joItem.getString("frame_id");
                String blueprint_hall_num = joItem.getString("blueprint_hall_num");
                String blueprint_bedroom_num = joItem.getString("blueprint_bedroom_num");
                String area = joItem.getString("area");
                String price = joItem.getString("price");
                String unit_price = joItem.getString("unit_price");
                String sign_date = joItem.getString("sign_date");
                String sign_timestamp = joItem.getString("sign_timestamp");
                String sign_source = joItem.getString("sign_source");
                String orientation = joItem.getString("orientation");
                String floor_state = joItem.getString("floor_state");
                String building_finish_year = joItem.getString("building_finish_year");
                String decoration = joItem.getString("decoration");
                String building_type = "";
                if (joItem.has("building_type")) {
                    building_type = joItem.getString("building_type");
                }
                /*
                LogUtil.logDebug("bizcircle_id " + bizcircle_id +
                        " community_id " + community_id +
                        " house_code " + house_code +
                        " title " + title +
                        " kv_house_type " + kv_house_type +
//                        " cover_pic " + cover_pic +
                        " frame_id " + frame_id +
                        " blueprint_hall_num " + blueprint_hall_num +
                        " blueprint_bedroom_num " + blueprint_bedroom_num +
                        " area " + area +
                        " price " + price +
                        " unit_price " + unit_price
//                        "  " +  +
                );
                */

                String[] paramArray = new String[]{
                        bizcircle_id,
                        community_id,
                        house_code,
                        title,
                        kv_house_type,
                        cover_pic,
                        frame_id,
                        blueprint_hall_num,
                        blueprint_bedroom_num,
                        area,
                        price,
                        unit_price,
                        sign_date,
                        sign_timestamp,
                        sign_source,
                        orientation,
                        floor_state,
                        building_finish_year,
                        decoration,
                        building_type
                };

//                try {
//                    Thread.sleep(1000);
//                } catch (Exception e) {
//                }

//                su.insert("biz_cycle_chengjiao", paramArray);
                su.insertAutoCre("biz_cycle_chengjiao", SqliteUtil.CREATE_BIZ_CYCLE_CHENGJIAO_TABLE_COLS_ARRAY, paramArray);
            }

            // 关闭数据库连接
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
