package com.ae.diyabetik;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BreakfastTracker extends AppCompatActivity {

    private EditText editTextBreakfast;
    private ImageView breakfastImage;
    private RecyclerView foodListBreakfast;
    private FloatingActionButton floatingActionButton;
    private EditText editTextPortion;
    private ArrayList<Food> breakfastItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breakfast_tracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        breakfastItemList = new ArrayList<>();
        editTextBreakfast = findViewById(R.id.editTextBreakfast);
        breakfastImage = findViewById(R.id.breakfast_image);
        foodListBreakfast = findViewById(R.id.foodListBreakfast);
        editTextPortion = findViewById(R.id.editTextPortion);
        floatingActionButton = findViewById(R.id.fab);

        // kullanıcı veri girince FAB görünür olacak, bu yüzden en başta gizliyorum
        floatingActionButton.hide();

        // RecyclerView'ı ayarla
        foodListBreakfast.setLayoutManager(new LinearLayoutManager(this));
        foodListBreakfast.setHasFixedSize(true);
        FoodListAdapter adapter = new FoodListAdapter(breakfastItemList,this);
        foodListBreakfast.setAdapter(adapter);

        // Kullanıcı girdi yapınca FAB görünür olacak, kullanıcı girsini takip etmek için textwatcherlar ekleniyor
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // editTextBreakfast ve editTextPortion doluysa FAB'ı göster
                String foodName = editTextBreakfast.getText().toString().trim();
                String portion = editTextPortion.getText().toString().trim();
                if (!foodName.isEmpty() && !portion.isEmpty()) {
                    floatingActionButton.show();
                } else {
                    floatingActionButton.hide();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        editTextBreakfast.addTextChangedListener(textWatcher);
        editTextPortion.addTextChangedListener(textWatcher);

        // FABa tıklanınca yapılacaklar
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = editTextBreakfast.getText().toString();
                String portion = editTextPortion.getText().toString();

                if (!foodName.isEmpty() && !portion.isEmpty()) {
                    Food food = new Food(foodName, portion);
                    breakfastItemList.add(food);
                    adapter.notifyDataSetChanged();
                    editTextBreakfast.setText("");
                    editTextPortion.setText("");
                    floatingActionButton.hide();
                }
            }
        });
    }

    /*// RecyclerView Adapter sınıfı
    private class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

        private ArrayList<Food> list;

        public FoodListAdapter(ArrayList<Food> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_breakfast, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Food food = breakfastItemList.get(position);
            holder.foodNameTextView.setText(list.get(position).getName());
            holder.portionTextView.setText(list.get(position).getPortion());
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showUpdateBreakfastDialog(food,position);
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
                breakfastItemList.remove(position);
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
        private void showUpdateBreakfastDialog(Food food, int adapterPosition){
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_update_meal);

            EditText editTextFoodName = dialog.findViewById(R.id.editTextFoodName);
            EditText editTextPortion = dialog.findViewById(R.id.editTextPortion);
            Button updateButton = dialog.findViewById(R.id.buttonUpdate);
            Button cancelButton = dialog.findViewById(R.id.buttonCancel);

            editTextFoodName.setText(food.getName());
            editTextPortion.setText(food.getPortion());

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateFood(food, adapterPosition);
                    dialog.dismiss();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        private void updateFood(Food foodToUpdate, int adapterPosition){
            String foodName = editTextBreakfast.getText().toString();
            String portion = editTextPortion.getText().toString();
            foodToUpdate.setName(foodName);
            foodToUpdate.setPortion(portion);
            notifyItemChanged(adapterPosition);
        }
    }*/

    // Food sınıfı (model)
    /*public class Food {
        private String name;
        private String portion;

        public Food(String name, String portion) {
            this.name = name;
            this.portion = portion;
        }

        public String getName() {
            return name;
        }

        public String getPortion() {
            return portion;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPortion(String portion) {
            this.portion = portion;
        }
    }*/

}


