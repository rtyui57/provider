package com.ramon.provider.controller;

import com.ramon.provider.manager.DeviceRegisterManager;
import com.ramon.provider.model.Post;
import com.ramon.provider.manager.repository.PostRepository;
///import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.ramon.provider.rs.converter.DeviceRegisterConverter;
import com.ramon.provider.rs.entity.RSDeviceRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/mock")
public class MockController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected PostRepository postRepository;

    @Autowired
    protected DeviceRegisterManager deviceRegisterManager;

    @Autowired
    protected DeviceRegisterConverter deviceRegisterConverter;

    @GetMapping(path = "/greetings")
    public String s() {
        return "Hola";

    }

    @PostMapping(path = "/create")
    public void creater(@RequestParam(required = false, defaultValue = "default") String name, @RequestBody Map<String, Object> map) {
        Post post = new Post();
        post.setName(name);
        post.setCreationDate(new Date());
        post.setCategory("");
        postRepository.save(post);
    }

    @GetMapping("/find")
    public Post find(@RequestParam String name) {
        return postRepository.findItemByName(name);
    }

    @PostMapping("/register")
    public void addRegistro(@RequestBody RSDeviceRegister register) {
        deviceRegisterManager.create( deviceRegisterConverter.convert(register));
    }
}
