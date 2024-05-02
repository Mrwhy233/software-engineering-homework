package com.example.vhr.http;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * http 请求工具
 *
 * @author Tellsea
 * @date 2021-11-24
 */
public class HttpUtils {
    private static String COOKIE=null;

    /**
     * get请求封装
     *
     * @param url
     * @param params
     * @param encode
     * @param listener
     */
    public static void getRequest(String url, Map<String, String> params, String encode, OnResponseListener listener) {
        StringBuffer sb = new StringBuffer(url);
        sb.append("?");
        if (params != null && !params.isEmpty()) {
            //增强for遍历循环添加拼接请求内容
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }

            sb.deleteCharAt(sb.length() - 1);
        }
        if (listener != null) {
                try {
                    URL path = new URL(sb.toString());
                    Log.e("url",path.toString());
                    if (path != null) {
                        HttpURLConnection con = (HttpURLConnection) path.openConnection();
                        //设置请求方式
                        con.setRequestMethod("GET");

                        con.setRequestProperty("Cookie",COOKIE);
                        Log.e("cookie",COOKIE);
                        //链接超时3秒
                        con.setConnectTimeout(3000);
//                        con.setDoOutput(true);
                        con.setDoInput(true);
                        Log.e("code",con.getRequestMethod());
                        //应答码200表示请求成功
                        Log.e("code",String.valueOf(con.getResponseCode()));
                        Log.e("code",con.getRequestMethod());
                        if (con.getResponseCode() == 200) {
                            onSuccess(encode, listener, con);
                        }else {

                        }
                    }
                } catch (Exception error) {
                    error.printStackTrace();
                    onError(listener, error);
                }
            }
    }

    /**
     * POST请求
     *
     * @param url
     * @param params
     * @param encode
     * @param listener
     */
    public static void postRequest(String url, Map<String, String> params, String encode, OnResponseListener listener) {
        StringBuffer sb = new StringBuffer();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        if (listener != null) {
            try {
                URL path = new URL(url);
                if (path != null) {
                    HttpURLConnection con = (HttpURLConnection) path.openConnection();
                    con.setRequestProperty("Cookie",COOKIE);
                    con.setRequestMethod("POST");   //设置请求方法POST
                    con.setConnectTimeout(3000);
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    byte[] bytes = sb.toString().getBytes();
                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(bytes);
                    outputStream.close();
                    Log.e("test",con.getResponseCode()+"返回的状态码");
                    if (con.getResponseCode() == 200) {
                        onSuccess(encode, listener, con);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onError(listener, e);
            }
        }
    }

    public static void deleteRequest(String url, Map<String, String> params, String encode, OnResponseListener listener) {
        StringBuffer sb = new StringBuffer();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        if (listener != null) {
            try {
                URL path = new URL(url);
                if (path != null) {
                    HttpURLConnection con = (HttpURLConnection) path.openConnection();
                    con.setRequestProperty("Cookie",COOKIE);
                    con.setRequestMethod("DELETE");   //设置请求方法POST
                    con.setConnectTimeout(3000);
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    byte[] bytes = sb.toString().getBytes();
                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(bytes);
                    outputStream.close();
                    Log.e("test",con.getResponseCode()+"返回的状态码");
                    if (con.getResponseCode() == 200) {
                        onSuccess(encode, listener, con);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onError(listener, e);
            }
        }
    }

    /**
     * 发送json
     * @param listener
     * @param onError
     */
    public static void postRequestJson(String url, Map<String, String> params, String encode, OnResponseListener listener) {
        String s = JSON.toJSONString(params);
        if (listener != null) {
            try {
                URL path = new URL(url);
                if (path != null) {
                    HttpURLConnection con = (HttpURLConnection) path.openConnection();
                    con.setRequestProperty("Cookie",COOKIE);
                    con.setRequestProperty("Content-Type","application/json");
                    con.setRequestMethod("POST");   //设置请求方法POST
                    con.setConnectTimeout(3000);
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    Log.e("con",con.toString());
                    byte[] bytes = s.getBytes();
                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(bytes);
                    outputStream.close();
                    Log.e("test",con.getResponseCode()+"返回的状态码");
                    if (con.getResponseCode() == 200) {
                        onSuccess(encode, listener, con);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onError(listener, e);
            }
        }
    }

    private static void onError(OnResponseListener listener, Exception onError) {
        listener.onError(onError.toString());
    }

    private static void onSuccess(String encode, OnResponseListener listener, HttpURLConnection httpURLConnection) throws IOException {
        Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
        if (headerFields.get("Set-Cookie")!=null) {
            Log.e("cookie",headerFields.get("Set-Cookie").get(0));
            COOKIE=headerFields.get("Set-Cookie").get(0);
        }

        InputStream inputStream = httpURLConnection.getInputStream();
        //创建内存输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        byte[] bytes = new byte[1024];
        if (inputStream != null) {
            while ((len = inputStream.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            String str = new String(baos.toByteArray(), encode);
            listener.onSuccess(str);
        }
    }
}