/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DiscountDTO
 * Created by Jeries Handal on 2/2/2016.
 * Version 1.0.0
 */
public class DiscountDTO  implements Serializable {

    private int discId;
    private String title;
    private String description;
    private Date start;
    private Date expiry;
    private Date created;

    public int getDiscId() {
        return discId;
    }

    public void setDiscId(int discId) {
        this.discId = discId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
