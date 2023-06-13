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
import com.ae.DAO.LunchDAO;
import com.ae.Models.Lunch;
import com.ae.diyabetik.R;
import com.ae.diyabetik.SwipeToDeleteCallback;

import java.util.ArrayList;

public class LunchAdapter extends RecyclerView.Adapter<LunchAdapter.ViewHolder> {

    private ArrayList<Lunch> lunchList;
    private Context context;
    private LunchDAO lunchDAO = new LunchDAO();
    private EditText lunchNameEditText;

    public LunchAdapter(ArrayList<Lunch> lunchList, Context context) {
        this.lunchList = lunchList;
        this.context = context;
    }

    private final ItemTouchHelper.Callback swipeToDeleteCallback = new SwipeToDeleteCallback() {
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            Lunch lunch = lunchList.get(position);
            String lunchId = lunch.getId();
            lunchDAO.delete(lunchId);
            lunchList.remove(position);
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
    public LunchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.breakfast_food_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LunchAdapter.ViewHolder holder, int position) {
        Lunch lunch = lunchList.get(position);
        holder.foodNameTextView.setText(String.valueOf(lunch.getName()));
        holder.date.setText(lunch.getDate());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showUpdateLunchDialog(lunch, position);
                return true;
            }
        });
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

    private void showUpdateLunchDialog(Lunch lunch, int adapterPosition) {

        Dialog dialog1 = new Dialog(context);
        dialog1.setContentView(R.layout.dialog_update_meal);
        lunchNameEditText = dialog1.findViewById(R.id.editTextFoodName);
        Button updateButton = dialog1.findViewById(R.id.buttonUpdate);
        Button cancelButton = dialog1.findViewById(R.id.buttonCancel);

        lunchNameEditText.setText(lunch.getName());


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLunchName(lunch, adapterPosition);
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

    private void updateLunchName(Lunch lunchToUpdate, int adapterPosition) {
        String lunchName = lunchNameEditText.getText().toString();
        lunchToUpdate.setName(lunchName);

        lunchDAO.update(lunchToUpdate);
        notifyItemChanged(adapterPosition);
    }
}
