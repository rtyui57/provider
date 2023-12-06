package com.ramon.provider.converters;

import com.ramon.provider.model.User;
import com.ramon.provider.rs.entity.RSUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserConverter {

    public RSUser convert(User user) {
        RSUser res = new RSUser();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setUsername(user.getUsername());
        res.setDescription(user.getDescription());
        res.setIcon(user.getIcon());
        res.setCreationDate(user.getCreationDate());
        res.setModificationDate(user.getModificationDate());
        res.setFirstName(user.getFirstName());
        res.setLastName(user.getLastName());
        res.setPassword(user.getPassword());
        res.setPuesto(user.getPuesto());
        return res;
    }

    public List<RSUser> convert(List<User> users) {
        List<RSUser> res = new ArrayList<>();
        users.forEach(h -> res.add(convert(h)));
        return res;
    }
}
