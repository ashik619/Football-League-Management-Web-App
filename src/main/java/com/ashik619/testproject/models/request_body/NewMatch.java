package com.ashik619.testproject.models.request_body;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class NewMatch {

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date matchDate;

    private String matchType;

    private Long aTeamId;

    private Long bTeamId;

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public Long getaTeamId() {
        return aTeamId;
    }

    public void setaTeamId(Long aTeamId) {
        this.aTeamId = aTeamId;
    }

    public Long getbTeamId() {
        return bTeamId;
    }

    public void setbTeamId(Long bTeamId) {
        this.bTeamId = bTeamId;
    }
}
