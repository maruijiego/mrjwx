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
public class DtkLinkConverterResponse {
    private String goodsId;
    private String originUrl;
    private String originType;
    private String originInfo;
    private String title;
    private String shopName;
    private String shopLogo;
    private String image;
    private String startTime;
    private String endTime;
    private String amount;
    private String startFee;
    private String price;
    private String activityId;
    private String pid;
    private String status;
    private String itemId;
    private String itemName;
    private String mainPic;
    private String dataType;
    private String couponSrcScene;
    private String itemLink;
    private String couponLink;
    private String bizSceneId;
    private String real_post_fee;
    private String cpsLongUrl;
    private String cpsFullTpwd;
    private String shortUrl;
    private String shortTpwd;
    private String sellerId;
    private String commissionRate;
    private String commissionType;

    public static DtkLinkConverterResponse converToResponse(JSONObject json) {
        if (json == null) {
            return new DtkLinkConverterResponse();

        }


        JSONObject data = json.getJSONObject("data");
        if (data == null) {
            return new DtkLinkConverterResponse();
        }
        DtkLinkConverterResponse response = new DtkLinkConverterResponse();
        response.goodsId = data.getString("goodsId");
        response.originUrl = data.getString("originUrl");
        response.originType = data.getString("originType");
        response.originInfo = data.getString("originInfo");
        response.title = data.getString("title");
        response.shopName = data.getString("shopName");
        response.shopLogo = data.getString("shopLogo");
        response.image = data.getString("image");
        response.startTime = data.getString("startTime");
        response.endTime = data.getString("endTime");
        response.amount = data.getString("amount");
        response.startFee = data.getString("startFee");
        response.price = data.getString("price");
        response.activityId = data.getString("activityId");
        response.pid = data.getString("pid");
        response.status = data.getString("status");
        response.itemId = data.getString("itemId");
        response.itemName = data.getString("itemName");
        response.mainPic = data.getString("mainPic");
        response.dataType = data.getString("dataType");
        response.couponSrcScene = data.getString("couponSrcScene");
        response.itemLink = data.getString("itemLink");
        response.couponLink = data.getString("couponLink");
        response.bizSceneId = data.getString("bizSceneId");
        response.real_post_fee = data.getString("realPostFee");
        response.cpsLongUrl = data.getString("cpsLongUrl");
        response.cpsFullTpwd = data.getString("cpsFullTpwd");
        response.shortUrl = data.getString("shortUrl");
        response.shortTpwd = data.getString("shortTpwd");
        response.sellerId = data.getString("sellerId");
        response.commissionRate = data.getString("commissionRate");
        response.commissionType = data.getString("commissionType");
        return response;
    }
}