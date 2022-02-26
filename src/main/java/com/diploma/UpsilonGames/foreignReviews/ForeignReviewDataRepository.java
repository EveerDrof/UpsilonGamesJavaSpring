package com.diploma.UpsilonGames.foreignReviews;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ForeignReviewDataRepository extends JpaRepository<ForeignReviewsData, Long> {
    //    @Query(value = "SELECT * FROM foreign_review fr WHERE fr.game_id = ?1 AND fr.site_type = ?2",
//            nativeQuery = true)
//    ArrayList<ForeignReviewsData> findByGameIdAndSiteType(Game game, ForeignReviewsData.SiteType siteType);
}
