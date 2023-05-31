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

import com.ae.DAO.DinnerDAO;
import com.ae.Models.Dinner;
import com.ae.diyabetik.R;
import com.ae.diyabetik.SwipeToDeleteCallback;

import java.util.ArrayList;

public class DinnerAdapter extends RecyclerView.Adapter<DinnerAdapter.ViewHolder> {
    private ArrayList<Dinner> dinnerList;
    private Context context;
    DinnerDAO dinnerDAO = new DinnerDAO();
    EditText dinnerNameEditText;

    public DinnerAdapter(ArrayList<Dinner> dinnerList, Context context) {
        this.dinnerList = dinnerList;
        this.context = context;
    }

    private final ItemTouchHelper.Callback swipeToDeleteCallback = new SwipeToDeleteCallback() {
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            Dinner dinner = dinnerList.get(position);
            String dinnerId = dinner.getId();
            dinnerDAO.delete(dinnerId);
            dinnerList.remove(position);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Dinner dinner = dinnerList.get(position);
        holder.foodNameTextView.setText(String.valueOf(dinner.getName()));
        holder.date.setText(dinner.getDate());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showUpdateDinnerDialog(dinner, position);
                return true;
            }
        });
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

    private void showUpdateDinnerDialog(Dinner dinner, int adapterPosition) {

        Dialog dialog1 = new Dialog(context);
        dialog1.setContentView(R.layout.dialog_update_meal);
        dinnerNameEditText = dialog1.findViewById(R.id.editTextFoodName);
        Button updateButton = dialog1.findViewById(R.id.buttonUpdate);
        Button cancelButton = dialog1.findViewById(R.id.buttonCancel);

        dinnerNameEditText.setText(dinner.getName());


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDinnerName(dinner, adapterPosition);
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

    private void updateDinnerName(Dinner dinnerToUpdate, int adapterPosition) {
        String dinnerName = dinnerNameEditText.getText().toString();
        dinnerToUpdate.setName(dinnerName);

        dinnerDAO.update(dinnerToUpdate);
        notifyItemChanged(adapterPosition);
    }
}
