/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jerieshandal.tribies.business.BusinessDTO;
import com.jerieshandal.tribies.discount.DiscountAdapter;
import com.jerieshandal.tribies.discount.DiscountDTO;
import com.jerieshandal.tribies.utility.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusinessDiscountsActivity extends AppCompatActivity {

    public static final String D_LIST_ACT_ID = "DISCOUNT_LIST";

    private RecyclerView mRecyclerView;

    private Toolbar toolbar;
    private ImageButton moreInformation;
    private BusinessDTO selectedBusiness;
    private TextView mBusinessToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_discount);

        mRecyclerView = (RecyclerView) findViewById(R.id.discount_list);
        mRecyclerView.setAdapter(new DiscountAdapter(mRecyclerView, mockItems()));
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        selectedBusiness = (BusinessDTO) getIntent().getSerializableExtra(D_LIST_ACT_ID);

        moreInformation = (ImageButton) findViewById(R.id.business_more_info_btn);

        moreInformation.setOnClickListener(moreInformationClickListener);

        mBusinessToolbarTitle = (TextView) findViewById(R.id.business_toolbar_title);
        mBusinessToolbarTitle.setTypeface(StringUtils.retrieveTitleFont(getAssets()));
        mBusinessToolbarTitle.setText(selectedBusiness.getName());

        toolbar = (Toolbar) findViewById(R.id.discounts_toolbar);
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

    private View.OnClickListener moreInformationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(BusinessDiscountsActivity.this, BusinessInformationActivity.class);
            intent.putExtra(BusinessInformationActivity.BUSINESS_ID_TAG, selectedBusiness);
            startActivity(intent);
        }
    };

    private List<DiscountDTO> mockItems(){
        List<DiscountDTO> c = new ArrayList<>();

        DiscountDTO e = new DiscountDTO();
        e.setDiscId(1);
        e.setDescription("Descripcion del descuento numero 1");
        e.setCreated(new Date());
        e.setExpiry(new Date());
        e.setTitle("Descuento 1");
        c.add(e);

        e = new DiscountDTO();
        e.setDiscId(2);
        e.setDescription("Descripcion del descuento numero 2");
        e.setCreated(new Date());
        e.setExpiry(new Date());
        e.setTitle("Descuento 2");
        c.add(e);

        return c;
    }
}
