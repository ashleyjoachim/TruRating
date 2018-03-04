package com.example.franciscoandrade.truerating.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.franciscoandrade.truerating.R;
import com.example.franciscoandrade.truerating.model.InspectionResultsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franciscoandrade on 3/3/18.
 */

public class GradingAdapter extends RecyclerView.Adapter<GradingViewHolder> {

    List<InspectionResultsModel> listGrades;
    Context context;


    public GradingAdapter(Context context){

        this.context= context;
        listGrades= new ArrayList<>();
    }

    @Override
    public GradingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_view, parent, false);
        return new GradingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GradingViewHolder holder, int position) {
        InspectionResultsModel inspectionResultsModel= listGrades.get(position);
        holder.onbind(inspectionResultsModel);



    }

    @Override
    public int getItemCount() {
        return listGrades.size();
    }

    public void adGrades(List<InspectionResultsModel> InspectionResultsModel) {
        listGrades.clear();
        listGrades.addAll(InspectionResultsModel);
        notifyDataSetChanged();
    }
}
