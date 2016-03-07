/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal> - December, 2015.
 *
 */
package com.jerieshandal.tribies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jerieshandal.tribies.category.CategoryDAO;
import com.jerieshandal.tribies.category.CategoryDTO;
import com.jerieshandal.tribies.database.DriverFactory;
import com.jerieshandal.tribies.enums.Names;
import com.jerieshandal.tribies.popup.LoginPopup;
import com.jerieshandal.tribies.utility.Callbacks;
import com.jerieshandal.tribies.utility.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity
 * Created by Jeries Handal on 12/29/2015.
 * Version 1.0.0
 */
public class MainActivity extends AppCompatActivity implements Callbacks {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MainActivity.class);
    private NavigationView mNavigationView;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mTitle;
    private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            //TODO: add selected category logic
            mDrawerLayout.closeDrawers();
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = PreferenceManager.getDefaultSharedPreferences(this).getString(LoginPopup.LOGGED_IN_ID, "");
        if (TextUtils.isEmpty(token)) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        setContentView(R.layout.activity_main);
        new LoadCategories(PreferenceManager.getDefaultSharedPreferences(this)).execute();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.main_navigation_view);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.main_container_view, new MainTabFragment()).commit();

        mNavigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mTitle = (TextView) findViewById(R.id.main_toolbar_title);
        mTitle.setTypeface(StringUtils.retrieveTitleFont(getAssets()));

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
                    menu.add(i, c.get(i).getCatId(), Menu.NONE, c.get(i).getName());
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
