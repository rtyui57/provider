package com.ramon.provider.controller;

import com.ramon.provider.manager.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device")
public class CacheController {

    @Autowired
    protected CacheManager cacheManager;

}
