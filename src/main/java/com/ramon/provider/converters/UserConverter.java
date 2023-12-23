package com.ramon.provider.converters;

import com.ramon.provider.model.Asignatura;
import com.ramon.provider.model.User;
import com.ramon.provider.rs.entity.RSAsignatura;
import com.ramon.provider.rs.entity.RSUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserConverter {

    public RSUser convert(User user) {
        RSUser res = new RSUser();
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
        res.setId(user.getUsername());
        res.setAsignaturas(user.getAsignaturas().stream().map(this::conv).toList());
        return res;
    }

    public List<RSUser> convert(List<User> users) {
        List<RSUser> res = new ArrayList<>();
        users.forEach(h -> res.add(convert(h)));
        return res;
    }

    public RSAsignatura conv(Asignatura asignatura) {
        RSAsignatura asignatura1 = new RSAsignatura();
        asignatura1.setName(asignatura.getName());
        asignatura1.setCurso(asignatura.getCurso());
        asignatura1.setGrado(asignatura.getGrado());
        return asignatura1;
    }
}
