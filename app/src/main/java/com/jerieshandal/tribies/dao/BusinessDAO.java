/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal> - December, 2015.
 *
 */
package com.jerieshandal.tribies.dao;

import com.jerieshandal.tribies.business.BUSINESS;
import com.jerieshandal.tribies.business.BusinessView;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * BusinessDAO
 * Created by Jeries Handal on 12/29/2015.
 * Version 1.0.0
 */
public class BusinessDAO extends GenericDAO {

    public BusinessDAO(Connection connection) {
        super(connection);
    }

    public boolean deleteFavoriteBusiness(int busId, int userId) throws SQLException {
        boolean success = false;

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            int i = 1;
            ps = connection.prepareStatement(BUSINESS.DeleteFavoriteBusiness.Sql());
            ps.setInt(i++, busId);
            ps.setInt(i++, userId);

            success = (ps.executeUpdate() != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return success;
    }

    public boolean insertFavoriteBusiness(int busId, int userId) throws SQLException {
        boolean success = false;

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            int i = 1;
            ps = connection.prepareStatement(BUSINESS.InsertFavoriteBusiness.Sql());
            ps.setInt(i++, busId);
            ps.setInt(i++, userId);

            success = (ps.executeUpdate() != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return success;
    }

    public List<BusinessView> readFavoriteBusiness(int catId, int userId) throws SQLException {
        List<BusinessView> c = new ArrayList<>();

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            int i = 1;
            ps = connection.prepareStatement(BUSINESS.ReadFavoriteBusiness.Sql());
            ps.setInt(i++, catId);
            ps.setInt(i++, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                BusinessView e;
                c.add(e = new BusinessView());
                e.setFavorite(true);
                hydrateBusiness(rs, e);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return c;
    }

    public List<BusinessView> readBusinessByName(int catId, int userId) throws SQLException {
        List<BusinessView> c = new ArrayList<>();

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            int i = 1;
            ps = connection.prepareStatement(BUSINESS.ReadBusinessByName.Sql());
            ps.setInt(i++, catId);
            ps.setInt(i++, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                BusinessView e;
                c.add(e = new BusinessView());
                hydrateBusiness(rs, e);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return c;
    }

    public List<BusinessView> readMostRecentBusiness(int catId, int userId) throws SQLException {
        List<BusinessView> c = new ArrayList<>();

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            int i = 1;
            ps = connection.prepareStatement(BUSINESS.ReadMostRecentBusiness.Sql());
            ps.setInt(i++, catId);
            ps.setInt(i++, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                BusinessView e;
                c.add(e = new BusinessView());
                hydrateBusiness(rs, e);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return c;
    }

    public List<BusinessView> executePlaces(String placesId, String businessName) throws SQLException {
        List<BusinessView> c = new ArrayList<>();

        ResultSet rs = null;
        CallableStatement cs = null;
        try {
            int i = 1;
            cs = connection.prepareCall(BUSINESS.ExecutePlaces.Sql());
            cs.setString(i++, placesId);
            cs.setString(i++, businessName);

            System.out.println("Searching for: " + placesId + " with Name: " + businessName);

            if (cs.execute()) {
                rs = cs.getResultSet();
                if (rs != null) {
                    while (rs.next()) {
                        BusinessView e;
                        c.add(e = new BusinessView());
                        hydrateBusiness(rs, e);

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (cs != null) {
                cs.close();
            }
        }

        return c;
    }

    private void hydrateBusiness(ResultSet rs, BusinessView e) throws SQLException {
        e.setBusId(rs.getInt("BusId"));
        e.setName(rs.getString("Name"));
        e.setEmail(rs.getString("Email"));
        e.setPhone(rs.getString("Phone"));
        e.setAddress(rs.getString("Address"));
        e.setLogo(rs.getString("Logo"));
        e.setWebsite(rs.getString("Website"));
        e.setCreated(toJavaDate(rs.getDate("Created")));
    }
}
