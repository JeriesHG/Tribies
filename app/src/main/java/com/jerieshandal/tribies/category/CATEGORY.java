/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.category;

/**
 * CATEGORY
 * Created by Jeries Handal on 3/5/2016.
 * Version 1.0.0
 */
public enum CATEGORY {

    /**
     * Reads all categories order by name
     */
    ReadCategories("SELECT * FROM Category ORDER BY Name");

    private final String sql;

    private CATEGORY(String sql){
        this.sql = sql;
    }

    public String Sql(){
        return sql;
    }
}
