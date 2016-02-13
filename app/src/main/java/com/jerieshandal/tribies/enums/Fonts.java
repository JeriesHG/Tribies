/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.enums;

/**
 * Fonts
 * Created by Jeries Handal on 1/1/2016.
 * Version 1.0.0
 */
public enum Fonts {

    TITLE_FONT("fonts/Comfortaa-Regular.ttf");

    private String font;

    Fonts(String font){
        this.font = font;
    }

    public String getFont(){
        return font;
    }
}
