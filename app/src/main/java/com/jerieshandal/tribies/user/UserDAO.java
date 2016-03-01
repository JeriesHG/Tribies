/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.user;

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
 * UserDAO
 * Created by Jeries Handal on 2/2/2016.
 * Version 1.0.0
 */
public class UserDAO extends GenericDAO {

    private Map<String, String> testCredentials;

    public UserDAO() {
        testCredentials = new HashMap<>();
        testCredentials.put("12345", "123");
        testCredentials.put("54321", "123");
        testCredentials.put("jhandal@test.com", "123");
        testCredentials.put("tribies@tribies.com", "123");
    }

    public UserDAO(Connection connection) {
        super(connection);
    }

    public UserDTO checkLoginCredentials(String email, String phone, String password) throws SQLException {
        UserDTO e = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            int i = 1;
            email = (TextUtils.isEmpty(email)) ? "" : email;
            phone = (TextUtils.isEmpty(phone)) ? "" : phone;

            ps = connection.prepareStatement(USER.CheckLoginCredentials.sql());
            ps.setString(i++, email.toUpperCase());
            ps.setString(i++, phone.toUpperCase());
            ps.setString(i++, password);

            rs = ps.executeQuery();

            if (rs.next()) {
                hydrateAccount(e = new UserDTO(), rs);
            }
        } finally {
            close(rs, ps);
        }
        return e;
    }

    public UserDTO registerAccount(String name, String email, String phone, String password) throws SQLException {
        UserDTO e = null;

        PreparedStatement ps = null;
        ResultSet rs;

        try {
            int i = 1;

            ps = connection.prepareStatement(USER.RegisterAccount.sql());
            ps.setString(i++, name);
            ps.setString(i++, email);
            ps.setString(i++, phone);
            ps.setString(i++, TokenGenerator.generate());
            ps.setString(i++, password);

            rs = ps.executeQuery();

            if (rs.next()) {
                hydrateAccount(e = new UserDTO(), rs);
            }
        } finally {
            close(ps);
        }

        return e;
    }

    public UserDTO registerMockAccount(String name, String email, String phone, String password) {
        UserDTO e = new UserDTO();
        e.setFullName(name);
        e.setEmail(email);
        e.setPhone(phone);
        e.setPassword(password);
        e.setAccId(1);
        e.setToken(TokenGenerator.generate());
        e.setCreated(new Date());
        return e;
    }

    public UserDTO checkMockAccount(String loginCredential, String password) {
        UserDTO e = null;

        if(testCredentials.containsKey(loginCredential)){
            if(testCredentials.get(loginCredential).equals(password)){
                 e = new UserDTO();
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

    private void hydrateAccount(UserDTO e, ResultSet rs) throws SQLException {
        e.setAccId(rs.getInt("AccId"));
        e.setFullName(rs.getString("FullName"));
        e.setEmail(rs.getString("Email"));
        e.setPhone(rs.getString("Phone"));
        e.setToken(rs.getString("Token"));
        e.setCreated(DateUtils.toJavaDate(rs.getDate("Created")));
    }
}
