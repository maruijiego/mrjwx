package com.mrjwx.weixin.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.alibaba.fastjson2.JSONObject;
import com.mrjwx.weixin.util.TokenUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.el.parser.Token;

@Log4j2
public class MenuCreator {
    private static final String API_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";

    public static void main(String[] args) {
        createMenu();
    }

    public static void createMenu() {
        try {
            // 构建菜单数据
            JSONObject menu = new JSONObject();
            JSONObject button1 = new JSONObject();
            button1.put("type", "view");
            button1.put("name", "测手相");
            button1.put("url", "http://www.maruijiego.cn/handle");

            JSONObject button2 = new JSONObject();
            button2.put("type", "view");
            button2.put("name", "饿了么外卖");
            button2.put("url", "https://u.ele.me/wZJ3CwoP");

            JSONObject button3 = new JSONObject();
            button3.put("type", "view");
            button3.put("name", "美团外卖");
            button3.put("url", "http://dpurl.cn/eIGNysPz");

            JSONObject[] buttons = {button1, button2, button3};
            menu.put("button", buttons);

            String postData = menu.toJSONString();

            URL url = new URL(API_URL+ TokenUtil.getAccessToken());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
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
                log.info(response.toString());
            }
        } catch (Exception e) {
            log.error("调用接口时出错", e);
        }
    }
}