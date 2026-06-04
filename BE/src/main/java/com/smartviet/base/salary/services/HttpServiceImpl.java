package com.smartviet.base.salary.services;

import com.smartviet.base.salary.services.iservice.HttpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class HttpServiceImpl implements HttpService {

    private final RestTemplate restTemplate;

    @Autowired
    public HttpServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public <T> T get(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

    @Override
    public <T> ResponseEntity<T> getEntity(String url, Class<T> responseType) {
        return restTemplate.getForEntity(url, responseType);
    }

    @Override
    public <T> T post(String url, Object requestBody, Class<T> responseType) {
        return restTemplate.postForObject(url, requestBody, responseType);
    }

    @Override
    public <T> ResponseEntity<T> postEntity(String url, Object requestBody, Class<T> responseType) {
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(requestBody),
                responseType
        );
    }

    @Override
    public <T> T put(String url, Object requestBody, Class<T> responseType) {
        ResponseEntity<T> resp = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(requestBody),
                responseType
        );
        return resp.getBody();
    }

    @Override
    public <T> ResponseEntity<T> putEntity(String url, Object requestBody, Class<T> responseType) {
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(requestBody),
                responseType
        );
    }

    @Override
    public <T> T patch(String url, Object requestBody, Class<T> responseType) {
        ResponseEntity<T> resp = restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                new HttpEntity<>(requestBody),
                responseType
        );
        return resp.getBody();
    }

    @Override
    public <T> ResponseEntity<T> patchEntity(String url, Object requestBody, Class<T> responseType) {
        return restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                new HttpEntity<>(requestBody),
                responseType
        );
    }

    @Override
    public <T> T delete(String url, Class<T> responseType) {
        ResponseEntity<T> resp = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                responseType
        );
        return resp.getBody();
    }

    @Override
    public <T> ResponseEntity<T> deleteEntity(String url, Class<T> responseType) {
        return restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                responseType
        );
    }

}
