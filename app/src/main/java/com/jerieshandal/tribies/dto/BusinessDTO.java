/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal> - December, 2015.
 *
 */
package com.jerieshandal.tribies.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * BusinessDTO
 * Created by Jeries Handal on 12/29/2015.
 * Version 1.0.0
 */
public class BusinessDTO implements Serializable {


    private int busId;
    private String  name;
    private String phone;
    private String email;
    private String address;
    private String logo;
    private String website;
    private Date created;


    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

}
