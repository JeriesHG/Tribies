/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2015.
 *
 */

package com.jerieshandal.tribies.business;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.jerieshandal.tribies.R;
import com.jerieshandal.tribies.utility.ImageUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * BusinessAdapter
 * Created by Jeries Handal on 12/29/2015.
 * Version 1.0.0
 */
public class BusinessAdapter extends ArrayAdapter<BusinessDTO> implements Filterable {

    private List<BusinessDTO> business;
    private BusinessAdapter adapter;
    private int resource;

    public BusinessAdapter(Context context, int resource, List<BusinessDTO> objects) {
        super(context, resource, objects);
        this.business = new ArrayList<>();
        this.business.addAll(objects);
        this.resource = resource;
        this.adapter = this;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout businessView;

        if (convertView == null) {
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            businessView = new RelativeLayout(getContext());

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(inflater);
            layoutInflater.inflate(resource, businessView, true);
        } else {
            businessView = (RelativeLayout) convertView;
        }

        //TODO: Bind DTO to the layout
        BusinessDTO e = getItem(position);

        //ImageView logo = (ImageView) parent.findViewById(R.id.businessLogo);
        //logo.setImageBitmap(ImageUtils.base64ToBitmap(e.getLogo()));

        TextView name = (TextView) businessView.findViewById(R.id.business_name);
        name.setText(e.getName());

        ToggleButton toggleButton = (ToggleButton) businessView.findViewById(R.id.like_button);
        toggleButton.setChecked(true);

        return businessView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    //No filter implemented, returning the list
                    results.values = business;
                    results.count = business.size();
                } else {
                    List<BusinessDTO> tempBusiness = new ArrayList<>();

                    for (BusinessDTO e : business) {
                        //TODO: Implement filter logic here
                    }

                    results.values = tempBusiness;
                    results.count = tempBusiness.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                adapter.clear();
                if (results.count == 0) {
                    notifyDataSetInvalidated();
                } else {
                    adapter.addAll((List<BusinessDTO>) results.values);
                    notifyDataSetChanged();
                }
            }
        };
    }
}
