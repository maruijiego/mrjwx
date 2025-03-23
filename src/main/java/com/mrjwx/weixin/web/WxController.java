package com.mrjwx.weixin.web;

import com.mrjwx.weixin.bo.DtkLinkConverterResponse;
import com.mrjwx.weixin.bo.LinkConverterResponse;
import com.alibaba.fastjson2.JSONObject;
import com.mrjwx.weixin.bo.NewLinkConverterResponse;
import com.mrjwx.weixin.util.CheckUtil;
import com.mrjwx.weixin.util.MathUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader; // 添加缺失的导入语句
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.Objects;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

@RestController
@Log4j2
public class WxController {
    @GetMapping("/hello")
    public String hello() {
        log.info("hello");
        return "hello";
    }

    @GetMapping("/")
    public String check(String signature, String timestamp, String nonce, String echostr) {
        log.info("singnature, timestamp, nonce, echostr: {}, {}, {}, {}", signature, timestamp, nonce, echostr);
//        验签
        if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }
        return null;
    }

    @PostMapping("/")
    public String recevieMessage(HttpServletRequest request) {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder xml = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                xml.append(line);
            }
            // 解析 XML 消息
// 添加缺失的导入语句
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
// 添加缺失的导入语句
            DocumentBuilder builder = factory.newDocumentBuilder();
// 添加缺失的导入语句
            InputSource is = new InputSource(new StringReader(xml.toString()));
// 添加缺失的导入语句
            Document doc = builder.parse(is);
            // 提取消息内容
            Element root = doc.getDocumentElement();
            String fromUserName = root.getElementsByTagName("FromUserName").item(0).getTextContent();
            String toUserName = root.getElementsByTagName("ToUserName").item(0).getTextContent();
            String msgType = root.getElementsByTagName("MsgType").item(0).getTextContent();
            String content = null;
            if ("text".equals(msgType)) {
                content = root.getElementsByTagName("Content").item(0).getTextContent();
            }
            log.info("接收到来自 {} 的 {} 消息: {}, 准备按照微信规范返回消息", fromUserName, msgType, content);
            if(content.length()<1){
                String finalResponseXml = String.format("<xml>\n" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>\n" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>\n" +
                        "<CreateTime>%d</CreateTime>\n" +
                        "<MsgType><![CDATA[text]]></MsgType>\n" +
                        "<Content><![CDATA[%s]]></Content>\n" +
                        "</xml>", fromUserName, toUserName, System.currentTimeMillis(), "暂无该商品哦亲亲~");
                return finalResponseXml;

            }

            // 假设接收到的content是用户提供的购物链接
            LinkConverterResponse result = LinkConverter.convertLink(content);
            log.info("接口返回结果: {}", JSONObject.toJSONString(result));
            if (Objects.nonNull(result.getGoodsId())) {
                NewLinkConverterResponse newResponse = NewLinkConverter.convertLink(result.getGoodsId(), "test", result.getSource(), "1", "1");

                log.info("接口返回结果: {}", JSONObject.toJSONString(newResponse));
                String outp = String.format("返利金额 %s,下单链接 %s", newResponse.getCommission(), newResponse.getUrl());


                // 将接口返回数据按照微信消息的 XML 格式封装
                String finalResponseXml = String.format("<xml>\n" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>\n" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>\n" +
                        "<CreateTime>%d</CreateTime>\n" +
                        "<MsgType><![CDATA[text]]></MsgType>\n" +
                        "<Content><![CDATA[%s]]></Content>\n" +
                        "</xml>", fromUserName, toUserName, System.currentTimeMillis(), outp);
                return finalResponseXml;
            }
            DtkLinkConverterResponse dtkLinkConverterResponse = DtkLinkConverter.convertLink(content);
            if (Objects.nonNull(dtkLinkConverterResponse) && Objects.nonNull(dtkLinkConverterResponse.getGoodsId())) {
                String outp = String.format("返利金额 %s,下单链接 %s", MathUtil.calculateCommission(dtkLinkConverterResponse.getReal_post_fee(), dtkLinkConverterResponse.getCommissionRate()), dtkLinkConverterResponse.getCpsFullTpwd());
                // 将接口返回数据按照微信消息的 XML 格式封装
                String finalResponseXml = String.format("<xml>\n" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>\n" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>\n" +
                        "<CreateTime>%d</CreateTime>\n" +
                        "<MsgType><![CDATA[text]]></MsgType>\n" +
                        "<Content><![CDATA[%s]]></Content>\n" +
                        "</xml>", fromUserName, toUserName, System.currentTimeMillis(), outp);
                return finalResponseXml;
            }
// 将接口返回数据按照微信消息的 XML 格式封装
            String finalResponseXml = String.format("<xml>\n" +
                    "<ToUserName><![CDATA[%s]]></ToUserName>\n" +
                    "<FromUserName><![CDATA[%s]]></FromUserName>\n" +
                    "<CreateTime>%d</CreateTime>\n" +
                    "<MsgType><![CDATA[text]]></MsgType>\n" +
                    "<Content><![CDATA[%s]]></Content>\n" +
                    "</xml>", fromUserName, toUserName, System.currentTimeMillis(), "暂无该商品哦亲亲~");
            return finalResponseXml;


        } catch (Exception e) {
            log.error("调用接口时出错", e);
            return "error";
        }
    }
}




