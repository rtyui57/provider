package com.ramon.provider.manager.repository;

import org.springframework.stereotype.Component;

@Component
public class RedisRepo {

    protected static final String KEY = "DEVICE";
//
//    @Resource(name = "redisTemplate")
//    public HashOperations<String, String, Device> hashOperations;
//
//    public List<Device> listDevices() {
//        return hashOperations.values(KEY);
//    }
//
//    public void add(Device device) {
//        //hashOperations.put(KEY, device.getId(), device);
//    }

//    public void remove(String id) {
//        hashOperations.delete(KEY, id);
//    }
//
//    public void clear() {
//        Set<String> ids = hashOperations.keys(KEY);
//        for (String id : ids) {remove(id);
//        }
//    }
}
