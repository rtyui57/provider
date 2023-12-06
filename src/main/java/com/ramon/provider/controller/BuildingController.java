package com.ramon.provider.controller;

import com.ramon.provider.converters.BuildingConverter;
import com.ramon.provider.manager.building.BuildingManager;
import com.ramon.provider.model.Aula;
import com.ramon.provider.model.Building;
import com.ramon.provider.rs.entity.RSBuilding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/building")
public class BuildingController {

    @Autowired
    protected BuildingManager manager;

    @Autowired
    protected BuildingConverter converter;

    @GetMapping
    public List<RSBuilding> list() {
        return converter.convert(manager.list());
    }

    @PostMapping
    public void importBuilding(@RequestBody Building building) {
        manager.importBuilding(building);
    }

    @GetMapping("/{id}")
    public RSBuilding find(@PathVariable String id) {
        return converter.convert(manager.find(id));
    }

    @DeleteMapping
    public void deleteAll() {
        manager.deleteAll();
    }

    @DeleteMapping(path = "/{id}")
    public void deleteAll(@PathVariable String id) {
        manager.delete(id);
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
