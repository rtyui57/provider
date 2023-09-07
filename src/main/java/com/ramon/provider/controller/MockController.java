package com.ramon.provider.controller;

import com.ramon.provider.model.Post;
import com.ramon.provider.repository.PostRepository;
///import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/mock")
public class MockController
{

    @Autowired
    protected PostRepository postRepository;

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    @GetMapping(path = "/greetings")
    public String s() throws IOException, TimeoutException {
        rabbitTemplate.convertAndSend("spring-boot-exchange", "foo.bar.hola.hola", "sdg");
        return "Hola";

    }

    @PostMapping(path = "/create")
    public void creater(@RequestParam String name) {
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
}
