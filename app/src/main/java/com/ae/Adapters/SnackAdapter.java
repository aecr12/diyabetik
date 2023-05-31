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

import com.ae.DAO.SnackDAO;

import com.ae.Models.Snack;
import com.ae.diyabetik.R;
import com.ae.diyabetik.SwipeToDeleteCallback;

import java.util.ArrayList;

public class SnackAdapter extends RecyclerView.Adapter<SnackAdapter.ViewHolder>{

    private ArrayList<Snack> snackList;
    private Context context;
    SnackDAO snackDAO = new SnackDAO();
    EditText snackNameEditText;

    public SnackAdapter(ArrayList<Snack> snackList, Context context) {
        this.snackList = snackList;
        this.context = context;
    }

    private final ItemTouchHelper.Callback swipeToDeleteCallback = new SwipeToDeleteCallback() {
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            Snack snack = snackList.get(position);
            String snackId = snack.getId();
            snackDAO.delete(snackId);
            snackList.remove(position);
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
        Snack snack = snackList.get(position);
        holder.foodNameTextView.setText(String.valueOf(snack.getName()));
        holder.date.setText(snack.getDate());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showUpdateSnackDialog(snack, position);
                return true;
            }
        });
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

    private void showUpdateSnackDialog(Snack snack, int adapterPosition) {

        Dialog dialog1 = new Dialog(context);
        dialog1.setContentView(R.layout.dialog_update_meal);
        snackNameEditText = dialog1.findViewById(R.id.editTextFoodName);
        Button updateButton = dialog1.findViewById(R.id.buttonUpdate);
        Button cancelButton = dialog1.findViewById(R.id.buttonCancel);

        snackNameEditText.setText(snack.getName());


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSnackName(snack, adapterPosition);
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

    private void updateSnackName(Snack snackToUpdate, int adapterPosition) {
        String snackName = snackNameEditText.getText().toString();
        snackToUpdate.setName(snackName);

        snackDAO.update(snackToUpdate);
        notifyItemChanged(adapterPosition);
    }

}
