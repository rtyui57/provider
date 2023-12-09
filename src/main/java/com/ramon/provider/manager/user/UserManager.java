package com.ramon.provider.manager.user;

import com.ramon.provider.exceptions.ResourceNotFoundException;
import com.ramon.provider.manager.CommonManager;
import com.ramon.provider.model.Horario;
import com.ramon.provider.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class UserManager {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected CommonManager commonManager;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User find(String id) {
        User user = userRepository.findByUsername(id);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return user;
    }

    public void deleteAll() {
        userRepository.findAll().forEach(user -> commonManager.removeUser(user));
    }

    public void deleteUser(String id) {
        User user = userRepository.findById(id).get();
        commonManager.removeUser(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void importUser(User newUser) {
        if (ObjectUtils.isEmpty(newUser.getUsername())) {
            throw new RuntimeException("User must have name");
        }
        User user;
        User opt = userRepository.findByUsername(newUser.getUsername());
        if (opt == null) {
            user = new User();
            user.setCreationDate(new Date());
            user.setUsername(newUser.getUsername());
        } else {
            user = opt;
        }
        user.setEmail(newUser.getEmail());
        user.setDescription(newUser.getDescription());
        user.setModificationDate(new Date());
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setIcon(newUser.getIcon());
        user.setPassword(newUser.getPassword());
        user.setPuesto(newUser.getPuesto());
        userRepository.save(user);
    }

    public List<Horario> getHorarios(String id) {
        return find(id).getHorarios();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && Objects.equals(user.getPassword(), password)) {
            return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        }
        throw new RuntimeException("Username or password invalid");
    }
}
