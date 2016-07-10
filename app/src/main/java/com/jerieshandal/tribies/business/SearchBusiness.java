/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.business;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.jerieshandal.tribies.R;
import com.jerieshandal.tribies.database.DriverFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * SearchBusiness
 * Created by Jeries Handal on 3/17/2016.
 * Version 1.0.0
 */
public class SearchBusiness  extends AsyncTask<List<BusinessView>, Void, List<BusinessView>> {
    private Context context;
   private List<String> placesIds;
    private List<String> busNames;

    public SearchBusiness(Context context, List<String> placesIds, List<String> busNames) {
        this.context = context;
        this.placesIds = placesIds;
        this.busNames = busNames;
    }

    @Override
    protected List<BusinessView> doInBackground(List... params) {
        List<BusinessView> c = new ArrayList<>();

        Connection connection = null;
        try {
            connection = DriverFactory.getTribiesConnection();
            BusinessDAO dao = new BusinessDAO(connection);
            for(int i = 0;i<placesIds.size();i++){
                c.addAll(dao.executePlaces(placesIds.get(i), busNames.get(i)));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return c;
    }

    @Override
    protected void onPostExecute(List<BusinessView> c) {
        if (c != null && !c.isEmpty()) {
            String message = (c.size()>1) ? "Hay mas de una tienda alrededor con descuentos disponibles!" :
                    c.get(0).getName() + " tiene descuentos, ve ahora!";
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.notif_icon)
                            .setContentTitle("Tribies")
                            .setContentText(message);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(123, mBuilder.build());
        }
        Toast.makeText(context, "Se han encontrado " + ((c != null) ? c.size() : 0) + " tiendas con descuentos", Toast.LENGTH_LONG).show();
    }
}
