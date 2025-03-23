package com.example.botfightwebserver.tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentSetRepository extends JpaRepository<TournamentSet, Long> {
}
