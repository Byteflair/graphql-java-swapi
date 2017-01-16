package com.byteflair.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by calata on 23/12/16.
 */
@Component
public class RestService {

    @Value("${api.server.base_uri}")
    private String baseApiUri;

    private RestTemplate restTemplate = new RestTemplate();

    public Object restRequest(String uri, Boolean relative){
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> parameters = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "swapi-Java-SWAPI-JAVA");
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = null;

        try {
            if (relative){
                uri = baseApiUri + uri;
            }

            response = restTemplate.exchange(uri,
                                             HttpMethod.GET,
                                             httpEntity,
                                             Map.class,
                                             parameters);
        } catch (RestClientException rce) {
            // log TODO
        }
        return response.getBody();
    }

}
