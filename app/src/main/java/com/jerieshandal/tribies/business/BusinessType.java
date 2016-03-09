/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.business;

/**
 * BusinessType
 * Created by Jeries Handal on 3/7/2016.
 * Version 1.0.0
 */
public enum BusinessType {

    /**
     * Business that loads favorite business only
     */
    MY_STORES(0, "Mis Tiendas"),
    /**
     * Loads all business by category
     */
    STORES(1, "Tiendas"),

    /**
     * Loads Most Recent business
     */
    MOST_RECENT(2,"Mas Reciente");

    private final int id;
    private final String type;

    BusinessType(int id, String type){
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
