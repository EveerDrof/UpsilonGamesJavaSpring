package com.diploma.UpsilonGames.marks;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarkRepository extends JpaRepository<Mark,Long> {
    Mark getByUserIdAndByGameId(long userId,long gameId);
    List<Mark> findAllByUserId(long userId);

    List<Mark> findAllByGameId(long gameId);
}
