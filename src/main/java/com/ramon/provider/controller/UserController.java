package com.ramon.provider.controller;

import com.ramon.provider.converters.HorarioConverter;
import com.ramon.provider.converters.UserConverter;
import com.ramon.provider.manager.user.UserManager;
import com.ramon.provider.model.User;
import com.ramon.provider.rs.entity.RSHorario;
import com.ramon.provider.rs.entity.RSUser;
import com.ramon.provider.security.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Transactional
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    protected UserManager userManager;

    @Autowired
    protected UserConverter userConverter;

    @Autowired
    protected SecurityManager securityManager;

    @Autowired
    protected HorarioConverter horarioConverter;

    @PostMapping(path = "/auth")
    public String login(@RequestBody RSUser user) {
        return securityManager.authenticate(user);
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
    public List<RSHorario> getHorario(@PathVariable String id) {
        return horarioConverter.convert(userManager.getHorarios(id));
    }

    @DeleteMapping
    public void deleteAll() {
        userManager.deleteAll();
    }
}

