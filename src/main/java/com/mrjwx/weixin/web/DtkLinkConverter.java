package com.mrjwx.weixin.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import com.alibaba.fastjson2.JSONObject;
import com.mrjwx.weixin.bo.DtkLinkConverterResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DtkLinkConverter {
    private static final String API_URL = "https://openapi.dataoke.com/api/tb-service/parse-content";

    public static DtkLinkConverterResponse convertLink( String content) throws IOException {
        TreeMap<String, String> paraMap = new TreeMap<>();
        paraMap.put("version", "v1.0.0");
        paraMap.put("appKey", "67d1a5fe92f2e");
        paraMap.put("AppSecret", "5fc789e2420c30465ff2639d25aafe95");

        paraMap.put("content", content);




        // 生成Nonce和Timer参数
        String nonce = generateNonce();
        long timer = System.currentTimeMillis();

        // 组装验签字符串
        String signString = assembleSignString("67d1a5fe92f2e", "5fc789e2420c30465ff2639d25aafe95", nonce, timer);

        // 对验签字符串进行MD5加密并转成大写
        String signRan = md5Encrypt(signString).toUpperCase();

        // 将生成的MD5加密结果加入请求链接
        paraMap.put("signRan", signRan);
        paraMap.put("nonce", nonce);
        paraMap.put("timer", String.valueOf(timer));
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> entry : paraMap.entrySet()) {
            if (postData.length() > 0) {
                postData.append("&");
            }
            postData.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        URL url = new URL(API_URL + "?" + postData.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            log.info(response.toString());
            return DtkLinkConverterResponse.converToResponse(JSONObject.parseObject(response.toString()));
        }
    }

    // 生成6位随机数
    private static String generateNonce() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    // 组装验签字符串
    private static String assembleSignString(String appKey, String appSecret, String nonce, long timer) {
        return "appKey=" + appKey + "&timer=" + timer + "&nonce=" + nonce + "&key=" + appSecret;
    }

    // MD5加密
    private static String md5Encrypt(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}