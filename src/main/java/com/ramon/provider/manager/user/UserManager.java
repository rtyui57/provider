package com.ramon.provider.manager.user;

import com.ramon.provider.manager.CommonManager;
import com.ramon.provider.model.Horario;
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

    @Autowired
    protected CommonManager commonManager;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User find(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.get();
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
        Optional<User> opt = userRepository.findById(newUser.getUsername());
        if (opt.isEmpty()) {
            user = new User();
            user.setCreationDate(new Date());
            user.setUsername(newUser.getUsername());
        } else {
            user = opt.get();
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
}
