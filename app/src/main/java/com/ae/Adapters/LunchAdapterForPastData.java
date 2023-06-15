package com.ae.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ae.DAO.BreakfastDAO;
import com.ae.DAO.LunchDAO;
import com.ae.Models.Breakfast;
import com.ae.Models.Lunch;
import com.ae.diyabetik.R;

import java.util.ArrayList;

public class LunchAdapterForPastData extends RecyclerView.Adapter<LunchAdapterForPastData.ViewHolder>{

    private ArrayList<Lunch> lunchList;
    private Context context;
    private LunchDAO lunchDAO = new LunchDAO();

    public LunchAdapterForPastData(ArrayList<Lunch> lunchList, Context context){
        this.lunchList = lunchList;
        this.context = context;
    }

    @Override
    public LunchAdapterForPastData.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.breakfast_food_card, parent, false);
        return new LunchAdapterForPastData.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(LunchAdapterForPastData.ViewHolder holder, int position) {
        Lunch lunch = lunchList.get(position);
        holder.foodNameTextView.setText(String.valueOf(lunch.getName()));
        holder.date.setText(lunch.getDate());

    }
    @Override
    public int getItemCount() {
        return lunchList.size();
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
