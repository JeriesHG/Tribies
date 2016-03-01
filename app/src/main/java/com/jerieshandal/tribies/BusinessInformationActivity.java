/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.jerieshandal.tribies.business.BusinessDTO;
import com.jerieshandal.tribies.utility.StringUtils;

import org.w3c.dom.Text;

public class BusinessInformationActivity extends AppCompatActivity {

    public static final String BUSINESS_ID_TAG = "Business Information";
    private BusinessDTO selectedBusiness;
    private TextView mBusinessToolbarTitle;
    private TextView mTitleView;
    private TextView mEmailView;
    private TextView mPhoneView;
    private TextView mAddressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_information);

        selectedBusiness = (BusinessDTO) getIntent().getSerializableExtra(BUSINESS_ID_TAG);
        mBusinessToolbarTitle = (TextView) findViewById(R.id.business_toolbar_title);
        mBusinessToolbarTitle.setTypeface(StringUtils.retrieveTitleFont(getAssets()));
        mBusinessToolbarTitle.setText(getString(R.string.title_activity_business_information).concat(" ").concat(selectedBusiness.getName()));

        mTitleView = (TextView) findViewById(R.id.business_name);
        mTitleView.setText(selectedBusiness.getName());

        mEmailView = (TextView) findViewById(R.id.business_email_text);
        mEmailView.setText(selectedBusiness.getEmail());

        mPhoneView = (TextView) findViewById(R.id.business_phone_text);
        mPhoneView.setText(selectedBusiness.getPhone());

        mAddressView = (TextView) findViewById(R.id.business_addresse_text);
        mAddressView.setText(selectedBusiness.getAddress());

        Toolbar toolbar = (Toolbar) findViewById(R.id.business_information_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
