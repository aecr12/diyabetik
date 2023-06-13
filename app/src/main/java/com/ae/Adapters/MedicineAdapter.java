package com.ae.Adapters;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;

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


import com.ae.DAO.MedicineDAO;
import com.ae.Models.Medicine;
import com.ae.diyabetik.R;
import com.ae.diyabetik.SwipeToDeleteCallback;

import java.util.ArrayList;
import java.util.Calendar;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private ArrayList<Medicine> medicineList;
    private int year, month, day, hour, minute;
    private Context context;
    private EditText medicineNameEditText;
    private EditText takenTimeEditText;

    MedicineDAO medicineDAO = new MedicineDAO();
    public MedicineAdapter(ArrayList<Medicine> medicineList, Context context){
        this.context=context;
        this.medicineList=medicineList;
    }
    // swipe to delete işlemi
    private final ItemTouchHelper.Callback swipeToDeleteCallback = new SwipeToDeleteCallback() {
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Medicine medicine = medicineList.get(position);
            String medicineId = medicine.getId();
            medicineDAO.delete(medicineId);
            medicineList.remove(position);
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
        takenTimeEditText.setText(selectedDateTime);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine, parent, false);
        return new MedicineAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.medicineNameTextView.setText(medicine.getMedicineName());
        holder.takenTimeTextView.setText(medicine.getTakenDate());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showUpdateBloodSugarDialog(medicine, position);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView medicineNameTextView;
        public TextView takenTimeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            medicineNameTextView = itemView.findViewById(R.id.textViewMedicineName);
            takenTimeTextView = itemView.findViewById(R.id.textViewTakenTime);
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
    private void showUpdateBloodSugarDialog(Medicine medicine, int adapterPosition) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_update_medicine);
        medicineNameEditText = dialog.findViewById(R.id.editTextMedicineName);
        takenTimeEditText = dialog.findViewById(R.id.editTextTakenTime);
        takenTimeEditText.setEnabled(false);
        ImageButton calendarImageButton = dialog.findViewById(R.id.imageButtonCalendar);
        Button updateButton = dialog.findViewById(R.id.buttonUpdate);
        Button cancelButton = dialog.findViewById(R.id.buttonCancel);

        medicineNameEditText.setText(String.valueOf(medicine.getMedicineName()));
        takenTimeEditText.setText(medicine.getTakenDate());

        calendarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMedicine(medicine, adapterPosition);
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
    private void updateMedicine(Medicine medicineToUpdate, int adapterPosition) {
        String medicineName = medicineNameEditText.getText().toString();
        String takenTime = takenTimeEditText.getText().toString();
        medicineToUpdate.setMedicineName(medicineName);
        medicineToUpdate.setTakenDate(takenTime);
        medicineDAO.update(medicineToUpdate);
        notifyItemChanged(adapterPosition);
    }
}


