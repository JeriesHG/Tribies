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
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.jerieshandal.tribies.BusinessDiscountsActivity;
import com.jerieshandal.tribies.R;
import com.jerieshandal.tribies.utility.Callbacks;

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
    /**
     * A dummy implementation of the {@link Callbacks} enum that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
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

    public BusinessListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: replace hardcoded list with databse one
        setListAdapter(new BusinessAdapter(getContext(), R.layout.fragment_business_list, mockList()));
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
        Intent intent = new Intent(getContext(),BusinessDiscountsActivity.class);
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

    public void setActivateOnITemClick(boolean activateOnITemClick) {
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

    private List<BusinessDTO> mockList() {
        List<BusinessDTO> c = new ArrayList<>();

        BusinessDTO e1 = new BusinessDTO();
        e1.setBusId(1);
        e1.setName("Negocio 1");
        e1.setLogo("citizen_logo_asibp2.jpg");
        e1.setPhone("+50425520501");
        e1.setEmail("negocio1@hotmail.com");
        e1.setAddress("3 calle, San Pedro Sula 00504, Honduras");
        c.add(e1);

        BusinessDTO e2 = new BusinessDTO();
        e2.setBusId(2);
        e2.setName("Negocio 2");
        e2.setLogo("zara_logo_mz22cs.png");
        e2.setPhone("+504225201051");
        e2.setEmail("negocio2@hotmail.com");
        e2.setAddress("16 calle S.O. | San Pedro Sula, Col., San Pedro Sula 21104, Honduras");
        c.add(e2);

        return c;
    }

}
