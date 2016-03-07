/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal> - December, 2015.
 *
 */
package com.jerieshandal.tribies.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Date;


/**
 * GenericDAO
 * Created by Jeries Handal on 12/29/2015.
 * Version 1.0.0
 */
public class GenericDAO {

    protected Connection connection;
    private final static Logger LOGGER = LoggerFactory.getLogger(GenericDAO.class);

    public GenericDAO(){}
    public GenericDAO(Connection connection){
        this.connection = connection;
    }

    protected Date toJavaDate(java.sql.Date date){
        return new Date(date.getTime());
    }

    protected void close(AutoCloseable... c) {
        for (AutoCloseable e : c) {
            if (e != null) {
                try {
                    e.close();
                } catch (Exception ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
    }
}
