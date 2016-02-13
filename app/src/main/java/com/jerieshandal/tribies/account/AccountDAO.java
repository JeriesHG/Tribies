/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.account;

import android.text.TextUtils;

import com.jerieshandal.tribies.security.TokenGenerator;
import com.jerieshandal.tribies.utility.DateUtils;
import com.jerieshandal.tribies.utility.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * AccountDAO
 * Created by Jeries Handal on 2/2/2016.
 * Version 1.0.0
 */
public class AccountDAO extends GenericDAO {

    private Map<String, String> testCredentials;

    public AccountDAO() {
        testCredentials = new HashMap<>();
        testCredentials.put("12345", "123");
        testCredentials.put("54321", "123");
        testCredentials.put("jhandal@test.com", "123");
        testCredentials.put("tribies@tribies.com", "123");
    }

    public AccountDAO(Connection connection) {
        super(connection);
    }

    public AccountDTO checkLoginCredentials(String email, String phone, String password) throws SQLException {
        AccountDTO e = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            int i = 1;
            email = (TextUtils.isEmpty(email)) ? "" : email;
            phone = (TextUtils.isEmpty(phone)) ? "" : phone;

            ps = connection.prepareStatement(ACCOUNT.CheckLoginCredentials.sql());
            ps.setString(i++, email.toUpperCase());
            ps.setString(i++, phone.toUpperCase());
            ps.setString(i++, password);

            rs = ps.executeQuery();

            if (rs.next()) {
                hydrateAccount(e = new AccountDTO(), rs);
            }
        } finally {
            close(rs, ps);
        }
        return e;
    }

    public AccountDTO registerAccount(String name, String email, String phone, String password) throws SQLException {
        AccountDTO e = null;

        PreparedStatement ps = null;
        ResultSet rs;

        try {
            int i = 1;

            ps = connection.prepareStatement(ACCOUNT.RegisterAccount.sql());
            ps.setString(i++, name);
            ps.setString(i++, email);
            ps.setString(i++, phone);
            ps.setString(i++, TokenGenerator.generate());
            ps.setString(i++, password);

            rs = ps.executeQuery();

            if (rs.next()) {
                hydrateAccount(e = new AccountDTO(), rs);
            }
        } finally {
            close(ps);
        }

        return e;
    }

    public AccountDTO registerMockAccount(String name, String email, String phone, String password) {
        AccountDTO e = new AccountDTO();
        e.setFullName(name);
        e.setEmail(email);
        e.setPhone(phone);
        e.setPassword(password);
        e.setAccId(1);
        e.setToken(TokenGenerator.generate());
        e.setCreated(new Date());
        return e;
    }

    public AccountDTO checkMockAccount(String loginCredential, String password) {
        AccountDTO e = null;

        if(testCredentials.containsKey(loginCredential)){
            if(testCredentials.get(loginCredential).equals(password)){
                 e = new AccountDTO();
                e.setFullName("TEST");
                e.setEmail((loginCredential.contains("@") ? loginCredential : "test@test.com"));
                e.setPhone((loginCredential.contains("@")) ? "12345" : loginCredential);
                e.setAccId(1);
                e.setCreated(new Date());
                e.setPassword(password);
                e.setToken(TokenGenerator.generate());
            }
        }

        return e;
    }

    private void hydrateAccount(AccountDTO e, ResultSet rs) throws SQLException {
        e.setAccId(rs.getInt("AccId"));
        e.setFullName(rs.getString("FullName"));
        e.setEmail(rs.getString("Email"));
        e.setPhone(rs.getString("Phone"));
        e.setToken(rs.getString("Token"));
        e.setCreated(DateUtils.toJavaDate(rs.getDate("Created")));
    }
}
