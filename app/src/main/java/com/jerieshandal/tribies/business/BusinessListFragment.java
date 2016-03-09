/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal> - December, 2015.
 *
 */
package com.jerieshandal.tribies.business;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.jerieshandal.tribies.BusinessDiscountsActivity;
import com.jerieshandal.tribies.R;
import com.jerieshandal.tribies.database.DriverFactory;
import com.jerieshandal.tribies.utility.Callbacks;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * BusinessListFragment
 * Created by Jeries Handal on 12/29/2015.
 * Version 1.0.0
 */

public class BusinessListFragment extends ListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    public static final String FRAGMENT_POSITION = "fragment_position";

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {

        }
    };
    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;
    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    private BusinessAdapter businessAdapter;

    public BusinessListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        businessAdapter = new BusinessAdapter(getContext(), R.layout.fragment_business_list);
        setListAdapter(businessAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callback methods");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callback interface to the dummy implementation
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //Notify the active callback interface that the item has been selected
        BusinessDTO e = (BusinessDTO) l.getAdapter().getItem(position);
        // mCallbacks.onItemSelected(e.getBusId() + "");
        Intent intent = new Intent(getContext(), BusinessDiscountsActivity.class);
        intent.putExtra(BusinessDiscountsActivity.D_LIST_ACT_ID, e);
        startActivity(intent);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnITemClick) {
        //When setting choice mode single, listview will auto give items the activated state when touched
        getListView().setChoiceMode(activateOnITemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(mActivatedPosition, false);
        }
    }

    public void updateList(BusinessType type) {
        new LoadBusiness(type).execute();
    }

    private class LoadBusiness extends AsyncTask<List<BusinessDTO>, Void, List<BusinessDTO>> {

        private BusinessType businessType;

        private LoadBusiness(BusinessType businessType) {
            this.businessType = businessType;
        }

        @Override
        protected List<BusinessDTO> doInBackground(List... params) {
            List<BusinessDTO> c = new ArrayList<>();

            Connection connection = null;
            try {
                connection = DriverFactory.getTribiesConnection();
                BusinessDAO dao = new BusinessDAO(connection);
                switch (businessType) {
                    case MY_STORES:
                        break;
                    case STORES:
//                        c = dao.readBusinessByName();
                        break;
                    case MOST_RECENT:
//                        c = dao.readMostRecentBusiness();
                        break;
                }
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            return c;
        }

        @Override
        protected void onPostExecute(List<BusinessDTO> c) {
            if (c != null) {
                businessAdapter.updateList(c);
            }
        }
    }

}
