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
import com.ae.DAO.TensionDAO;
import com.ae.Models.Tension;
import com.ae.diyabetik.R;
import com.ae.diyabetik.SwipeToDeleteCallback;

import java.util.ArrayList;
import java.util.Calendar;

public class TensionAdapter extends RecyclerView.Adapter<TensionAdapter.ViewHolder> implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private ArrayList<Tension> tensionList;
    private int year, month, day, hour, minute;
    private Context context;
    private EditText systolicEditText, diastolicEditText, dateEditText;
    TensionDAO tensionDAO = new TensionDAO();

    public TensionAdapter(ArrayList<Tension> tensionList, Context context) {
        this.context = context;
        this.tensionList = tensionList;
    }

    private final ItemTouchHelper.Callback swipeToDeleteCallback = new SwipeToDeleteCallback() {
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            Tension tension = tensionList.get(position);
            String tensionId = tension.getId();
            tensionDAO.delete(tensionId);
            tensionList.remove(position);
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
    public TensionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tension, parent, false);
        return new TensionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TensionAdapter.ViewHolder holder, int position) {
        Tension tension = tensionList.get(position);
        holder.systolicTextView.setText(String.valueOf(tension.getSystolic()));
        holder.diastolicTextView.setText(String.valueOf(tension.getDiastolic()));
        holder.dateTextView.setText(tension.getDate());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showUpdateTensionDialog(tension, position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return tensionList.size();
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
        public TextView systolicTextView;
        public TextView diastolicTextView;
        public TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            systolicTextView = itemView.findViewById(R.id.textViewSystolic);
            diastolicTextView = itemView.findViewById(R.id.textViewDiastolic);
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
    private void showUpdateTensionDialog(Tension tension, int adapterPosition) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_update_tension);
        systolicEditText = dialog.findViewById(R.id.editTextSystolic);
        diastolicEditText = dialog.findViewById(R.id.editTextDiastolic);
        dateEditText = dialog.findViewById(R.id.editTextDate);
        dateEditText.setEnabled(false);
        ImageButton calendarImageButton = dialog.findViewById(R.id.imageButtonCalendar);
        Button updateButton = dialog.findViewById(R.id.buttonUpdate);
        Button cancelButton = dialog.findViewById(R.id.buttonCancel);

        systolicEditText.setText(String.valueOf(tension.getSystolic()));
        diastolicEditText.setText(String.valueOf(tension.getDiastolic()));
        dateEditText.setText(tension.getDate());

        calendarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTension(tension, adapterPosition);
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

    private void updateTension(Tension tensionToUpdate, int adapterPosition) {

        String systolic = systolicEditText.getText().toString();
        String diastolic = diastolicEditText.getText().toString();
        String date = dateEditText.getText().toString();

        tensionToUpdate.setSystolic(Integer.parseInt(systolic));
        tensionToUpdate.setDiastolic(Integer.parseInt(diastolic));
        tensionToUpdate.setDate(date);

        tensionDAO.update(tensionToUpdate);
        notifyItemChanged(adapterPosition);
    }

}
