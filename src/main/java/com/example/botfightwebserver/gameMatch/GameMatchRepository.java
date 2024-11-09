package com.example.botfightwebserver.gameMatch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameMatchRepository extends JpaRepository<GameMatch, Long> {
}
