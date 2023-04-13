package com.ae.diyabetik;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.ae.Models.BloodSugar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;


public class BloodSugarAdapter extends RecyclerView.Adapter<BloodSugarAdapter.ViewHolder> implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private ArrayList<BloodSugar> bloodSugarList;
    private int year, month, day, hour, minute;
    private Context context;
    EditText bloodSugarValueEditText;
    EditText dateEditText;


    public BloodSugarAdapter(ArrayList<BloodSugar> bloodSugarList, Context context) {
        this.context = context;
        this.bloodSugarList = bloodSugarList;
    }

    private final ItemTouchHelper.Callback swipeToDeleteCallback = new SwipeToDeleteCallback() {
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            BloodSugar bloodSugar = bloodSugarList.get(position);
            String bloodSugarId = bloodSugar.getId();
            DatabaseReference blood_sugar_data_ref = FirebaseDatabase.getInstance().getReference().child("blood_sugar_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(bloodSugarId);
            blood_sugar_data_ref.removeValue();
            bloodSugarList.remove(position);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blood_sugar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BloodSugar bloodSugar = bloodSugarList.get(position);
        holder.bloodSugarValueTextView.setText(String.valueOf(bloodSugar.getBloodSugarValue()));
        holder.dateTextView.setText(bloodSugar.getDate());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showUpdateBloodSugarDialog(bloodSugar, position);
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BloodSugar bloodSugar1 = bloodSugarList.get(position);
                String selectedValue = String.valueOf(bloodSugar1.getBloodSugarValue());
                Intent intent = new Intent(context,BloodSugarWithHBA1CDiagram.class);
                intent.putExtra("selectedValue",Float.parseFloat(selectedValue));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bloodSugarList.size();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        year = year;
        month = month;
        day = dayOfMonth;

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, this, hour, minute, DateFormat.is24HourFormat(context));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hour = hourOfDay;
        minute = minute;

        String selectedDateTime = day + "/" + (month + 1) + "/" + year + " " + String.format("%02d", hour) + ":" + String.format("%02d", minute);
        dateEditText.setText(selectedDateTime);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bloodSugarValueTextView;
        public TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            bloodSugarValueTextView = itemView.findViewById(R.id.textViewBloodSugarValue);
            dateTextView = itemView.findViewById(R.id.textViewDate);
        }
    }

    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, this, year, month, day);
        datePickerDialog.show();
    }
    private void showUpdateBloodSugarDialog(BloodSugar bloodSugar, int adapterPosition) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_update_blood_sugar);
        bloodSugarValueEditText = dialog.findViewById(R.id.editTextBloodSugarValue);
        dateEditText = dialog.findViewById(R.id.editTextDate);
        dateEditText.setEnabled(false);
        ImageButton calendarImageButton = dialog.findViewById(R.id.imageButtonCalendar);
        Button updateButton = dialog.findViewById(R.id.buttonUpdate);
        Button cancelButton = dialog.findViewById(R.id.buttonCancel);

        bloodSugarValueEditText.setText(String.valueOf(bloodSugar.getBloodSugarValue()));
        dateEditText.setText(bloodSugar.getDate());

        calendarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBloodSugar(bloodSugar, adapterPosition);
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout((int) (0.8 * Resources.getSystem().getDisplayMetrics().widthPixels),
                (int) (0.55 * Resources.getSystem().getDisplayMetrics().heightPixels));
    }

    private void updateBloodSugar(BloodSugar bloodSugarToUpdate, int adapterPosition) {
        String bloodSugarValue = bloodSugarValueEditText.getText().toString();
        String date = dateEditText.getText().toString();
        bloodSugarToUpdate.setBloodSugarValue(Integer.parseInt(bloodSugarValue));
        bloodSugarToUpdate.setDate(date);
        notifyItemChanged(adapterPosition);
    }
}
