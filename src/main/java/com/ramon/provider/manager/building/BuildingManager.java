package com.ramon.provider.manager.building;

import com.ramon.provider.manager.CommonManager;
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

    @Autowired
    protected CommonManager commonManager;

    @Autowired
    protected AulaManager aulaManager;

    public List<Building> list() {
        return repo.findAll();
    }

    public Building find(String id) {
        return repo.findById(id).get();
    }

    public void delete(String id) {
        commonManager.removeBuilding(find(id));
    }

    public void delete(Building building) {
        repo.delete(building);
    }

    public void importBuilding(Building newBuilding) {
        newBuilding.setId(newBuilding.getName().replace(" ", "_"));
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
        repo.findAll().forEach(building -> commonManager.removeBuilding(building));
    }

    public void importClassroom(String id, Aula aula) {
        Building building = find(id);
        aula.setId(id + "--" + aula.getName().replace(" ", "_"));
        building.getAulas().add(aula);
        aula.setBuilding(building);
        aulaManager.saveAula(aula);
        repo.save(building);
    }

    public Map<String, String> getClassrooms() {
        Map<String, String> classrooms = new HashMap<>();
        for (Building building : repo.findAll()) {
            for (Aula aula : building.getAulas()) {
                classrooms.put(aula.getId(), building.getId());
            }
        }
        return classrooms;
    }

    public void saveBuilding(Building building) {
        repo.save(building);
    }
}
