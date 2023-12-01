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
        return res;
    }

    public List<RSUser> convert(List<User> users) {
        List<RSUser> res = new ArrayList<>();
        users.forEach(h -> res.add(convert(h)));
        return res;
    }
}
