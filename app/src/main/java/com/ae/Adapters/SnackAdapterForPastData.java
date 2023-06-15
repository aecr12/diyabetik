package com.ae.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ae.DAO.BreakfastDAO;
import com.ae.DAO.SnackDAO;
import com.ae.Models.Breakfast;
import com.ae.Models.Snack;
import com.ae.diyabetik.R;

import java.util.ArrayList;

public class SnackAdapterForPastData extends RecyclerView.Adapter<SnackAdapterForPastData.ViewHolder>{

    private ArrayList<Snack> snackList;
    private Context context;
    private SnackDAO snackDAO = new SnackDAO();

    public SnackAdapterForPastData(ArrayList<Snack> snackList, Context context){
        this.snackList = snackList;
        this.context = context;
    }

    @Override
    public SnackAdapterForPastData.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.breakfast_food_card, parent, false);
        return new SnackAdapterForPastData.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(SnackAdapterForPastData.ViewHolder holder, int position) {
        Snack snack = snackList.get(position);
        holder.foodNameTextView.setText(String.valueOf(snack.getName()));
        holder.date.setText(snack.getDate());

    }
    @Override
    public int getItemCount() {
        return snackList.size();
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
