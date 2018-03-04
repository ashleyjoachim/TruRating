package com.example.franciscoandrade.truerating.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.franciscoandrade.truerating.R;

/**
 * Created by melg on 3/3/18.
 */

public class BusinessDetail extends AppCompatActivity {

    private TextView businessName;
    private TextView addressLine;
    private TextView critical;
    private TextView violationPoints;
    private TextView violationCodes;
    private TextView violationDescription;
    private TextView letterGrade;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        businessName = findViewById(R.id.name_restaurant_detail);
        addressLine = findViewById(R.id.business_address_detail);
        critical = findViewById(R.id.critical_flag);
        violationPoints = findViewById(R.id.points);
        violationCodes = findViewById(R.id.violation_code);
        violationDescription = findViewById(R.id.violation_des);
        letterGrade = findViewById(R.id.lettergrade_detail);

        try {
            businessName.setText(getIntent().getExtras().getString("name"));
            addressLine.setText(getIntent().getExtras().get("address").toString());
            letterGrade.setText(getIntent().getExtras().get("grade").toString());
            critical.setText("Status: " + getIntent().getExtras().get("critical").toString());
            violationDescription.setText("Violation Description: " + getIntent().getExtras().get("desc").toString());
            violationCodes.setText("Violation Code: " + getIntent().getExtras().get("code").toString());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
