/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.jerieshandal.tribies.dto.DiscountDTO;
import com.jerieshandal.tribies.utility.StringUtils;

public class DiscountDetailsActivity extends AppCompatActivity {

    public final static String DISCOUNT_DETAIL_ID = "Discount Detail";
    private TextView mDiscountToolbarTitle;
    private Toolbar toolbar;
    private SliderLayout mSliderShow;
    private DiscountDTO selectedDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_details);

        selectedDiscount = (DiscountDTO) getIntent().getSerializableExtra(DISCOUNT_DETAIL_ID);

        mDiscountToolbarTitle = (TextView) findViewById(R.id.discount_toolbar_title);
        mDiscountToolbarTitle.setTypeface(StringUtils.retrieveTitleFont(getAssets()));
        mDiscountToolbarTitle.setText(selectedDiscount.getTitle());

        toolbar = (Toolbar) findViewById(R.id.discounts_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mSliderShow = (SliderLayout) findViewById(R.id.discount_slider);
        DefaultSliderView sliderView = new DefaultSliderView(this);
        sliderView.image("http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");
        mSliderShow.addSlider(sliderView);

        DefaultSliderView sliderView1 = new DefaultSliderView(this);
        sliderView1.image("https://s-media-cache-ak0.pinimg.com/236x/36/ab/81/36ab81cd8d63cf7c4a08f39403698c77.jpg");
        mSliderShow.addSlider(sliderView1);

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

    @Override
    protected void onStop() {
        mSliderShow.stopAutoCycle();
        super.onStop();
    }
}
