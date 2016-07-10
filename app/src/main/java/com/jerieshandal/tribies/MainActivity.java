/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal> - December, 2015.
 *
 */
package com.jerieshandal.tribies;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jerieshandal.tribies.business.SearchBusiness;
import com.jerieshandal.tribies.category.CategoryDAO;
import com.jerieshandal.tribies.category.CategoryDTO;
import com.jerieshandal.tribies.database.DriverFactory;
import com.jerieshandal.tribies.enums.Names;
import com.jerieshandal.tribies.popup.LoginPopup;
import com.jerieshandal.tribies.utility.Callbacks;
import com.jerieshandal.tribies.utility.StringUtils;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity
 * Created by Jeries Handal on 12/29/2015.
 * Version 1.0.0
 */
public class MainActivity extends AppCompatActivity implements Callbacks, GoogleApiClient.OnConnectionFailedListener {

    public static String CATEGORY_ID = "category_id";
    private NavigationView mNavigationView;
    private FragmentManager mFragmentManager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int userId = PreferenceManager.getDefaultSharedPreferences(this).getInt(LoginPopup.LOGGED_IN_ID, 0);
        if (userId == 0) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        setContentView(R.layout.activity_main);
        new LoadCategories(PreferenceManager.getDefaultSharedPreferences(this)).execute();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();


        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.main_navigation_view);

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.main_container_view, new MainTabFragment(), MainTabFragment.MAIN_TAB_TAG).commit();

        mNavigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        TextView mTitle = (TextView) findViewById(R.id.main_toolbar_title);
        mTitle.setTypeface(StringUtils.retrieveTitleFont(getAssets()));

        Button button = (Button) findViewById(R.id.locate_stores);
        button.setOnClickListener(locateStoresListener);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    public void onItemSelected(String id) {
        /*
        TODO: Create new intent and pass the selected business id
         to load the discounts related to the business
         */
        System.out.println(id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.user_settings:
                /**
                 * TODO: get user token or id and pass it to the iser settings intent
                 */
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            for (int i = 0; i < mNavigationView.getMenu().size(); i++) {
                mNavigationView.getMenu().getItem(i).setChecked(false);
            }
            item.setChecked(true);
            mDrawerLayout.closeDrawers();
            MainTabFragment tabFragment = (MainTabFragment) mFragmentManager.findFragmentByTag(MainTabFragment.MAIN_TAB_TAG);
            tabFragment.updateListFragment(item.getItemId());
            return false;
        }
    };

    private View.OnClickListener locateStoresListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                        .getCurrentPlace(mGoogleApiClient, null);
                result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                    @Override
                    public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                        Toast.makeText(MainActivity.this, "Escaneando, por favor espere", Toast.LENGTH_LONG).show();
                        List<String> placesId = new ArrayList<>();
                        List<String> busNames = new ArrayList<>();
                        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                            placesId.add(placeLikelihood.getPlace().getId());
                            busNames.add(placeLikelihood.getPlace().getName().toString());
                        }
                        likelyPlaces.release();
                        new SearchBusiness(getApplicationContext(), placesId,busNames).execute();
                    }
                });
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient != null){
            mGoogleApiClient.disconnect();
        }
    }

    private class LoadCategories extends AsyncTask<List<CategoryDTO>, Void, List<CategoryDTO>> {

        private final Type listType = new TypeToken<
                List<CategoryDTO>>() {
        }.getType();
        private final Gson gson;
        private SharedPreferences preferences;

        private LoadCategories(SharedPreferences preferences) {
            this.preferences = preferences;
            gson = new Gson();
        }

        @Override
        protected List<CategoryDTO> doInBackground(List... params) {
            List<CategoryDTO> c = gson.fromJson(preferences.getString(Names.CATEGORY_LIST.getName(), ""), listType);

            if (c == null) {
                c = new ArrayList<>();
                try {
                    Connection connection = DriverFactory.getTribiesConnection();
                    CategoryDAO dao = new CategoryDAO(connection);
                    c = dao.readCategories();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                preferences.edit().putString(Names.CATEGORY_LIST.getName(), gson.toJson(c, listType)).apply();
            }

            return c;
        }

        @Override
        protected void onPostExecute(List<CategoryDTO> c) {
            if (c != null) {
                Menu menu = mNavigationView.getMenu();
                for (int i = 0; i < c.size(); i++) {
                    CategoryDTO e = c.get(i);
                    menu.add(i, e.getCatId(), Menu.NONE, e.getName());
                    if (i == 0) {
                        menu.getItem(i).setChecked(true);
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(findViewById(R.id.root_view));
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

//    public String getJSON(String url, int timeout) {
//        HttpURLConnection c = null;
//        try {
//            URL u = new URL(url);
//            c = (HttpURLConnection) u.openConnection();
//            c.setRequestMethod("GET");
//            c.setRequestProperty("Content-length", "0");
//            c.setUseCaches(false);
//            c.setAllowUserInteraction(false);
//            c.setConnectTimeout(timeout);
//            c.setReadTimeout(timeout);
//            c.connect();
//            int status = c.getResponseCode();
//
//            switch (status) {
//                case 200:
//                case 201:
//                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
//                    StringBuilder sb = new StringBuilder();
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        sb.append(line + "\n");
//                    }
//                    br.close();
//                    return sb.toString();
//            }
//
//        } catch (MalformedURLException ex) {
//            LOGGER.error(ex.getMessage(), ex);
//        } catch (IOException ex) {
//            LOGGER.error(ex.getMessage(), ex);
//        } finally {
//            if (c != null) {
//                try {
//                    c.disconnect();
//                } catch (Exception ex) {
//                    LOGGER.error(ex.getMessage(), ex);
//                }
//            }
//        }
//
//        return null;
//    }
}
