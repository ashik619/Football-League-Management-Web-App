package com.ashik619.testproject.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aTeamId", nullable = false)
    private Team aTeam;

    @ManyToOne
    @JoinColumn(name = "bTeamId", nullable = false)
    private Team bTeam;

    @Column(name = "matchType")
    private String matchType = "Group Stage";

    @Column(name = "match_status",insertable = false, columnDefinition = "varchar(31) DEFAULT 'scheduled'")
    private String matchStatus;

    @Temporal(value=TemporalType.DATE)
    @Column(name="matchDate")
    private Date matchDate;

    private Integer aTeamGoals = 0;

    private Integer bTeamGoals = 0;

    @Column(name="match_result")
    private String matchResult;

    @Column(name = "match_slot")
    private String matchSlot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getaTeam() {
        return aTeam;
    }

    public void setaTeam(Team aTeam) {
        this.aTeam = aTeam;
    }

    public Team getbTeam() {
        return bTeam;
    }

    public void setbTeam(Team bTeam) {
        this.bTeam = bTeam;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public Integer getaTeamGoals() {
        return aTeamGoals;
    }

    public void setaTeamGoals(Integer aTeamGoals) {
        this.aTeamGoals = aTeamGoals;
    }

    public Integer getbTeamGoals() {
        return bTeamGoals;
    }

    public void setbTeamGoals(Integer bTeamGoals) {
        this.bTeamGoals = bTeamGoals;
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    public String getMatchSlot() {
        return matchSlot;
    }

    public void setMatchSlot(String matchSlot) {
        this.matchSlot = matchSlot;
    }

}
