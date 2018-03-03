package com.example.franciscoandrade.truerating.controller;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.franciscoandrade.truerating.R;

/**
 * Created by melg on 3/3/18.
 */

public class GradingViewHolder extends RecyclerView.ViewHolder {
    private TextView restaurantName;
    private TextView address;
    private TextView phoneNumber;
    private ImageView letterGrade;
    public GradingViewHolder(View itemView) {
        super(itemView);

        restaurantName = itemView.findViewById(R.id.restaurant_name);
        address = itemView.findViewById(R.id.restaurant_address);
        phoneNumber = itemView.findViewById(R.id.telephone_number);
        letterGrade = itemView.findViewById(R.id.letter_grade);

    }
    public void onbind(){

    }
}
