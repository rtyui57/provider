package com.ramon.provider.converters;

import com.ramon.provider.model.Aula;
import com.ramon.provider.model.Building;
import com.ramon.provider.rs.entity.RSAula;
import com.ramon.provider.rs.entity.RSBuilding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BuildingConverter {

    @Autowired
    protected HorarioConverter horarioConverter;

    public RSBuilding convert(Building building) {
        RSBuilding result = new RSBuilding();
        result.setId(building.getId());
        result.setName(building.getName());
        result.setLocation(building.getLocation());
        result.setAulas(convertAulas(building.getAulas()));
        return result;
    }

    public List<RSBuilding> convert(List<Building> buildings) {
        List<RSBuilding> res = new ArrayList<>();
        buildings.forEach(h -> res.add(convert(h)));
        return res;
    }

    public RSAula convert(Aula aula) {
        RSAula res = new RSAula();
        res.setId(aula.getId());
        res.setCapacity(aula.getCapacity());
        res.setName(aula.getName());
        res.setHorarios(horarioConverter.convert(aula.getHorarios()));
        return res;
    }

    public List<RSAula> convertAulas(List<Aula> aula) {
        List<RSAula> res = new ArrayList<>();
        aula.forEach(h -> res.add(convert(h)));
        return res;
    }
}
