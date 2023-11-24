package com.ramon.provider.controller;

import com.ramon.provider.manager.horario.HorarioManager;
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
    protected HorarioManager horarioManager;

    @PostMapping(path = "/auth")
    public Map<String, Object> login(@RequestBody RSUser user) {
        return Map.of("username", user.getUser(), "customer", "customer");
    }

    @GetMapping("/list")
    public List<User> listUsers() {
        return userManager.findAll();
    }


    @GetMapping
    public User findUser(@RequestParam String id) {
        return userManager.find(id);
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

    @PostMapping("/{id}")
    public void addAsignatura(@PathVariable String id, @RequestParam String asginatura) {
        userManager.addAsignatura(id, asginatura);
    }
}

