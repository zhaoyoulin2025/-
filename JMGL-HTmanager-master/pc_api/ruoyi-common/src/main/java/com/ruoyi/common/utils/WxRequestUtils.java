package com.ruoyi.common.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.http.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class WxRequestUtils {

    private final static String LOGINURL  = "https://api.weixin.qq.com/sns/jscode2session";

    private final static String ACCESSTOKENURL  = "https://api.weixin.qq.com/cgi-bin/token";
    private final static String PHONEURL  = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=";
    private final static String WXCHECKCONTENT  = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=";
    private final static String WXCHECKMEDIA  = "https://api.weixin.qq.com/wxa/media_check_async?access_token=";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 你自己的微信小程序APPID
     */

    @Value("${wxApi.AppID}")
    private String AppID ;

    @Value("${wxApi.AppSecret}")
    private String AppSecret;

    @Autowired
    private RedisCache redisCache;

    public String getToken(){

        String accessToken = redisCache.getCacheObject("ACCESS_TOKEN");
        if (StringUtils.isBlank(accessToken)){
            String params = StrUtil.format("grant_type=client_credential&appid={}&secret={}", AppID, AppSecret);
            //开始发起网络请求,若依管理系统自带网络请求工具，直接使用即可
            String res = HttpUtils.sendGet(ACCESSTOKENURL,params);
            JSONObject jsonObject = JSON.parseObject(res);
            try {
                accessToken = jsonObject.getString("access_token");
                redisCache.setCacheObject("ACCESS_TOKEN",accessToken,100,TimeUnit.MINUTES);
            }catch (Exception e){
                accessToken = "";
            }


        }
        return accessToken;

    }

    public String getWxPhone(String code){
        String phoneNumber = "";
        String accessToken = getToken();
        if (StringUtils.isNotEmpty(accessToken)){
            try {
                // 使用前端code获取手机号码 参数为json格式
                String url =  PHONEURL + accessToken;
                Map<String, String> paramMap = new HashMap<>();
                paramMap.put("code", code);
                HttpHeaders headers = new HttpHeaders();
                HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(paramMap, headers);
                ResponseEntity<Map> response = restTemplate.postForEntity(url, httpEntity, Map.class);
                Map<String, Object> responseBody = response.getBody();

                if (responseBody != null && (Integer) responseBody.get("errcode") == 0) {
                    Map<String, Object> phoneInfo = (Map<String, Object>) responseBody.get("phone_info");
                    if (phoneInfo != null) {
                        phoneNumber = (String) phoneInfo.get("phoneNumber");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return phoneNumber;

    }

    public Boolean wxCheckContent(String openId,String content){
        try {
            String label = "";
            String accessToken = getToken();
            String url = WXCHECKCONTENT+accessToken;
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            //接口参数

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("content", content);
            paramMap.put("openid",openId);
            paramMap.put("scene",2);
            paramMap.put("version",2);
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, httpEntity, Map.class);
            //body
            Map<String, Object> result = response.getBody();
            System.out.println("接收到微信文本校验结果");
            System.out.println(result);
            if("0".equals(result.get("errcode").toString())){
                Map<String, Object> data = (Map<String, Object>) result.get("result");
                if (data != null) {
                    label = data.get("label").toString();
                }
            }
            if(!"100".equals(label)){
                return false;
            }else {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public String wxCheckMedia(String openId,String content,Integer type){
        String traceId = "";
        try {
            String accessToken = getToken();
            String url = WXCHECKMEDIA+accessToken;
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            //接口参数

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("media_url", content);
            paramMap.put("openid",openId);
            paramMap.put("scene",2);
            paramMap.put("version",2);
            paramMap.put("media_type",type);
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, httpEntity, Map.class);
            //body
            Map<String, Object> result = response.getBody();
            System.out.println(result);
            if("0".equals(result.get("errcode").toString())){
                traceId = result.get("trace_id").toString();

            }

        }catch (Exception e){
            e.printStackTrace();

        }
        return traceId;

    }

}
