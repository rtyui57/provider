package com.ramon.provider.manager.building;

import com.ramon.provider.model.Aula;
import com.ramon.provider.model.Building;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BuildingManager {

    @Autowired
    protected BuildingRepo repo;

    public List<Building> list() {
        return repo.findAll();
    }

    public Building find(String id) {
        return repo.findById(id).get();
    }

    public void delete(String id) {
        repo.deleteById(id);
    }

    public void importBuilding(Building newBuilding) {
        newBuilding.setId(newBuilding.getName());
        Building building;
        if (repo.findById(newBuilding.getId()).isEmpty()) {
            building = new Building();
            building.setId(newBuilding.getId());
        } else {
            building = repo.findById(newBuilding.getId()).get();
        }
        building.setLocation(newBuilding.getLocation());
        building.setName(newBuilding.getName());
        building.setAulas(newBuilding.getAulas());
        repo.save(building);
    }

    public void deleteAll() {
        repo.deleteAll();
    }

    public void importClassroom(String id, Aula aula) {
        Building building = find(id);
        aula.setId(id + "--" + aula.getName());
        building.getAulas().add(aula);
        repo.save(building);
    }

    public Map<String, String> getClassrooms() {
        Map<String, String> classrooms = new HashMap<>();
        for (Building building : repo.findAll()) {
            for (Aula aula : building.getAulas()) {
                classrooms.put(aula.getName(), building.getId());
            }
        }
        return classrooms;
    }

    public void saveBuilding(Building building) {
        repo.save(building);
    }
}
