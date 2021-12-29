package com.diploma.UpsilonGames.storeRecords;

import com.diploma.UpsilonGames.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StoreRecordRepository extends JpaRepository<StoreRecord,Long> {
    @Query(value = "SELECT sr.game_id FROM store_record sr WHERE sr.user_id = ?1 " +
            " AND sr.store_record_type = ?2"
            ,nativeQuery = true)
    List<Long> getUserGamesWithType(User userId, String storeRecordType);
    @Query(value = "UPDATE store_record sr SET sr.store_record_type = ?3 " +
            " WHERE sr.user_id = ?1 AND sr.store_record_type = ?2"
            ,nativeQuery = true)
    @Modifying
    @Transactional
    void switchUserGamesType(User user, String oldRecordType, String newRecordType);
}
