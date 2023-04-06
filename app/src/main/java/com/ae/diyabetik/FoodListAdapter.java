package com.ae.diyabetik;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {
    private ArrayList<Food> list;
    private Context context;
    EditText editTextFoodName;
    EditText editTextPortion;


    public FoodListAdapter(ArrayList<Food> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_breakfast, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = list.get(position);
        holder.foodNameTextView.setText(food.getName());
        holder.portionTextView.setText(food.getPortion());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showUpdateBreakfastDialog(food, position);
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView foodNameTextView;
        private TextView portionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.text1);
            portionTextView = itemView.findViewById(R.id.text2);
        }
    }

    // swipe to delete işlemi
    private final ItemTouchHelper.Callback swipeToDeleteCallback = new SwipeToDeleteCallback() {
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            list.remove(position);
            notifyItemRemoved(position);
        }
    };
    private final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    // güncelleme işlemi için gerekli işlemler
    private void showUpdateBreakfastDialog(Food food, int adapterPosition) {

        Dialog dialog1 = new Dialog(context);
        dialog1.setContentView(R.layout.dialog_update_meal);
        editTextFoodName = dialog1.findViewById(R.id.editTextFoodName);
        editTextPortion = dialog1.findViewById(R.id.editTextPortion);
        Button updateButton = dialog1.findViewById(R.id.buttonUpdate);
        Button cancelButton = dialog1.findViewById(R.id.buttonCancel);

        editTextFoodName.setText(food.getName());
        editTextPortion.setText(food.getPortion());


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = editTextFoodName.getText().toString().trim();
                String portion = editTextPortion.getText().toString().trim();

                if (!foodName.isEmpty() && !portion.isEmpty()) {
                    food.setName(foodName);
                    food.setPortion(portion);
                    notifyItemChanged(adapterPosition);
                    dialog1.dismiss();
                } else {
                    Toast.makeText(context, "Lütfen boş alanları doldurunuz.", Toast.LENGTH_SHORT).show();
                }
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
}
