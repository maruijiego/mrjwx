package com.mrjwx.weixin.bo;

import com.alibaba.fastjson2.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkConverterResponse {
    private String goodsName;
    private String goodsThumbUrl;
    private String marketPrice;
    private String price;
    private String discount;
    private String goodsId;
    private String commissionRate;
    private String commission;
    private String shopName;
    private String source;


    public static LinkConverterResponse converToResponse(JSONObject json) {
        JSONObject data = json.getJSONObject("data");
        if (data == null) {
            return LinkConverterResponse.builder().build();
        }
        return LinkConverterResponse.builder()
                .goodsName(json.getJSONObject("data").getString("goodsName"))
                .goodsThumbUrl(json.getJSONObject("data").getString("goodsThumbUrl"))
                .marketPrice(json.getJSONObject("data").getString("marketPrice"))
                .price(json.getJSONObject("data").getString("price"))
                .discount(json.getJSONObject("data").getString("discount"))
                .goodsId(json.getJSONObject("data").getString("goodsId"))
                .commissionRate(json.getJSONObject("data").getString("commissionRate"))
                .commission(json.getJSONObject("data").getString("commission"))
                .shopName(json.getJSONObject("data").getString("shopName"))
                .source(json.getJSONObject("data").getString("source"))
                .build();
    }
}