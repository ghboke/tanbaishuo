package com.ghboke.tbs;

import android.content.Context;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/7.
 */

public class Honestsay {
    private String frindData;
    private String cookies;

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public String getFrindData() {
        return frindData;
    }

    public void setFrindData(String frindData) {
        this.frindData = frindData;
    }

    public SimpleAdapter GetHonestsayData( Context context) {
        String str = getjsonstr(frindData);
        ArrayList list = new ArrayList();
        try {

            JSONObject json = new JSONObject(str);
            JSONObject json_data = json.getJSONObject("data");
            JSONArray json_list = json_data.getJSONArray("list");
            for (int i = 0; i < json_list.length(); i++) {
                JSONObject json_listobj = json_list.getJSONObject(i);
                Map<String, String> maplist = new HashMap<>();

                maplist.put("title", json_listobj.getString("topicName"));

                String fromEncodeUin=json_listobj.getString("fromEncodeUin");
                String QQ=HonestsayFind.parse(fromEncodeUin);
                Log.d("QQ", fromEncodeUin+"|"+QQ);

                maplist.put("content", "QQ:"+QQ);
                list.add(maplist);
            }
        } catch (JSONException e) {
            Toast.makeText(context, "解析失败！！", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(context,
                list,// List数据
                android.R.layout.simple_list_item_2,
                new String[]{"title", "content"},
                new int[]{android.R.id.text1, android.R.id.text2});
        return simpleAdapter;

    }

    public String getjsonstr(String url) {
        OkHttpClient client = getUnsafeOkHttpClient();
        String Htmldata = "";
        try {
            Request request = new Request.Builder()
                    .url(url).addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 8.0; MI 6 Build/OPR1.170623.027; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044006 Mobile Safari/537.36 V1_AND_SQ_7.5.5_806_YYB_D QQ/7.5.5.3460 NetType/4G WebP/0.3.0 Pixel/1080")
                    .header("Cookie", cookies)
                    .build();//创建Request 对象
            Response response = null;
            response = client.newCall(request).execute();//得到Response 对象
            if (response.isSuccessful()) {
                Htmldata = response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ERRO", "访问出错");
        }
        return Htmldata;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
