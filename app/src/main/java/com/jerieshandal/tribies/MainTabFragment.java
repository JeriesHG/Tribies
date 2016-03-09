/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies;

import android.os.Bundle;
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

/**
 * MainTabFragment
 * Created by Jeries Handal on 1/1/2016.
 * Version 1.0.0
 */
public class MainTabFragment extends Fragment {

    private static final String[] tabNames = {BusinessType.MY_STORES.getType(),
            BusinessType.STORES.getType(),
            BusinessType.MOST_RECENT.getType()};

    public static TabLayout tabLayout;
    public static ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_tab_layout, null);

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

    private class TabAdapter extends FragmentPagerAdapter{

        public TabAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            BusinessListFragment fragment = new BusinessListFragment();

            switch(position){
                case 0:
                    fragment.updateList(BusinessType.MY_STORES);
                    break;
                case 1:
                    fragment.updateList(BusinessType.STORES);
                    break;
                case 2:
                    fragment.updateList(BusinessType.MOST_RECENT);
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames[position];
        }
    }
}
