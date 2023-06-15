package com.ae.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ae.DAO.DinnerDAO;

import com.ae.Models.Dinner;
import com.ae.diyabetik.R;

import java.util.ArrayList;

public class DinnerAdapterForPastData extends RecyclerView.Adapter<DinnerAdapterForPastData.ViewHolder> {

    private ArrayList<Dinner> dinnerList;
    private Context context;
    private DinnerDAO dinnerDAO = new DinnerDAO();

    public DinnerAdapterForPastData(ArrayList<Dinner> dinnerList, Context context){
        this.dinnerList = dinnerList;
        this.context = context;
    }

    @Override
    public DinnerAdapterForPastData.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.breakfast_food_card, parent, false);
        return new DinnerAdapterForPastData.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(DinnerAdapterForPastData.ViewHolder holder, int position) {
        Dinner dinner = dinnerList.get(position);
        holder.foodNameTextView.setText(String.valueOf(dinner.getName()));
        holder.date.setText(dinner.getDate());

    }
    @Override
    public int getItemCount() {
        return dinnerList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView foodNameTextView;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            date = itemView.findViewById(R.id.date);
        }
    }

}
