package com.mrjwx.weixin.bo;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewLinkConverterResponse {
    String goodsName;
    private String price;
    private String marketPrice;
    private String discount;
    private String commissionRate;
    private String commission;
    private Map<String,String> couponInfo;
    private Map<String,String> we_app_info;
    private String url;

    public static NewLinkConverterResponse converToResponse(JSONObject json) {
        JSONObject data = json.getJSONObject("data");
        if (data == null) {
            return new NewLinkConverterResponse();
        }
        NewLinkConverterResponse response = new NewLinkConverterResponse();
        response.goodsName = data.getString("goodsName");
        response.price = data.getString("price");
        response.marketPrice = data.getString("marketPrice");
        response.discount = data.getString("discount");
        response.commissionRate = data.getString("commissionRate");
        response.commission = data.getString("commission");
        response.couponInfo = data.getJSONObject("couponInfo") != null ? data.getJSONObject("couponInfo").toJavaObject(Map.class) : null;
        response.we_app_info = data.getJSONObject("we_app_info") != null ? data.getJSONObject("we_app_info").toJavaObject(Map.class) : null;
        response.url = data.getString("url");
        return response;
    }
}

