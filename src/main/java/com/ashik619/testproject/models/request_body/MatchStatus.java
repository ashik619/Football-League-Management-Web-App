package com.ashik619.testproject.models.request_body;

public class MatchStatus {
    private String type;
    private Integer aGoals;
    private Integer bGoals;
    private String result;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getaGoals() {
        return aGoals;
    }

    public void setaGoals(Integer aGoals) {
        this.aGoals = aGoals;
    }

    public Integer getbGoals() {
        return bGoals;
    }

    public void setbGoals(Integer bGoals) {
        this.bGoals = bGoals;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
