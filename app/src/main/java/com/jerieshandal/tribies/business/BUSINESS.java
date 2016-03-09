/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal> - December, 2015.
 *
 */
package com.jerieshandal.tribies.business;

/**
 * BUSINESS
 * Created by Jeries Handal on 12/29/2015.
 * Version 1.0.0
 */
public enum BUSINESS {

    /**
     * Insert a new record when the user hits the favorite button
     */
    InsertFavoriteBusiness("INSERT INTO FavoriteBusiness SET Favorite = 1, BusId = ?, UserId = ?;"),
    /**
     * Delete favorite business when
     */
    DeleteFavoriteBusiness("DELETE FROM FavoriteBusiness WHERE BusId = ? AND UserId = ?;"),
    /**
     * Reads All Business By Name
     */
    ReadBusinessByName("SELECT  B.BusId,B.Name, B.Email, B.Phone, B.Address, B.Logo, B.Created " +
            "FROM Business as B " +
            "INNER JOIN BusinessByCategory as BC " +
            "ON BC.CatId = ? AND B.BusId = BC.BusId " +
            "WHERE  NOT EXISTS (SELECT 1  FROM   FavoriteBusiness as FB  WHERE  B.BusId = FB.BusId AND FB.UserId = ?) " +
            "ORDER BY B.Name;"),
    /**
     * Reads all business liked by user
     */
    ReadFavoriteBusiness("SELECT  B.BusId,B.Name, B.Email, B.Phone, B.Address, B.Logo, B.Created " +
            "FROM Business as B " +
            "INNER JOIN BusinessByCategory as BC " +
            "ON BC.CatId = ? AND B.BusId = BC.BusId " +
            "WHERE EXISTS (SELECT 1  FROM   FavoriteBusiness as FB  WHERE  B.BusId = FB.BusId AND FB.UserId = ?) " +
            "ORDER BY B.Name;"),
    /**
     * Read most recent business
     */
    ReadMostRecentBusiness("SELECT  B.BusId,B.Name, B.Email, B.Phone, B.Address, B.Logo, B.Created " +
            "FROM Business as B " +
            "INNER JOIN BusinessByCategory as BC " +
            "ON BC.CatId = ? AND B.BusId = BC.BusId " +
            "WHERE  NOT EXISTS (SELECT 1  FROM   FavoriteBusiness as FB  WHERE  B.BusId = FB.BusId AND FB.UserId = ?) " +
            "ORDER BY B.Created;");

    private final String sql;

    BUSINESS(String sql) {
        this.sql = sql;
    }

    public String Sql() {
        return sql;
    }
}
