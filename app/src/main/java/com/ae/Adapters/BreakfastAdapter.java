package com.ae.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.ae.DAO.BreakfastDAO;
import com.ae.Models.Breakfast;
import com.ae.diyabetik.R;
import com.ae.diyabetik.SwipeToDeleteCallback;

import java.util.ArrayList;

public class BreakfastAdapter extends RecyclerView.Adapter<BreakfastAdapter.ViewHolder> {

    private ArrayList<Breakfast> breakfastList;
    private Context context;
    private BreakfastDAO breakfastDAO = new BreakfastDAO();
    private EditText breakfastNameEditText;

     public BreakfastAdapter(ArrayList<Breakfast> breakfastList, Context context){
         this.breakfastList = breakfastList;
         this.context = context;
     }
    private final ItemTouchHelper.Callback swipeToDeleteCallback = new SwipeToDeleteCallback() {
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            Breakfast breakfast = breakfastList.get(position);
            String breakfastId = breakfast.getId();
            breakfastDAO.delete(breakfastId);
            breakfastList.remove(position);
            notifyItemRemoved(position);
        }
    };
    private final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.breakfast_food_card, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(BreakfastAdapter.ViewHolder holder, int position) {
        Breakfast breakfast = breakfastList.get(position);
        holder.foodNameTextView.setText(String.valueOf(breakfast.getName()));
        holder.date.setText(breakfast.getDate());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showUpdateBreakfastDialog(breakfast, position);
                return true;
            }
        });
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
    private void showUpdateBreakfastDialog(Breakfast breakfast, int adapterPosition) {

        Dialog dialog1 = new Dialog(context);
        dialog1.setContentView(R.layout.dialog_update_meal);
        breakfastNameEditText = dialog1.findViewById(R.id.editTextFoodName);
        Button updateButton = dialog1.findViewById(R.id.buttonUpdate);
        Button cancelButton = dialog1.findViewById(R.id.buttonCancel);
        breakfastNameEditText.setText(breakfast.getName());

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             updateBreakfastName(breakfast,adapterPosition);
             dialog1.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
        Window window = dialog1.getWindow();
        window.setLayout((int) (0.8 * Resources.getSystem().getDisplayMetrics().widthPixels),
                (int) (0.55 * Resources.getSystem().getDisplayMetrics().heightPixels));
    }
    private void updateBreakfastName(Breakfast breakfastToUpdate, int adapterPosition){
        String breakfastName = breakfastNameEditText.getText().toString();
        breakfastToUpdate.setName(breakfastName);

        breakfastDAO.update(breakfastToUpdate);
        notifyItemChanged(adapterPosition);
    }
}
