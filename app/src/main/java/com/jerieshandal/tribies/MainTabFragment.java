/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerieshandal.tribies.business.BusinessListFragment;
import com.jerieshandal.tribies.business.BusinessType;
import com.jerieshandal.tribies.popup.LoginPopup;

import java.util.HashMap;
import java.util.Map;

/**
 * MainTabFragment
 * Created by Jeries Handal on 1/1/2016.
 * Version 1.0.0
 */
public class MainTabFragment extends Fragment {

    public static final String MAIN_TAB_TAG = "main_tab_tag";

    private int userId;

    private static final BusinessType[] tabNames = {BusinessType.MY_STORES,
            BusinessType.STORES,
            BusinessType.MOST_RECENT};

    public static TabLayout tabLayout;
    public static ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_tab_layout, null);
        userId = PreferenceManager.getDefaultSharedPreferences(container.getContext()).getInt(LoginPopup.LOGGED_IN_ID, 0);
        tabLayout = (TabLayout) view.findViewById(R.id.main_tabs);
        viewPager = (ViewPager) view.findViewById(R.id.main_view_pager);
        viewPager.setAdapter(new TabAdapter(getChildFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        return view;
    }

    public void updateListFragment(int catId) {
        TabAdapter adapter = (TabAdapter) viewPager.getAdapter();
        BusinessListFragment fragment = (BusinessListFragment) adapter.getFragment(tabLayout.getSelectedTabPosition());
        fragment.updateList(tabNames[tabLayout.getSelectedTabPosition()], catId, userId);
    }

    private class TabAdapter extends FragmentPagerAdapter {

        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;

        public TabAdapter(FragmentManager mFragmentManager) {
            super(mFragmentManager);
            this.mFragmentManager = mFragmentManager;
            mFragmentTags = new HashMap<>();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);

            if (obj instanceof Fragment) {
                // record the fragment tag here.
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }

            return obj;
        }

        @Override
        public Fragment getItem(int position) {
            BusinessListFragment fragment = new BusinessListFragment();

            switch (position) {
                case 0:
                    fragment.updateList(BusinessType.MY_STORES, 8, userId);
                    break;
                case 1:
                    fragment.updateList(BusinessType.STORES, 8, userId);
                    break;
                case 2:
                    fragment.updateList(BusinessType.MOST_RECENT, 8, userId);
                    break;
            }

            return fragment;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null)
                return null;
            return mFragmentManager.findFragmentByTag(tag);
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames[position].getType();
        }
    }
}
