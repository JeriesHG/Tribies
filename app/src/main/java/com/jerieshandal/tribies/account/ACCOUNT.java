/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.account;

/**
 * ACCOUNT
 * Created by Jeries Handal on 2/2/2016.
 * Version 1.0.0
 */
public enum ACCOUNT {

    /**
     * Check account credentials by Email or Phone
     */
    CheckLoginCredentials("SELECT * FROM Account WHERE UPPER(Email = ? OR Phone = ?) AND Password = ?"),
    /**
     * Registers New Account
     */
    RegisterAccount("INSERT INTO Account SET FullName = ?, Password = ?, Email = ?, Phone = ?, Token = ?");

    private final String sql;

    ACCOUNT(String sql){
        this.sql = sql;
    }

    public String sql() {
        return sql;
    }
}
