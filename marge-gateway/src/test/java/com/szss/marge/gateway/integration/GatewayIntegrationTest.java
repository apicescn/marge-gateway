package com.szss.marge.gateway.integration;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.szss.marge.common.util.GatewayUtils;

/**
 * @author XXX
 * @date 2018/7/5
 */
public class GatewayIntegrationTest {

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testGetSign() throws Exception {
        Long timestamp = System.currentTimeMillis() / 1000;
        Map<String, String> query = new HashMap<>();
        query.put("app_id", "test");
        query.put("session_key", "2134");
        query.put("timestamp", timestamp + "");
        query.put("http_method", "GET");
        query.put("request_uri", "/middle/api/v1/warehouse/list");
        query.put("sign_method", "md5");
//        query.put("pageIndex", "1");
//        query.put("pageSize", "10");
        String sign = GatewayUtils.signRequest(query, "1489f2313cbd23412950", GatewayUtils.MD5);
        String url = "http://192.168.10.33:8080/gateway/router/middle/api/v1/warehouse/list?app_id=test&session_key=2134&timestamp="
            + timestamp + "&sign_method=md5&sign=" + sign;// + "&pageIndex=1&pageSize=10"
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("accept", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set("content-type", MediaType.APPLICATION_JSON_VALUE);
        // httpHeaders.set("Authorization","Bearer
        // eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsibWFyZ2UtYXV0aC1zZXJ2ZXIiLCJwYW50aGVyLW1lbWJlciIsInBhbnRoZXItd2VjaGF0IiwibWFyZ2UtYWRtaW4tc2VydmVyIiwicGFudGhlci1vcGVuYXBpIiwicGFudGhlci1mZWVkY2FyZCIsInBhbnRoZXItb3JkZXIiXSwiZXhwIjoxNjI1MzMwMTA4LCJ1c2VyX25hbWUiOiI1NTQ3QzkzNy0wQjQ2LTQ3MzktODM4Ni1CMDk0RTkzMzg3RjciLCJqdGkiOiIzZjQ3OTAyMC1mMGUwLTRjM2YtYjk3MC1jNDE3MmUwYWFlYjMiLCJjbGllbnRfaWQiOiJ0ZXN0Iiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl19.oRAHOcXfNY32qRvlBKBuMYta0t9v0Cq8DIZe_pu3IwE");
        HttpEntity entity = new HttpEntity(httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println(response.toString());
    }

    @Test
    public void testPostSign() throws Exception {
        Long timestamp = System.currentTimeMillis() / 1000;
        Map<String, String> query = new HashMap<>();
        query.put("app_id", "test");
        query.put("session_key", "2134");
        query.put("timestamp", timestamp + "");
        query.put("http_method", "POST");
        query.put("request_uri", "/middle/api/v1/product/update");
        query.put("sign_method", "md5");
        query.put("id", "1");
        String sign = GatewayUtils.signRequest(query, "1489f2313cbd23412950", GatewayUtils.MD5);
        String url
            = "http://127.0.0.1:8080/router/middle/api/v1/product/update?app_id=test&session_key=2134&timestamp="
                + timestamp + "&sign_method=md5&sign=" + sign;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("accept", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set("content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        // httpHeaders.set("Authorization","Bearer
        // eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsibWFyZ2UtYXV0aC1zZXJ2ZXIiLCJwYW50aGVyLW1lbWJlciIsInBhbnRoZXItd2VjaGF0IiwibWFyZ2UtYWRtaW4tc2VydmVyIiwicGFudGhlci1vcGVuYXBpIiwicGFudGhlci1mZWVkY2FyZCIsInBhbnRoZXItb3JkZXIiXSwiZXhwIjoxNjI1MzMwMTA4LCJ1c2VyX25hbWUiOiI1NTQ3QzkzNy0wQjQ2LTQ3MzktODM4Ni1CMDk0RTkzMzg3RjciLCJqdGkiOiIzZjQ3OTAyMC1mMGUwLTRjM2YtYjk3MC1jNDE3MmUwYWFlYjMiLCJjbGllbnRfaWQiOiJ0ZXN0Iiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl19.oRAHOcXfNY32qRvlBKBuMYta0t9v0Cq8DIZe_pu3IwE");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("id", "1");
        HttpEntity entity = new HttpEntity(params, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(response.toString());
    }
}
