/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.dao;


import com.jerieshandal.tribies.category.CATEGORY;
import com.jerieshandal.tribies.dto.CategoryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CategoryDAO
 * Created by Jeries Handal on 3/5/2016.
 * Version 1.0.0
 */
public class CategoryDAO extends GenericDAO {

    public CategoryDAO(Connection connection) {
        super(connection);
    }

    public List<CategoryDTO> readCategories() throws SQLException {
        List<CategoryDTO> c = new ArrayList<>();

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(CATEGORY.ReadCategories.Sql());
            rs = ps.executeQuery();

            while (rs.next()) {
                CategoryDTO e;
                c.add(e = new CategoryDTO());
                hydrateCategory(rs, e);

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

    private void hydrateCategory(ResultSet rs, CategoryDTO e) throws SQLException {
        e.setCatId(rs.getInt("CatId"));
        e.setName(rs.getString("Name"));
        e.setIcon(rs.getString("Icon"));
        e.setCreated(toJavaDate(rs.getDate("Created")));
    }
}
