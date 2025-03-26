package com.mrjwx.weixin.util;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
@Log4j2
public class TokenUtil {
    //测试环境
//    private static final String APP_ID = "wx3a41d00891522da5";
//    private static final String APP_SECRET = "b7979c04256d997a879afc08145661b8";
    //生产环境
    private static final String APP_ID = "wxf667353ef15b8030";
    private static final String APP_SECRET = "dd0d1f7e8e995ca50e5c7fc24620c93a";

    private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APP_ID + "&secret=" + APP_SECRET;
    private static final Cache<String, String> tokenCache = CacheBuilder.newBuilder()
           .expireAfterWrite(7200, TimeUnit.SECONDS)
           .build();
    public static void main(String[] args) {
        getAccessToken();
    }

    public static String getAccessToken() {
        String accessToken = tokenCache.getIfPresent("access_token");
        if (accessToken == null) {
            accessToken = refreshAccessToken();
            if (accessToken != null) {
                tokenCache.put("access_token", accessToken);
            }
        }
        log.info("accessToken:",accessToken);
        return accessToken;
    }

    private static String refreshAccessToken() {
        try {
            URL url = new URL(TOKEN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                java.util.Scanner scanner = new java.util.Scanner(connection.getInputStream(), "UTF-8").useDelimiter("\\A");
                String response = scanner.hasNext() ? scanner.next() : "";
                JSONObject json = JSONObject.parseObject(response);
                if (json.containsKey("access_token")) {
                    return json.getString("access_token");
                } else if (json.containsKey("errcode")) {
                    handleError(json.getIntValue("errcode"), json.getString("errmsg"));
                }
            } else {
                log.error("HTTP request failed with response code:{} " , responseCode);
            }
        } catch (Exception e) {
            log.error("refreshAccessToken 出错", e);
        }
        return null;
    }

    private static void handleError(int errorCode, String errorMessage) {
        switch (errorCode) {
            case -1:
                log.error("系统繁忙，此时请开发者稍候再试: " + errorMessage);
                break;
            case 40001:
                log.error("AppSecret错误或者AppSecret不属于这个公众号，请开发者确认AppSecret的正确性: " + errorMessage);
                break;
            case 40002:
                log.error("请确保grant_type字段值为client_credential: " + errorMessage);
                break;
            case 40164:
                log.error("调用接口的IP地址不在白名单中，请在接口IP白名单中进行设置: " + errorMessage);
                break;
            case 40243:
                log.error("AppSecret已被冻结，请登录MP解冻后再次调用: " + errorMessage);
                break;
            case 89503:
                log.error("此IP调用需要管理员确认,请联系管理员: " + errorMessage);
                break;
            case 89501:
                log.error("此IP正在等待管理员确认,请联系管理员: " + errorMessage);
                break;
            case 89506:
                log.error("24小时内该IP被管理员拒绝调用两次，24小时内不可再使用该IP调用: " + errorMessage);
                break;
            case 89507:
                log.error("1小时内该IP被管理员拒绝调用一次，1小时内不可再使用该IP调用: " + errorMessage);
                break;
            default:
                log.error("未知错误: " + errorMessage);
        }
    }
}
