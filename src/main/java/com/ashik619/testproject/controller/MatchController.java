package com.ashik619.testproject.controller;


import com.ashik619.testproject.models.HttpRespMessage;
import com.ashik619.testproject.models.Match;
import com.ashik619.testproject.models.Team;
import com.ashik619.testproject.models.request_body.MatchStatus;
import com.ashik619.testproject.models.request_body.NewMatch;
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
@RequestMapping("/api/match")
public class MatchController {

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    TeamRepository teamRepository;


    @GetMapping("/getAll")
    @ResponseBody
    public ResponseEntity<?> getAllTeams() {
        try {
            List<Match> matchList = matchRepository.findAll();
            return new  ResponseEntity<>(matchList,HttpStatus.OK );
        }catch (DataAccessException e){
            e.printStackTrace();
            return new ResponseEntity<>(new HttpRespMessage(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/getAllRunning")
    @ResponseBody
    public ResponseEntity<?> getAllRunningMatches() {
        try {
            List<Match> matchList = matchRepository.getRunningMatches();
            return new  ResponseEntity<>(matchList,HttpStatus.OK );
        }catch (DataAccessException e){
            e.printStackTrace();
            return new ResponseEntity<>(new HttpRespMessage(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getRunning/{slot}")
    @ResponseBody
    public ResponseEntity<?> getAllRunningMatcheBySlot(@PathVariable(value = "slot") String slot) {
        try {
            List<Match> matchList = matchRepository.getRunningMatchesBySlot(slot);
            return new  ResponseEntity<>(matchList,HttpStatus.OK );
        }catch (DataAccessException e){
            e.printStackTrace();
            return new ResponseEntity<>(new HttpRespMessage(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity<?> getTeamById(@PathVariable(value = "id") Long id) {
        try {
            Optional<Match> optionalMatch = matchRepository.findById(id);
            if(!optionalMatch.isPresent())
                return new  ResponseEntity<>(new HttpRespMessage("Not found"),HttpStatus.OK );
            return new  ResponseEntity<>(optionalMatch.get(),HttpStatus.OK );
        }catch (DataAccessException e){
            e.printStackTrace();
            return new ResponseEntity<>(new HttpRespMessage(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/addNew")
    @ResponseBody
    public ResponseEntity<HttpRespMessage> addTeam(@RequestBody NewMatch body) {
        Optional<Team> aTeamOptional = teamRepository.findById(body.getaTeamId());
        if(!aTeamOptional.isPresent()){
            return new ResponseEntity<>(new HttpRespMessage("A team not found"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<Team> bTeamOptional = teamRepository.findById(body.getbTeamId());
        if(!bTeamOptional.isPresent()){
            return new ResponseEntity<>(new HttpRespMessage("B team not found"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(body.getMatchDate()==null){
            return new ResponseEntity<>(new HttpRespMessage("Invalid date"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Team aTeam = aTeamOptional.get();
        Team bTeam = bTeamOptional.get();

        Match match = new Match();
        match.setMatchDate(body.getMatchDate());
        match.setaTeam(aTeam);
        match.setbTeam(bTeam);
        if(body.getMatchType()!=null) {
            match.setMatchType(body.getMatchType());
        }
        try {
            matchRepository.save(match);
            return new ResponseEntity<>(new HttpRespMessage("Match Added"),HttpStatus.OK);
        }catch (DataAccessException e){
            e.printStackTrace();
            return new ResponseEntity<>(new HttpRespMessage(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @PostMapping("/changeStatus/{id}")
    @ResponseBody
    public ResponseEntity<HttpRespMessage> changeStatus(@PathVariable(value = "id") Long id,@RequestBody MatchStatus body) {
        Optional<Match> matchOptional = matchRepository.findById(id);
        if(!matchOptional.isPresent()){
            return new ResponseEntity<>(new HttpRespMessage("match not found"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Match match = matchOptional.get();
        String slot = null;
        switch (body.getType()){
            case "startMatch" :{
                List<Match> matchList = matchRepository.getRunningMatches();
                if(matchList==null||matchList.size()<2) {
                    switch (match.getMatchStatus()) {
                        case "scheduled": {
                            if(matchList==null||matchList.size()==0)
                                slot = "A";
                            else if(matchList.size() == 1) {
                                if(matchList.get(0).getMatchSlot().equals("B")){
                                    slot = "A";
                                }else {
                                    slot = "B";
                                }
                            }
                            match.setMatchStatus("running");
                            match.setMatchSlot(slot);
                            break;
                        }
                        case "running": {
                            return new ResponseEntity<>(new HttpRespMessage("match already started"), HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                        case "finished": {
                            return new ResponseEntity<>(new HttpRespMessage("match already finished"), HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    }
                }else {
                    return new ResponseEntity<>(new HttpRespMessage("Only two matches can be started at a time. Finish other matches to start"), HttpStatus.INTERNAL_SERVER_ERROR);
                }
                break;
            }
            case "endMatch" :{
                switch (match.getMatchStatus()){
                    case "scheduled" :{
                        return new ResponseEntity<>(new HttpRespMessage("match not started"),HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    case "finished":{
                        return new ResponseEntity<>(new HttpRespMessage("match already finished"),HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
                match.setaTeamGoals(body.getaGoals());
                match.setbTeamGoals(body.getbGoals());
                Optional<Team> aTeamOptional = teamRepository.findById(match.getaTeam().getId());
                Optional<Team> bTeamOptional = teamRepository.findById(match.getbTeam().getId());
                if(!aTeamOptional.isPresent()||!bTeamOptional.isPresent()){
                    return new ResponseEntity<>(new HttpRespMessage("teams not found"),HttpStatus.INTERNAL_SERVER_ERROR);
                }
                Team aTeam = aTeamOptional.get();
                Team bTeam = bTeamOptional.get();
                aTeam.setGoals_scored(body.getaGoals());
                aTeam.setGoals_conceded(body.getbGoals());
                bTeam.setGoals_scored(body.getbGoals());
                bTeam.setGoals_conceded(body.getaGoals());
                switch (body.getResult()){
                    case "AW":{
                        aTeam.setWin_count(aTeam.getWin_count()+1);
                        bTeam.setLoose_count(bTeam.getLoose_count()+1);
                        String result = aTeam.getTeam_name()+" Won the match";
                        match.setMatchResult(result);
                        break;
                    }
                    case "BW":{
                        bTeam.setWin_count(bTeam.getWin_count()+1);
                        aTeam.setLoose_count(aTeam.getLoose_count()+1);
                        String result = bTeam.getTeam_name()+" Won the match";
                        match.setMatchResult(result);
                        break;
                    }
                    case "D":{
                        aTeam.setDraw_count(aTeam.getDraw_count()+1);
                        bTeam.setDraw_count(bTeam.getDraw_count()+1);
                        match.setMatchResult("Match draw");
                        break;
                    }
                }
                try {
                    teamRepository.save(aTeam);
                    teamRepository.save(bTeam);
                }catch (DataAccessException e){
                    return new ResponseEntity<>(new HttpRespMessage(e.getMessage()),HttpStatus.OK);
                }
                match.setMatchStatus("finished");
                break;
            }
        }
        try {
            matchRepository.save(match);
            HttpRespMessage httpRespMessage = new HttpRespMessage("match updated");
            if(slot!=null){
                httpRespMessage.setData(slot);
            }
            return new ResponseEntity<>(httpRespMessage,HttpStatus.OK);
        }catch (DataAccessException e){
            e.printStackTrace();
            return new ResponseEntity<>(new HttpRespMessage(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<HttpRespMessage> deleteMatch(@PathVariable(value = "id") Long id){
        Optional<Match> matchOptional = matchRepository.findById(id);
        if(!matchOptional.isPresent()){
            return new ResponseEntity<>(new HttpRespMessage("match not found"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            matchRepository.deleteById(id);
            return new ResponseEntity<>(new HttpRespMessage("match deleted"),HttpStatus.OK);
        }catch (DataAccessException e){
            e.printStackTrace();
            return new ResponseEntity<>(new HttpRespMessage(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/bannerOne", method = RequestMethod.GET)
    public String getMatchABanner() {
        return "matchOneBanner";

    }




}
