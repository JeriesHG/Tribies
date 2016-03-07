/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal> - December, 2015.
 *
 */
package com.jerieshandal.tribies.business;

import com.jerieshandal.tribies.utility.GenericDAO;

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
public class BusinessDAO extends GenericDAO{

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
            if(ps != null){
                ps.close();
            }
            if(connection != null){
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
            if(ps != null){
                ps.close();
            }
            if(connection != null){
                connection.close();
            }
        }

        return success;
    }

    public List<BusinessDTO> readFavoriteBusiness(int userId) throws SQLException {
        List<BusinessDTO> c = new ArrayList<>();

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            int i = 1;
            ps = connection.prepareStatement(BUSINESS.ReadFavoriteBusiness.Sql());
            ps.setInt(i++, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                BusinessDTO e;
                c.add(e = new BusinessDTO());
                hydrateBusiness(rs, e);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if(ps != null){
                ps.close();
            }
            if(connection != null){
                connection.close();
            }
        }

        return c;
    }

    public List<BusinessDTO> readBusinessByName() throws SQLException {
        List<BusinessDTO> c = new ArrayList<>();

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(BUSINESS.ReadBusinessByName.Sql());
            rs = ps.executeQuery();

            while (rs.next()) {
                BusinessDTO e;
                c.add(e = new BusinessDTO());
                hydrateBusiness(rs, e);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if(ps != null){
                ps.close();
            }
            if(connection != null){
                connection.close();
            }
        }

        return c;
    }

    public List<BusinessDTO> readTrendingBusiness() throws SQLException {
        List<BusinessDTO> c = new ArrayList<>();

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(BUSINESS.ReadTrendingBusiness.Sql());
            rs = ps.executeQuery();

            while (rs.next()) {
                BusinessDTO e;
                c.add(e = new BusinessDTO());
                hydrateBusiness(rs, e);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if(ps != null){
                ps.close();
            }
            if(connection != null){
                connection.close();
            }
        }

        return c;
    }

    private void hydrateBusiness(ResultSet rs, BusinessDTO e) throws SQLException {
        e.setBusId(rs.getInt("BusId"));
        e.setName(rs.getString("Name"));
        e.setEmail(rs.getString("Email"));
        e.setPhone(rs.getString("Phone"));
        e.setAddress(rs.getString("Address"));
        e.setLogo(rs.getString("Logo"));
        e.setCreated(toJavaDate(rs.getDate("Created")));
    }
}
