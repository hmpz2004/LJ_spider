package com.ceo.fucklj.util;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

public class HttpsUtil {

    static TrustManager[] xtmArray = new TrustManager[] {
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    LogUtil.logDebug("checkClientTrusted()");
                }
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    LogUtil.logDebug("checkServerTrusted()");
                }
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    LogUtil.logDebug("getAcceptedIssuers()");
                    return null;
                }
        }};// 创建信任规则列表

    private final static int CONNENT_TIMEOUT = 15000;
    private final static int READ_TIMEOUT = 15000;
    static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * 信任所有主机-对于任何证书都不做检查 Create a trust manager that does not validate
     * certificate chains， Android 采用X509的证书信息机制，Install the all-trusting trust
     * manager
     */
    private static void trustAllHosts() {
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, xtmArray, new java.security.SecureRandom());
            sc.init(null, xtmArray, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            // 不进行主机名确认,对所有主机
            HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     /**
     * https get方法，返回值是https请求，服务端返回的数据string类型，数据进行xml解析
     * */
     public static String HttpsGet(String httpsurl, Map<String, String> map) {
         String result = null;
         HttpURLConnection http = null;
         URL url;
         try {
             url = new URL(httpsurl);
             // 判断是http请求还是https请求
             trustAllHosts();
//             http = (HttpsURLConnection) url.openConnection();
//             ((HttpsURLConnection) http).setHostnameVerifier(DO_NOT_VERIFY);// 不进行主机名确认

             http = HttpsUtil2.connect(httpsurl);

             http.setConnectTimeout(CONNENT_TIMEOUT);// 设置超时时间
             http.setReadTimeout(READ_TIMEOUT);
             http.setRequestMethod("GET");// 设置请求类型
             http.setDoInput(true);

             for (Map.Entry<String, String> entry : map.entrySet()) {
                 http.setRequestProperty(entry.getKey(), entry.getValue());
             }

//             LogUtil.logDebug("https hesder size " + http.getHeaderFields().size());
//             Map<String, List<String>> mm = http.getHeaderFields();
//             int i = 0;
//             for (Map.Entry<String, List<String>> e : mm.entrySet()) {
//                 LogUtil.logDebug(" ITEM ENTRY " + e.getKey() + " " + ((List)e.getValue()).get(i++));
//             }

             // 设置http返回状态200（ok）还是403
             int responseCode = http.getResponseCode();
             BufferedReader in = null;
             if (responseCode == 200) {
                 getCookie(http);
                 in = new BufferedReader(new InputStreamReader(
                         http.getInputStream()));
             } else
                 in = new BufferedReader(new InputStreamReader(
                         http.getErrorStream()));
             String temp = in.readLine();
             while (temp != null) {
                 if (result != null)
                     result += temp;
                 else
                     result = temp;
                 temp = in.readLine();
             }
             in.close();
             http.disconnect();
         } catch (Exception e) {
             e.printStackTrace();
         }
         return result;
     }

    /**
     * https post方法，返回值是https请求，服务端返回的数据string类型，数据进行xml解析
     * */
    public static String HttpsPost(String httpsurl, String data) {
        String result = null;
        HttpURLConnection http = null;
        URL url;
        try {
            url = new URL(httpsurl);
            // 判断是http请求还是https请求
            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                http = (HttpsURLConnection) url.openConnection();
                ((HttpsURLConnection) http).setHostnameVerifier(DO_NOT_VERIFY);// 不进行主机名确认
            } else {
                http = (HttpURLConnection) url.openConnection();
            }

            http.setConnectTimeout(CONNENT_TIMEOUT);// 设置超时时间
            http.setReadTimeout(READ_TIMEOUT);
            if (data == null) {
                http.setRequestMethod("GET");// 设置请求类型
                http.setDoInput(true);
                // http.setRequestProperty("Content-Type", "text/xml");
//                if (AppSession.mCookieStore != null)
//                    http.setRequestProperty("Cookie", AppSession.mCookieStore);
            } else {
                http.setRequestMethod("POST");// 设置请求类型为post
                http.setDoInput(true);
                http.setDoOutput(true);
                // http.setRequestProperty("Content-Type", "text/xml");
//                if (AppSession.mCookieStore != null
//                        && AppSession.mCookieStore.trim().length() > 0)
//                    http.setRequestProperty("Cookie", AppSession.mCookieStore);

                DataOutputStream out = new DataOutputStream(
                        http.getOutputStream());
                out.writeBytes(data);
                out.flush();
                out.close();
            }

            // 设置http返回状态200（ok）还是403
            int responseCode = http.getResponseCode();
            BufferedReader in = null;
            if (responseCode == 200) {
                getCookie(http);
                in = new BufferedReader(new InputStreamReader(
                        http.getInputStream()));
            } else
                in = new BufferedReader(new InputStreamReader(
                        http.getErrorStream()));
            String temp = in.readLine();
            while (temp != null) {
                if (result != null)
                    result += temp;
                else
                    result = temp;
                temp = in.readLine();
            }
            in.close();
            http.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到cookie
     *
     */
    private static void getCookie(HttpURLConnection http) {
        String cookieVal = null;
        String key = null;
        String mCookieStore = "";
        for (int i = 1; (key = http.getHeaderFieldKey(i)) != null; i++) {
            if (key.equalsIgnoreCase("set-cookie")) {
                cookieVal = http.getHeaderField(i);
                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                mCookieStore = mCookieStore + cookieVal + ";";
            }
        }
    }
}
