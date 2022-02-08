package com.diploma.UpsilonGames.foreignReviews;

import com.diploma.UpsilonGames.games.Game;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ForeignReviewsDataService {
    private ForeignReviewDataRepository foreignReviewDataRepository;

    public ForeignReviewsDataService(ForeignReviewDataRepository foreignReviewDataRepository) {

        this.foreignReviewDataRepository = foreignReviewDataRepository;
    }

    public ArrayList<ForeignReviewsData> findByGameIdAndSiteType(Game game,
                                                                 ForeignReviewsData.SiteType siteType) {
//        return foreignReviewDataRepository.findByGameIdAndSiteType(game, siteType);
        return null;
    }

}
