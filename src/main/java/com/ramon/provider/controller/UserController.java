package com.ramon.provider.controller;

import com.ramon.provider.manager.CustomerManager;
import com.ramon.provider.manager.UserManager;
import com.ramon.provider.model.Customer;
import com.ramon.provider.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    protected UserManager userManager;

    @Autowired
    protected CustomerManager customerManager;

    @PostMapping(path = "/auth")
    public String login(@RequestBody Map<String, Object> map) {
        return "customer";
    }

    @GetMapping("/list")
    public List<User> listUsers(@RequestHeader(required = false) String customer) {
        return userManager.findByCustomer(customer);
    }

    @PostMapping
    public void importUser(@RequestBody User user) {
        userManager.importUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userManager.deleteUser(id);
    }

    @GetMapping("/customer/list")
    public List<Customer> listCustomers() {
        return customerManager.findAll();
    }

    @PostMapping("/customer")
    public void importCustomer(@RequestBody Customer customer) {
        customerManager.importCustomer(customer);
    }


    @DeleteMapping("/customer/{id}")
    public void deleteCustomer(@PathVariable String id) {
        customerManager.delete(id);
    }

    @GetMapping("/customer/{id}")
    public Customer findCustomer(@PathVariable String id) {
        return customerManager.find(id);
    }

    @DeleteMapping("/customer/all")
    public void deleteAllCustomers() {
        customerManager.deleteAll();
    }
}
