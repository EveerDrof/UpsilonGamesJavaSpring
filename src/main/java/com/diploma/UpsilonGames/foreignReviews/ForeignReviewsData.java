package com.diploma.UpsilonGames.foreignReviews;

import com.diploma.UpsilonGames.games.Game;

import javax.persistence.*;
import java.util.HashMap;

@Entity
public class ForeignReviewsData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "site", columnDefinition = "ENUM('STEAM','METACRITIC')")
    @Enumerated(EnumType.STRING)
    private SiteType siteType;
    //    @OneToOne(targetEntity = Game.class, mappedBy = "id")
//    private Game gameId;
    @Column(length = 99999999)
    private HashMap<String, Object> reviewsSiteObject;

    public ForeignReviewsData() {
    }

    public ForeignReviewsData(SiteType siteType, Game gameId, HashMap reviewsSiteObject) {

        this.siteType = siteType;
//        this.gameId = gameId;
        this.reviewsSiteObject = reviewsSiteObject;
    }

    public long getId() {
        return id;
    }

    public HashMap getReviewsSiteObject() {
        return reviewsSiteObject;
    }


    public enum SiteType {
        STEAM("STEAM"),
        METACRITIC("METACRITIC");
        private final String siteType;

        SiteType(String siteType) {
            this.siteType = siteType;
        }

        public String getSiteType() {
            return siteType;
        }
    }
}
