package com.xiaoban.xiaoi.utils;

import com.xiaoban.xiaoi.model.TentcentChatConfig;
import com.xiaoban.xiaoi.model.TentcentChatResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author xiaoban
 * @version 1.0.0
 * @create 2021/04/12 - 22:25
 */
public class XiaoIUtil {
    /** 闲聊 */
   public static final String NLP_URL = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat";

    public static void main(String[] args) throws UnsupportedEncodingException {
        String appId = "2160281757";
        String appKey = "HUj9gsE5IEkYEarc";
        TentcentChatConfig ttConfig = new TentcentChatConfig();
        ttConfig.setAppId(appId);
        ttConfig.setAppKey(appKey);
        String question = "你的好朋友";
        smallTalk(question,ttConfig);
    }

    public static<T> TentcentChatResult<T> smallTalk(String question, TentcentChatConfig config) throws UnsupportedEncodingException {
        String timeStamp = System.currentTimeMillis()/1000+"";
        String nonceStr = System.currentTimeMillis()/10000+"";
        TreeMap<String,String> params = new TreeMap<>();
        params.put("app_id",config.getAppId());
        params.put("time_stamp",timeStamp);
        params.put("nonce_str",nonceStr);
        params.put("session",nonceStr);
        params.put("question",URLEncoder.encode(question,"utf-8"));
        String sign= signature(params,config.getAppKey());
        params.put("sign",sign);
        params.put("question",question);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            multiValueMap.add(entry.getKey(),entry.getValue());
        }
        HttpEntity<Object> httpEntity = new HttpEntity<>(multiValueMap,httpHeaders);
       try{
           ResponseEntity<TentcentChatResult> exchange = restTemplate.exchange(NLP_URL, HttpMethod.POST, httpEntity, TentcentChatResult.class);
           System.out.println(httpEntity.getBody());
           System.out.println(exchange.getStatusCode());
           System.out.println(exchange.getBody());
           return exchange.getBody();
       }catch (HttpClientErrorException e){
           System.out.println(httpEntity);
       }
       return null;


    }



    private final List<HttpMessageConverter<?>> converters = new RestTemplate().getMessageConverters();
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");


    /**
     * 签名
     *
     * @param params 参数个数
     * @param appKey 应用的关键
     * @return {@link String}
     */
    public static String signature(TreeMap<String,String> params, String appKey) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        builder.append("app_key=").append(appKey);
        return DigestUtils.md5Hex(builder.toString().getBytes(StandardCharsets.UTF_8)).toUpperCase();
    }


}
