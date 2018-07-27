package com.ashik619.testproject.controller;


import com.ashik619.testproject.models.Group;
import com.ashik619.testproject.models.HttpRespMessage;
import com.ashik619.testproject.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    GroupRepository groupRepository;


    @GetMapping("/getAll")
    @ResponseBody
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> teamList = groupRepository.findAll();
        return new  ResponseEntity<>(teamList,HttpStatus.OK );
    }

    @PostMapping("/addNew")
    @ResponseBody
    public ResponseEntity<HttpRespMessage> addGroup(@RequestBody Map<String, String> body) {
        Group group = new Group(body.get("name"));
        try {
            groupRepository.save(group);
            return new ResponseEntity<>(new HttpRespMessage("Team Added"),HttpStatus.OK);
        }catch (DataAccessException e){
            e.printStackTrace();
            return new ResponseEntity<>(new HttpRespMessage(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<HttpRespMessage> deleteGroup(@PathVariable(value = "id") Long id){
        Optional<Group> groupOptional = groupRepository.findById(id);
        if(!groupOptional.isPresent()){
            return new ResponseEntity<>(new HttpRespMessage("group not found"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            groupRepository.deleteById(id);
            return new ResponseEntity<>(new HttpRespMessage("group deleted"),HttpStatus.OK);
        }catch (DataAccessException e){
            e.printStackTrace();
            return new ResponseEntity<>(new HttpRespMessage(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
