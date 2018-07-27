package com.ashik619.testproject.controller;


import com.ashik619.testproject.models.Group;
import com.ashik619.testproject.models.HttpRespMessage;
import com.ashik619.testproject.models.Team;
import com.ashik619.testproject.repositories.GroupRepository;
import com.ashik619.testproject.repositories.MatchRepository;
import com.ashik619.testproject.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    GroupRepository groupRepository;

    @GetMapping("/getAll")
    @ResponseBody
    public ResponseEntity<?> getAllTeams() {
        try {
            List<Team> teamList = teamRepository.findAll();
            return new  ResponseEntity<>(teamList,HttpStatus.OK );
        }catch (DataAccessException e){
            e.printStackTrace();
            return new ResponseEntity<>(new HttpRespMessage(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/addNew")
    @ResponseBody
    public ResponseEntity<HttpRespMessage> addTeam(@RequestBody Map<String, Object> body) {
        Optional<Group> groupOptional = groupRepository.findById(Long.valueOf((int)body.get("group_id")));
        if(!groupOptional.isPresent()){
            return new ResponseEntity<>(new HttpRespMessage("Group not found"),HttpStatus.OK);
        }
        Team team = new Team();
        team.setTeam_name((String) body.get("name"));
        team.setGroup(groupOptional.get());
        team.setTeam_logo_url((String) body.get("url"));
        try {
            teamRepository.save(team);
            return new ResponseEntity<>(new HttpRespMessage("Team Added"),HttpStatus.OK);
        }catch (DataAccessException e){
            e.printStackTrace();
            return new ResponseEntity<>(new HttpRespMessage(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getById/{id}")
    @ResponseBody
    public ResponseEntity<?> getTeamById(@PathVariable(value = "id") Long id) {
        Optional<Team> optionalTeam = teamRepository.findById(id);
        if(optionalTeam.isPresent()){
            return new ResponseEntity<>(optionalTeam.get(),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new HttpRespMessage("Not found"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getByGroup/{gId}")
    @ResponseBody
    public ResponseEntity<?> getTeamByGroup(@PathVariable(value = "gId") Long gId) {
        try {
            List<Team> teamList = teamRepository.getTeamsByGroup(gId);
            return new  ResponseEntity<>(teamList,HttpStatus.OK );
        }catch (DataAccessException e){
            e.printStackTrace();
            return new ResponseEntity<>(new HttpRespMessage(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/finishMatch")
    @ResponseBody
    public ResponseEntity<?> finishMatch(@PathVariable(value = "id") Long id, @RequestBody Map<String, Object> body) {
        Optional<Team> optionalTeamA = teamRepository.findById((long)body.get("team_a_id"));
        Optional<Team> optionalTeamB = teamRepository.findById((long)body.get("team_a_id"));
        if(optionalTeamA.isPresent()&&optionalTeamB.isPresent()){
            Team teamA = optionalTeamA.get();
            Team teamB = optionalTeamB.get();
            teamA.setGoals_scored((int)body.get("a_goal_s"));
            teamA.setGoals_conceded((int)body.get("a_goal_c"));
            teamB.setGoals_scored((int)body.get("a_goal_s"));
            teamB.setGoals_conceded((int)body.get("a_goal_c"));
            if(body.get("result").equals("AW")){
                teamA.setWin_count(teamA.getWin_count()+1);
                teamB.setLoose_count(teamB.getLoose_count()+1);
            }else if(body.get("result").equals("BW")){
                teamA.setLoose_count(teamA.getLoose_count()+1);
                teamB.setWin_count(teamB.getWin_count()+1);
            }else if(body.get("result").equals("D")){
                teamA.setDraw_count(teamA.getDraw_count()+1);
            }
            try {
                teamRepository.save(teamA);
                teamRepository.save(teamB);
                return new ResponseEntity<>(new HttpRespMessage("Result Updated"),HttpStatus.OK);
            }catch (DataAccessException e){
                return new ResponseEntity<>(new HttpRespMessage(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else {
            return new ResponseEntity<>(new HttpRespMessage("Team not found"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<HttpRespMessage> deleteTeam(@PathVariable(value = "id") Long id){
        Optional<Team> teamOptional = teamRepository.findById(id);
        if(!teamOptional.isPresent()){
            return new ResponseEntity<>(new HttpRespMessage("team not found"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            int resp = matchRepository.deleteMatchByTeam(id);
        }catch (DataAccessException e){
            e.printStackTrace();
            return new ResponseEntity<>(new HttpRespMessage(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            teamRepository.deleteById(id);
            return new ResponseEntity<>(new HttpRespMessage("team deleted"),HttpStatus.OK);
        }catch (DataAccessException e){
            return new ResponseEntity<>(new HttpRespMessage(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
