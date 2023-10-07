package com.ramon.provider.manager;

import com.ramon.provider.manager.repository.UserRepository;
import com.ramon.provider.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class UserManager {

    @Autowired
    protected UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByCustomer(String customer) {
        return userRepository.findByCustomer(customer);
    }

    public User find(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.get();
    }

    public void deleteUser(String id) {
        userRepository.delete(find(id));
    }

    public void importUser(User newUser) {
        if (ObjectUtils.isEmpty(newUser.getUsername())) {
            throw new RuntimeException("User must have name");
        }
        User user;
        Optional<User> opt = userRepository.findById(newUser.getUsername());
        if (opt.isEmpty()) {
            user = new User();
            user.setCreationDate(new Date());
            user.setUsername(newUser.getUsername());
        } else {
            user = opt.get();
        }
        user.setCustomer(newUser.getCustomer());
        user.setEmail(newUser.getEmail());
        user.setDescription(newUser.getDescription());
        user.setModificationDate(new Date());
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setIcon(newUser.getIcon());
        user.setPassword(newUser.getPassword());
        userRepository.save(user);
        //userRepository.findAll();
    }
}
