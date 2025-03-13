package com.mrjwx.weixin.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import com.alibaba.fastjson2.JSONObject;
import com.mrjwx.weixin.bo.LinkConverterResponse;

public class LinkConverter {
    private static final String API_URL = "http://api.act.jutuike.com/union/wn_convert";
    private static final String PUB_ID = "149341";

    public static LinkConverterResponse convertLink(String content) throws IOException {
        String encodedContent = URLEncoder.encode(content, StandardCharsets.UTF_8);
        String postData = "pub_id=" + PUB_ID + "&content=" + encodedContent;

        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postData.length()));
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = postData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return LinkConverterResponse.converToResponse(JSONObject.parseObject(response.toString()));
        }
    }
}