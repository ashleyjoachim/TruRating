package com.franciscoandrade.truerating.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.franciscoandrade.truerating.R;

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
    private String status;
    private String points;
    private String description;
    private String code;
    private String name;
    private String address;
    private String violationPoint;
    private String grade;


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

        showToolBar("", true);
        Intent intent = getIntent();

        code = intent.getStringExtra("code");
        description = intent.getStringExtra("desc");
        status = intent.getStringExtra("critical");
        address = intent.getStringExtra("address");
        grade = intent.getStringExtra("grade");

        if (intent.getStringExtra("grade") != null){
            setTextColor(grade, letterGrade);
        }
        name = intent.getStringExtra("name");
        points = intent.getStringExtra("point");

        Log.e("TAG", "onCreate: "+ code );
        Log.e("TAG", "onCreate: "+ grade );
        Log.e("TAG", "onCreate: "+ name );
        Log.e("TAG", "onCreate: "+ points );
        Log.e("TAG", "onCreate: "+ status );
        Log.e("TAG", "onCreate: "+ description );
        Log.e("TAG", "onCreate: "+ address );


        try {
            businessName.setText(name);
            addressLine.setText(address);
            letterGrade.setText(grade);
            letterGrade.setTextSize(70);
            critical.setText("Status: " + status);
            violationDescription.setText("Violation Description: " + "\n" + description);
            violationCodes.setText("Violation Code: " + code);
            violationPoints.setText("Violation Points: " + points);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("ResourceAsColor")
    private void showToolBar(String tittle, boolean upButton) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
        toolbar.getNavigationIcon()
                .setColorFilter(getResources()
                        .getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Intent intent = new Intent(this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
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
        else if (letter.equals("Z")) {
            view.setTextColor(view.getContext().getResources().getColor(R.color.green));
        }
    }
}
