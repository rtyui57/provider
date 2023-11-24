package com.ramon.provider.controller;

import com.ramon.provider.manager.building.BuildingManager;
import com.ramon.provider.model.Aula;
import com.ramon.provider.model.Building;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/building")
public class BuildingController {

    @Autowired
    protected BuildingManager manager;

    @GetMapping
    public List<Building> list() {
        return manager.list();
    }

    @PostMapping
    public void importBuilding(@RequestBody Building building) {
        manager.importBuilding(building);
    }

    @GetMapping("/{id}")
    public Building find(@PathVariable String id) {
        return manager.find(id);
    }

    @DeleteMapping
    public void deleteAll() {
        manager.deleteAll();
    }

    @PostMapping("/{id}/classroom")
    public void createClassroom(@PathVariable String id, @RequestBody Aula aula) {
        manager.importClassroom(id, aula);
    }

    @GetMapping("/classrooms")
    public Map<String, String> displayClassrooms() {
        return manager.getClassrooms();
    }
}
