package com.gpdata.wanyou.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by chengchao on 16-10-14.
 */
public class RestTemplateUtil {

    public static final String executePost(String url, Map<String, Object> params) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, params, String.class);
        return responseEntity.getBody();
    }

}
