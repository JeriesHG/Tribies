/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal> - December, 2015.
 *
 */
package com.jerieshandal.tribies.utility;

/**
 * Callbacks
 * Created by Jeries Handal on 12/29/2015.
 * Version 1.0.0
 */
public interface Callbacks {
    /**
     * A callback enum that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public void onItemSelected(String id);
}