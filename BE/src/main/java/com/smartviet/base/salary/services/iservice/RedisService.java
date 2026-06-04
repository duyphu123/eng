//package com.smartviet.base.salary.services.iservice;
//
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
//public interface RedisService {
//
//    // —— Key-Value operations ——
//    /**
//     * Lưu value (Object) với key; mặc định không hết hạn.
//     */
//    void set(String key, Object value);
//
//    /**
//     * Lưu value với key và TTL.
//     * @param timeout thời gian tồn tại
//     * @param unit    đơn vị thời gian
//     */
//    void set(String key, Object value, long timeout, TimeUnit unit);
//
//    /**
//     * Lấy về object kiểu T (cần cast ngầm), hoặc null nếu không có.
//     */
//    <T> T get(String key, Class<T> clazz);
//
//    /**
//     * Xoá key khỏi Redis.
//     * @return true nếu key tồn tại và đã bị xoá
//     */
//    boolean delete(String key);
//
//    /**
//     * Kiểm tra key có tồn tại hay không.
//     */
//    boolean hasKey(String key);
//
//    // —— Hash operations ——
//    /**
//     * HSET key hKey = value
//     */
//    void hSet(String key, String hashKey, Object value);
//
//    /**
//     * HGET key hKey
//     */
//    <T> T hGet(String key, String hashKey, Class<T> clazz);
//
//    /**
//     * Xoá các hashKeys khỏi key
//     */
//    void hDelete(String key, String... hashKeys);
//
//    /**
//     * Lấy toàn bộ hashKeys trong key
//     */
//    Set<String> hKeys(String key);
//
//    // —— Counters ——
//    /**
//     * INCRBY key delta
//     */
//    long incr(String key, long delta);
//
//    /**
//     * DECRBY key delta
//     */
//    long decr(String key, long delta);
//
//}
