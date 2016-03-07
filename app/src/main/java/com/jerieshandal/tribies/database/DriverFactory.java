/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DriverFactory
 * Created by Jeries Handal on 3/5/2016.
 * Version 1.0.0
 */
public class DriverFactory {

    private static final String tribies = "jdbc:mysql://52.36.153.127:3306/tribies";
    private static final String user = "root";
    private static final String pass = "J!2345678";

    public static Connection getTribiesConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        return DriverManager.getConnection(tribies, user, pass);

    }
}
