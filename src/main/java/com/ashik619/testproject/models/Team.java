package com.ashik619.testproject.models;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String  team_name;

    private String team_logo_url;


    @ManyToOne
    @JoinColumn(name = "groupId", nullable = false)
    private Group group;


    @Column(name="win_count")
    private Integer win_count = 0;

    @Column(name="loose_count")
    private Integer loose_count = 0;

    @Column(name="draw_count")
    private Integer draw_count = 0;

    @Column(name="goals_scored")
    private Integer goals_scored = 0;

    @Column(name="goals_conceded")
    private Integer goals_conceded = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getTeam_logo_url() {
        return team_logo_url;
    }

    public void setTeam_logo_url(String team_logo_url) {
        this.team_logo_url = team_logo_url;
    }


    public Integer getWin_count() {
        return win_count;
    }

    public void setWin_count(Integer win_count) {
        this.win_count = win_count;
    }

    public Integer getLoose_count() {
        return loose_count;
    }

    public void setLoose_count(Integer loose_count) {
        this.loose_count = loose_count;
    }

    public Integer getDraw_count() {
        return draw_count;
    }

    public void setDraw_count(Integer draw_count) {
        this.draw_count = draw_count;
    }

    public Integer getGoals_scored() {
        return goals_scored;
    }

    public void setGoals_scored(Integer goals_scored) {
        this.goals_scored = goals_scored;
    }

    public Integer getGoals_conceded() {
        return goals_conceded;
    }

    public void setGoals_conceded(Integer goals_conceded) {
        this.goals_conceded = goals_conceded;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

}
