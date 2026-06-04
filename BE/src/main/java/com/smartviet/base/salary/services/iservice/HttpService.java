package com.smartviet.base.salary.services.iservice;

import org.springframework.http.ResponseEntity;

public interface HttpService {

    <T> T get(String url, Class<T> responseType);

    <T> ResponseEntity<T> getEntity(String url, Class<T> responseType);

    <T> T post(String url, Object requestBody, Class<T> responseType);

    <T> ResponseEntity<T> postEntity(String url, Object requestBody, Class<T> responseType);

    <T> T put(String url, Object requestBody, Class<T> responseType);

    <T> ResponseEntity<T> putEntity(String url, Object requestBody, Class<T> responseType);

    <T> T patch(String url, Object requestBody, Class<T> responseType);

    <T> ResponseEntity<T> patchEntity(String url, Object requestBody, Class<T> responseType);

    <T> T delete(String url, Class<T> responseType);

    <T> ResponseEntity<T> deleteEntity(String url, Class<T> responseType);

}
