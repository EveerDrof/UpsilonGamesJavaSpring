package com.diploma.UpsilonGames.storeRecords;

public enum StoreRecordType {
    IN_CART("IN_CART"),
    IN_LIBRARY("IN_LIBRARY");
    private String type;
    StoreRecordType(String type){
        this.type = type;
    }
}
