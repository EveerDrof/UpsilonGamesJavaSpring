package com.diploma.UpsilonGames.storeRecords;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.users.User;

import javax.persistence.*;

@Entity
@Table(
        name = "store_record",
        uniqueConstraints = { @UniqueConstraint( columnNames = { "userId", "gameId" } ) }
)

public class StoreRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinColumn(name = "userId", referencedColumnName = "id", updatable = false,nullable = false)
    private User userId;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinColumn(name = "gameId", referencedColumnName = "id", updatable = false,nullable = false)
    private Game gameId;
    @Column(nullable = false, columnDefinition = "ENUM('IN_CART','IN_LIBRARY')")
    @Enumerated(EnumType.STRING)
    private StoreRecordType storeRecordType;
    public  StoreRecord(){

    }

    public StoreRecord(long id, User userId, Game gameId, StoreRecordType storeRecordType) {
        this.id = id;
        this.userId = userId;
        this.gameId = gameId;
        this.storeRecordType = storeRecordType;
    }
    public StoreRecord( User userId, Game gameId, StoreRecordType storeRecordType) {
        this(0,userId,gameId,storeRecordType);
        this.userId = userId;
        this.gameId = gameId;
        this.storeRecordType = storeRecordType;
    }

    public StoreRecordType getStoreRecordType() {
        return storeRecordType;
    }

    public void setStoreRecordType(StoreRecordType storeRecordType) {
        this.storeRecordType = storeRecordType;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
