/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.security;


import java.util.UUID;

/**
 * TokenGenerator
 * Created by Jeries Handal on 2/11/2016.
 * Version 1.0.0
 */
public class TokenGenerator {

    public static String generate(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().toUpperCase().replaceAll("-","");
    }
}
