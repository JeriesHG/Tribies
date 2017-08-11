/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.dao;

import android.text.TextUtils;

import com.jerieshandal.tribies.security.TokenGenerator;
import com.jerieshandal.tribies.user.USER;
import com.jerieshandal.tribies.dto.UserDTO;
import com.jerieshandal.tribies.utility.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserDAO
 * Created by Jeries Handal on 2/2/2016.
 * Version 1.0.0
 */
public class UserDAO extends GenericDAO {

    public UserDAO(Connection connection) {
        super(connection);
    }

    public UserDTO loadUser(int userId) throws SQLException {
        UserDTO e = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            int i = 1;

            ps = connection.prepareStatement(USER.LoadUser.sql());
            ps.setInt(i++, userId);

            rs = ps.executeQuery();

            if (rs.next()) {
                hydrateAccount(e = new UserDTO(), rs);
            }
        } finally {
            close(rs, ps);
            if (connection != null) {
                connection.close();
            }
        }
        return e;
    }

    public UserDTO checkLoginCredentials(String credentials, String password) throws SQLException {
        UserDTO e = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            int i = 1;
            credentials = (TextUtils.isEmpty(credentials)) ? "" : credentials;

            ps = connection.prepareStatement(USER.CheckLoginCredentials.sql());
            ps.setString(i++, credentials.toUpperCase());
            ps.setString(i++, credentials.toUpperCase());
            ps.setString(i++, password);

            rs = ps.executeQuery();

            if (rs.next()) {
                hydrateAccount(e = new UserDTO(), rs);
            }
        } finally {
            close(rs, ps);
            if (connection != null) {
                connection.close();
            }
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
            if (connection != null) {
                connection.close();
            }
        }

        return e;
    }

    private void hydrateAccount(UserDTO e, ResultSet rs) throws SQLException {
        e.setUsrId(rs.getInt("UserId"));
        e.setFullName(rs.getString("FullName"));
        e.setEmail(rs.getString("Email"));
        e.setPhone(rs.getString("Phone"));
        e.setToken(rs.getString("Token"));
        e.setCreated(DateUtils.toJavaDate(rs.getDate("Created")));
    }
}
