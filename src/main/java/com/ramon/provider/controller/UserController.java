package com.ramon.provider.controller;

import com.ramon.provider.converters.UserConverter;
import com.ramon.provider.manager.user.UserManager;
import com.ramon.provider.model.Horario;
import com.ramon.provider.model.User;
import com.ramon.provider.rs.entity.RSUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Transactional
public class UserController {

    @Autowired
    protected UserManager userManager;

    @Autowired
    protected UserConverter userConverter;

    @PostMapping(path = "/auth")
    public Map<String, Object> login(@RequestBody RSUser user) {
        return Map.of("username", "", "customer", "customer");
    }

    @GetMapping("/list")
    public List<RSUser> listUsers() {
        return userConverter.convert(userManager.findAll());
    }

    @GetMapping
    public RSUser findUser(@RequestParam String id) {
        return userConverter.convert(userManager.find(id));
    }

    @PostMapping
    public void importUser(@RequestBody User user) {
        userManager.importUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userManager.deleteUser(id);
    }

    @GetMapping("/{id}/horario")
    public List<Horario> getHorario(@PathVariable String id) {
        return userManager.getHorarios(id);

    }

    @DeleteMapping
    public void deleteAll() {
        userManager.deleteAll();
    }
}

