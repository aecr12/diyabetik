package com.ae.Adapters;

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

import com.ae.DAO.BloodSugarDAO;
import com.ae.Models.BloodSugar;
import com.ae.diyabetik.BloodSugarWithHBA1CDiagram;
import com.ae.diyabetik.R;
import com.ae.diyabetik.SwipeToDeleteCallback;

import java.util.ArrayList;
import java.util.Calendar;


public class BloodSugarAdapter extends RecyclerView.Adapter<BloodSugarAdapter.ViewHolder> implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    // Bileşenlerin initialize edilmesi

    private ArrayList<BloodSugar> bloodSugarList;
    private int year, month, day, hour, minute;
    private Context context;
    private EditText bloodSugarValueEditText;
    private EditText dateEditText;

    private BloodSugarDAO bloodSugarDAO = new BloodSugarDAO();
    public BloodSugarAdapter(ArrayList<BloodSugar> bloodSugarList, Context context) {
        this.context = context;
        this.bloodSugarList = bloodSugarList;
    }

    //Swipe to delete işlemi için oluşturulan callback interfacesi çağrılıyor
    private final ItemTouchHelper.Callback swipeToDeleteCallback = new SwipeToDeleteCallback() {
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            // position tıklanan elemanının konumunu alıyor, daha sonra listedeki yeri bulunup db ve
            // view üzerinde silme işlemi yapılıyor
            int position = viewHolder.getAdapterPosition();
            BloodSugar bloodSugar = bloodSugarList.get(position);
            String bloodSugarId = bloodSugar.getId();
            bloodSugarDAO.delete(bloodSugarId);
            bloodSugarList.remove(position);
            notifyItemRemoved(position);
        }
    };

    // recycler viewdaki elemanlara dokunma, kaydırma gibi işlemeler için gerekli interface
    private final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    // recy. view oluşturulduğunda liste elemanlarını temsil edecek elemanın View holdera eklenmesi (item_blood_sugar)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blood_sugar, parent, false);
        return new ViewHolder(view);
    }

    // rec. view için gerekli view holderin oluşturulması ve oluşturulduğunda görüntülenecek elemanların içeriklerinin yüklenmesi
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BloodSugar bloodSugar = bloodSugarList.get(position);
        holder.bloodSugarValueTextView.setText(String.valueOf(bloodSugar.getBloodSugarValue()));
        holder.dateTextView.setText(bloodSugar.getDate());

        // Uzun tıklama ile edit ekranının açılması
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showUpdateBloodSugarDialog(bloodSugar, position);
                return true;
            }
        });

        // Dokununca rapor sayfasına yönlendirme
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BloodSugar bloodSugar1 = bloodSugarList.get(position);

                //Kan şekeri değeri, hba1c hesaplanması için gerekli activitye intent aracılığıyla gönderiliyor
                String selectedValue = String.valueOf(bloodSugar1.getBloodSugarValue());
                Intent intent = new Intent(context, BloodSugarWithHBA1CDiagram.class);
                intent.putExtra("selectedValue",Float.parseFloat(selectedValue));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bloodSugarList.size();
    }

    // Kullanıcının tarih seçmesi için kullanılan interfacenin metotları
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

    // View holder (View holder recycler viewda, tanımlanan eleman görünümünün attributelerini tutuyor)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bloodSugarValueTextView;
        public TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            bloodSugarValueTextView = itemView.findViewById(R.id.textViewBloodSugarValue);
            dateTextView = itemView.findViewById(R.id.textViewDate);
        }
    }
    // Takvim iconuna tıklanınca kullanıcının tarih seçmesi için gerekli işlemler bu fonksiyonda yapıldı
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

    // Edit durumunda kullanıcıya gösterilecek dialog ekranı
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
                // kullanıcı bilgileri yeniden girip güncelle butonuna basarsa aşağıdaki fonksiyon çalışacak
                updateBloodSugar(bloodSugar, adapterPosition);
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kullanıcı güncelleme işleminden vazgeçerse dialog ekranı kapanacak
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout((int) (0.8 * Resources.getSystem().getDisplayMetrics().widthPixels),
                (int) (0.55 * Resources.getSystem().getDisplayMetrics().heightPixels));
    }

    // Güncelleme işlemlerini liste ve db den yapacak metot
    private void updateBloodSugar(BloodSugar bloodSugarToUpdate, int adapterPosition) {

        String bloodSugarValue = bloodSugarValueEditText.getText().toString();
        String date = dateEditText.getText().toString();

        bloodSugarToUpdate.setBloodSugarValue(Integer.parseInt(bloodSugarValue));
        bloodSugarToUpdate.setDate(date);

        bloodSugarDAO.update(bloodSugarToUpdate);
        notifyItemChanged(adapterPosition);
    }
}
