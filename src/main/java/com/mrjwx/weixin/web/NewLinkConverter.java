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
import com.mrjwx.weixin.bo.NewLinkConverterResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class NewLinkConverter {
    private static final String API_URL = "http://api.act.jutuike.com/union/convert";
    private static final String PUB_ID = "149341";

    public static NewLinkConverterResponse convertLink( String goodsId, String sid, String source, String subShareRate, String kzUrl) throws IOException {
        StringBuilder postData = new StringBuilder();
        postData.append("pub_id=").append(URLEncoder.encode(PUB_ID, StandardCharsets.UTF_8));
        postData.append("&goodsId=").append(URLEncoder.encode(goodsId, StandardCharsets.UTF_8));
        postData.append("&sid=").append(URLEncoder.encode(sid, StandardCharsets.UTF_8));
        if (source != null) {
            postData.append("&source=").append(URLEncoder.encode(source, StandardCharsets.UTF_8));
        }
        if (subShareRate != null) {
            postData.append("&sub_share_rate=").append(URLEncoder.encode(subShareRate, StandardCharsets.UTF_8));
        }
        if (kzUrl != null) {
            postData.append("&kz_url=").append(URLEncoder.encode(kzUrl, StandardCharsets.UTF_8));
        }

        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postData.length()));
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = postData.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            log.info(response.toString());
            return NewLinkConverterResponse.converToResponse(JSONObject.parseObject(response.toString()));
        }
    }
}

