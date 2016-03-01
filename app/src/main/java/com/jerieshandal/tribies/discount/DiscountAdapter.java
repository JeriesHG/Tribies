/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.discount;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jerieshandal.tribies.BusinessDiscountsActivity;
import com.jerieshandal.tribies.BusinessInformationActivity;
import com.jerieshandal.tribies.DiscountDetailsActivity;
import com.jerieshandal.tribies.R;
import com.jerieshandal.tribies.utility.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * DiscountAdapter
 * Created by Jeries Handal on 2/27/2016.
 * Version 1.0.0
 */
public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder>{

    private RecyclerView mRecyclerView;
    private List<DiscountDTO> discountList;
    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public DiscountAdapter(RecyclerView recyclerView, List<DiscountDTO> items) {
        this.mRecyclerView = recyclerView;
        discountList = items;
    }

    @Override
    public DiscountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_discount_container, parent, false);

        return new DiscountViewHolder(mRecyclerView, itemView);
    }

    @Override
    public void onBindViewHolder(DiscountViewHolder holder, int position) {
        DiscountDTO e = discountList.get(position);
        // holder.ivIcon.setImageDrawable(e.icon);
        holder.bTitle.setText(e.getTitle());
        holder.bShortDescr.setText(e.getDescription());
        holder.bThumbsUpCounter.setText("213");
        holder.bExpireDate.setText(DATE_FORMAT.format(e.getExpiry()));
    }

    @Override
    public int getItemCount() {
        return discountList.size();
    }


    public static class DiscountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected ImageView bThumbnail;
        protected TextView bTitle;
        protected TextView bShortDescr;
        protected TextView bThumbsUpCounter;
        protected TextView bExpireDate;
        protected RecyclerView mRecyclerView;

        public DiscountViewHolder(RecyclerView recyclerView, View view) {
            super(view);
            mRecyclerView = recyclerView;
            view.setOnClickListener(this);
            bThumbnail = (ImageView) view.findViewById(R.id.discount_thumbnail);
            bTitle = (TextView) view.findViewById(R.id.discount_title);
            bShortDescr = (TextView) view.findViewById(R.id.discount_short_descr);
            bThumbsUpCounter = (TextView) view.findViewById(R.id.discount_thumbs_up_counter);
            bExpireDate = (TextView) view.findViewById(R.id.discount_expire_date);
        }

        @Override
        public void onClick(View v) {
            DiscountViewHolder holder = (DiscountViewHolder) mRecyclerView.getChildViewHolder(v);
            Intent intent = new Intent(v.getContext(), DiscountDetailsActivity.class);
            intent.putExtra(DiscountDetailsActivity.DISCOUNT_DETAIL_ID, holderToDTO(holder));
            v.getContext().startActivity(intent);
        }

        private DiscountDTO holderToDTO(DiscountViewHolder holder){
            DiscountDTO e = new DiscountDTO();
            e.setTitle((String) holder.bTitle.getText());
            e.setDescription((String) holder.bShortDescr.getText());

            return e;
        }
    }
}
