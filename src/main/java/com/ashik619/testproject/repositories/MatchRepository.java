package com.ashik619.testproject.repositories;

import com.ashik619.testproject.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface MatchRepository extends JpaRepository<Match,Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM matches  WHERE aTeamId = ?1 OR bTeamId = ?1",
            nativeQuery = true)
    int deleteMatchByTeam(Long teamId);


    @Query(value = "SELECT * FROM matches  WHERE match_status  = 'running'",
            nativeQuery = true)
    List<Match> getRunningMatches();

    @Query(value = "SELECT * FROM matches  WHERE match_status  = 'running' AND match_slot = ?1",
            nativeQuery = true)
    List<Match> getRunningMatchesBySlot(String slot);


}
