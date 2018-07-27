package com.ashik619.testproject.repositories;

import com.ashik619.testproject.models.Match;
import com.ashik619.testproject.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query(value = "SELECT * FROM teams  WHERE groupId  = ?1",
            nativeQuery = true)
    List<Team> getTeamsByGroup(Long gId);



}
