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
import com.jerieshandal.tribies.image.ImageLoader;
import com.jerieshandal.tribies.utility.ImageUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * BusinessAdapter
 * Created by Jeries Handal on 12/29/2015.
 * Version 1.0.0
 */
public class BusinessAdapter extends ArrayAdapter<BusinessView> implements Filterable {

    private List<BusinessView> business;
    private BusinessAdapter adapter;
    private int resource;


    public BusinessAdapter(Context context, int resource) {
        super(context, resource);
        this.business = new ArrayList<>();
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
        BusinessView e = getItem(position);

        ImageView logo = (ImageView) businessView.findViewById(R.id.business_logo);
        ImageLoader imageLoader = new ImageLoader(businessView.getContext());
        imageLoader.displayImage(e.getLogo(), logo);

        TextView name = (TextView) businessView.findViewById(R.id.business_name);
        name.setText(e.getName());

        ToggleButton toggleButton = (ToggleButton) businessView.findViewById(R.id.like_button);
        toggleButton.setText(e.getBusId()+"");
        toggleButton.setChecked(e.isFavorite());
        toggleButton.setOnClickListener(toggleClickListener);

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
                    List<BusinessView> tempBusiness = new ArrayList<>();

                    for (BusinessView e : business) {
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
                    adapter.addAll((List<BusinessView>) results.values);
                    notifyDataSetChanged();
                }
            }
        };
    }

    private View.OnClickListener toggleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ToggleButton button = (ToggleButton) v;
            System.out.println(button.isChecked());
        }
    };

    public void updateList(List<BusinessView> c){
        adapter.clear();
        adapter.addAll(c);
        notifyDataSetChanged();
    }
}
