/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.enums;

/**
 * Names
 * Created by Jeries Handal on 1/1/2016.
 * Version 1.0.0
 */
public enum Names {

    CATEGORY_LIST("categories");


    private String name;

    Names(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
