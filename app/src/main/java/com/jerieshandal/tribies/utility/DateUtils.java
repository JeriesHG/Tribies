/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.utility;

import java.util.Date;

/**
 * DateUtils
 * Created by Jeries Handal on 1/1/2016.
 * Version 1.0.0
 */
public class DateUtils {

    public static Date toJavaDate(java.sql.Date date) {
        return (date == null) ? date : new Date(date.getTime());
    }
}
