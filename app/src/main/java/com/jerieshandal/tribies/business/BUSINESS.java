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
    ReadBusinessByName("SELECT  B.BusId,B.Name, B.Email, B.Phone, B.Address, B.Logo, B.Created" +
            "FROM Business as B " +
            "ORDER BY B.B.Name;"),
    /**
     * Reads all business liked by user
     */
    ReadFavoriteBusiness("SELECT  B.BusId, B.Name, B.Email, B.Phone, B.Address, B.Logo, B.Created " +
            "FROM Business as B " +
            "INNER JOIN FavoriteBusiness as FB " +
            "ON B.BusId = FB.BusId " +
            "AND FB.UserId = ?;"),
    /**
     * Read trending business
     */
    ReadTrendingBusiness("SELECT  B.BusId, B.Name, B.Email, B.Phone, B.Address, B.Logo, B.Created " +
            "FROM Business as B " +
            "ORDER BY B.Created;");

    private final String sql;

    private BUSINESS(String sql) {
        this.sql = sql;
    }

    public String Sql() {
        return sql;
    }
}
