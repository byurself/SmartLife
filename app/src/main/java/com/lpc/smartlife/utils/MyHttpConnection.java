package com.lpc.smartlife.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Set;


public class MyHttpConnection {
    /**
     * 进行post数据发送
     *
     * @param path http路径
     * @param param json格式的参数
     * @return
     */
    String header = "http://192.168.43.240:8090/api";

    public String myPost(String path, JSONObject param) {
        String result = "";
        try {
            URL url = new URL(header + path);
            // 利用HttpURLConnection对象，我们可以从网页中获取网页数据
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 单位为毫秒，设置超时时间为5秒
            conn.setConnectTimeout(5 * 1000);

            // http正文内，因此需要设为true, 默认情况下是false;
            conn.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            //conn.setDoInput(true);
            // HttpURLConnection对象是通过HTTP协议请求path路径的，所以需要设置请求方式，可以不设置，因为默认为get
            // Post 请求不能使用缓存
            conn.setUseCaches(false);
            // 设定传送的内容类型是可序列化的java对象
            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
            conn.setRequestMethod("POST"); // 设置请求方式
            //获取输出流对象，把请求参数发送打服务器接口
            OutputStream out = conn.getOutputStream();
            out.write(getPostDataString(param).getBytes("UTF-8"));
            out.flush();

            out.close();

            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream(); // 获取输入流，此时才真正建立链接
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader bufferReader = new BufferedReader(isr);
                String inputLine = "";
                while ((inputLine = bufferReader.readLine()) != null) {
                    result += inputLine + "\n";
                }
                is.close();
                isr.close();
                bufferReader.close();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return result;

    }

    public String myGet(String path) {
        HttpURLConnection conn = null; //连接对象
        String reslut = "";
        try {
            URL url = new URL(header + path); //URL对象
            conn = (HttpURLConnection) url.openConnection(); //使用URL打开一个链接
            conn.setConnectTimeout(5 * 1000);
            conn.setDoInput(true); //允许输入流，即允许下载
            //conn.setDoOutput(true); //允许输出流，即允许上传
            conn.setUseCaches(false); //不使用缓冲
            conn.setRequestMethod("GET"); //使用get请求
            InputStream is = conn.getInputStream();   //获取输入流，此时才真正建立链接
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine = "";
            while ((inputLine = bufferReader.readLine()) != null) {
                reslut += inputLine + "\n";
            }
            is.close();
            isr.close();
            bufferReader.close();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

        }

        return reslut;

    }

    public String doPost(String path, JSONObject param) {
        String result = "";
        try {
            URL url = new URL(path);
            // 利用HttpURLConnection对象，我们可以从网页中获取网页数据
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 单位为毫秒，设置超时时间为5秒
            conn.setConnectTimeout(5 * 1000);

            // http正文内，因此需要设为true, 默认情况下是false;
            conn.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            //conn.setDoInput(true);
            // HttpURLConnection对象是通过HTTP协议请求path路径的，所以需要设置请求方式，可以不设置，因为默认为get
            // Post 请求不能使用缓存
            conn.setUseCaches(false);
            // 设定传送的内容类型是可序列化的java对象
            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
            conn.setRequestMethod("POST"); // 设置请求方式
            //获取输出流对象，把请求参数发送打服务器接口
            OutputStream out = conn.getOutputStream();
            out.write(getPostDataString(param).getBytes("UTF-8"));
            out.flush();

            out.close();

            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream(); // 获取输入流，此时才真正建立链接
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader bufferReader = new BufferedReader(isr);
                String inputLine = "";
                while ((inputLine = bufferReader.readLine()) != null) {
                    result += inputLine + "\n";
                }
                is.close();
                isr.close();
                bufferReader.close();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return result;

    }

    public String doGet(String path) {
        HttpURLConnection conn = null; //连接对象
        String reslut = "";
        try {
            URL url = new URL(path); //URL对象
            conn = (HttpURLConnection) url.openConnection(); //使用URL打开一个链接
            conn.setConnectTimeout(5 * 1000);
            conn.setDoInput(true); //允许输入流，即允许下载
            //conn.setDoOutput(true); //允许输出流，即允许上传
            conn.setUseCaches(false); //不使用缓冲
            conn.setRequestMethod("GET"); //使用get请求
            InputStream is = conn.getInputStream();   //获取输入流，此时才真正建立链接
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine = "";
            while ((inputLine = bufferReader.readLine()) != null) {
                reslut += inputLine + "\n";
            }
            is.close();
            isr.close();
            bufferReader.close();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

        }

        return reslut;

    }

    private String getPostDataString(JSONObject params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        int index = 0;

        Set<String> keys = params.keySet();

        for (String key : keys) {
            if (index > 0)
                result.append("&");
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(params.getString(key), "UTF-8"));
            index++;
        }

        return result.toString();
    }
}