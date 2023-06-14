package com.ae.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ae.DAO.BreakfastDAO;
import com.ae.Models.Breakfast;
import com.ae.diyabetik.R;


import java.util.ArrayList;
public class BreakfastAdapterForPastData extends RecyclerView.Adapter<BreakfastAdapterForPastData.ViewHolder> {

    private ArrayList<Breakfast> breakfastList;
    private Context context;
    private BreakfastDAO breakfastDAO = new BreakfastDAO();

    public BreakfastAdapterForPastData(ArrayList<Breakfast> breakfastList, Context context){
        this.breakfastList = breakfastList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.breakfast_food_card, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(BreakfastAdapterForPastData.ViewHolder holder, int position) {
        Breakfast breakfast = breakfastList.get(position);
        holder.foodNameTextView.setText(String.valueOf(breakfast.getName()));
        holder.date.setText(breakfast.getDate());

    }
    @Override
    public int getItemCount() {
        return breakfastList.size();
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
