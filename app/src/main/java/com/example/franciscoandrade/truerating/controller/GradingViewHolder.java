package com.example.franciscoandrade.truerating.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.franciscoandrade.truerating.R;
import com.example.franciscoandrade.truerating.model.InspectionResultsModel;
import com.example.franciscoandrade.truerating.view.BusinessDetail;

import static java.lang.String.*;

/**
 * Created by melg on 3/3/18.
 */

public class GradingViewHolder extends RecyclerView.ViewHolder {
    private TextView restaurantName;
    private TextView address;
    private TextView phoneNumber;
    private TextView letterGrade, letter_gradePending;
    private String streetAddress;
    private String businessNumber;
    private String letter;
    private String pendingGrade;
    private String name;
    private String point;
    private LinearLayout cardLayout;
    private Context context;


    public GradingViewHolder(View itemView) {
        super(itemView);

        restaurantName = itemView.findViewById(R.id.restaurant_name);
        address = itemView.findViewById(R.id.restaurant_address);
        phoneNumber = itemView.findViewById(R.id.telephone_number);
        letterGrade = itemView.findViewById(R.id.letter_grade);


        letter_gradePending = itemView.findViewById(R.id.letter_gradePending);
        cardLayout = itemView.findViewById(R.id.iv_card);

    }

    public void setTextColor(String letter, TextView view) {
        if (letter.equals("A")) {
            view.setTextColor(view.getContext().getResources().getColor(R.color.blue));

        }else if (letter.equals("B")) {
            view.setTextColor(view.getContext().getResources().getColor(R.color.green));
        }
        else if (letter.equals("C")) {
        view.setTextColor(view.getContext().getResources().getColor(R.color.orange));
        }


    }

    public void onbind(final InspectionResultsModel inspectionResultsModel) {

        name = inspectionResultsModel.getDba();
        streetAddress = inspectionResultsModel.getBuilding() + " " + inspectionResultsModel.getStreet()
                + "\n" + inspectionResultsModel.getBoro() + " , " + inspectionResultsModel.getZipcode();

        businessNumber = inspectionResultsModel.getPhone();

        letter = inspectionResultsModel.getGrade();



        pendingGrade = "Pending";
        point = inspectionResultsModel.getScore();

        restaurantName.setText(name);
        address.setText(streetAddress);
        phoneNumber.setText(businessNumber);
        if (inspectionResultsModel.getGrade() != null){
        setTextColor(letter, letterGrade);
        }
        if (inspectionResultsModel.getGrade() == null || inspectionResultsModel.getGrade().equals(pendingGrade)) {
            letter_gradePending.setVisibility(View.VISIBLE);
            letterGrade.setVisibility(View.GONE);

        } else {
            letter_gradePending.setVisibility(View.GONE);
            letterGrade.setVisibility(View.VISIBLE);


        }
        letterGrade.setText(letter);

        cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), BusinessDetail.class);

                intent.putExtra("code", inspectionResultsModel.getViolation_code());
                intent.putExtra("desc", inspectionResultsModel.getViolation_description());
                intent.putExtra("critical", inspectionResultsModel.getCritical_flag());
                intent.putExtra("address", streetAddress);
                intent.putExtra("name", name);
                intent.putExtra("point", point);

                if (letter != "Not Yet Graded") {
                    intent.putExtra("grade", pendingGrade);
                }
                if (letter != null) {
                    intent.putExtra("grade", letter);
                } else {
                    intent.putExtra("grade", pendingGrade);
                }

                itemView.getContext().startActivity(intent);
            }
        });
    }
}
