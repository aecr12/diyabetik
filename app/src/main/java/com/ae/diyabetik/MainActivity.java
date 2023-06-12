package com.ae.diyabetik;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ae.DAO.InformationCallback;
import com.ae.DAO.UserInformationDAO;
import com.ae.Models.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private CardView cardViewPersonalInformations, cardViewTension, cardViewPedometer, cardViewBloodSugarTracker, cardViewMedications, cardViewMealTracker,
            cardViewTreatmentChoices;
    private ImageView imageViewPersonalInformations, imageViewTension, imageViewPedometer, imageViewBloodSugarNotes, imageViewMedications, imageViewMeals,
            imageViewTreatments;
    private TextView textViewPersonalInformations, textViewTension, textViewPedometer, textViewBloodSugarNotes, textViewMedications, textViewMeals,
            textViewTreatmens, textViewHeight, textViewWeight, textViewWaist, textViewHbA1c;
    private FloatingActionButton fab, fabDiabetesBook, fabHospital, fabPharmacy, fabLogout;
    private Intent intent;
    private boolean isOpen = false;
    private Animation anim1, anim2, anim3, anim4;
    private UserInformationDAO userInformationDAO = new UserInformationDAO();
    private List<UserInformation> userInformationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardViewPersonalInformations = findViewById(R.id.cardViewPersonalInformations);
        cardViewTension = findViewById(R.id.cardViewTension);
        cardViewPedometer = findViewById(R.id.cardViewPedometer);
        cardViewBloodSugarTracker = findViewById(R.id.cardViewBloodSugarTracker);
        cardViewMedications = findViewById(R.id.cardViewMedications);
        cardViewMealTracker = findViewById(R.id.cardViewMealTracker);
        cardViewTreatmentChoices = findViewById(R.id.cardViewTreatmentChoices);

        imageViewPersonalInformations = findViewById(R.id.imageViewPersonalInformations);
        imageViewTension = findViewById(R.id.imageViewTension);
        imageViewPedometer = findViewById(R.id.imageViewPedometer);
        imageViewBloodSugarNotes = findViewById(R.id.imageViewBSugarNotes);
        imageViewMedications = findViewById(R.id.imageViewMedications);
        imageViewMeals = findViewById(R.id.imageViewMeals);
        imageViewTreatments = findViewById(R.id.imageViewTreatments);

        textViewPersonalInformations = findViewById(R.id.textViewPersonalInformations);
        textViewTension = findViewById(R.id.textViewTension);
        textViewPedometer = findViewById(R.id.textViewPedometer);
        textViewBloodSugarNotes = findViewById(R.id.textViewBSugarNotes);
        textViewMedications = findViewById(R.id.textViewMedications);
        textViewMeals = findViewById(R.id.textViewMeals);
        textViewTreatmens = findViewById(R.id.textViewTreatments);
        textViewHeight = findViewById(R.id.textViewHeight);
        textViewWeight = findViewById(R.id.textViewWeight);
        textViewWaist = findViewById(R.id.textViewWaist);
        textViewHbA1c = findViewById(R.id.textViewHba1c);

        fab = findViewById(R.id.fab);
        fabDiabetesBook = findViewById(R.id.fabDiabetesBook);
        fabHospital = findViewById(R.id.fabHospital);
        fabPharmacy = findViewById(R.id.fabPharmacy);
        fabLogout = findViewById(R.id.fabLogout);

        anim1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1); //open
        anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim2); //close
        anim3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim2); //clock
        anim4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim4); //anticlock

        userInformationList = new ArrayList<>();
        cardViewTension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, TensionTracker.class);
                startActivity(intent);
            }
        });
        cardViewPedometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, StepCounterActivity.class);
                startActivity(intent);
            }
        });
        cardViewBloodSugarTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, BloodSugarTracker.class);
                startActivity(intent);
            }
        });
        cardViewMedications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, MedicineTracker.class);
                startActivity(intent);
            }
        });
        cardViewMealTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, MealTracker.class);
                startActivity(intent);
            }
        });
        cardViewTreatmentChoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, TreatmentChoiceTracking.class);
                startActivity(intent);
            }
        });
        cardViewPersonalInformations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,UserInformationTracking.class);
                startActivity(intent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    fabLogout.startAnimation(anim2);
                    fabHospital.startAnimation(anim2);
                    fabDiabetesBook.startAnimation(anim2);
                    fabPharmacy.startAnimation(anim2);
                    fab.startAnimation(anim4);
                    isOpen=false;
                } else {
                    fabHospital.startAnimation(anim1);
                    fabHospital.setClickable(true);
                    fabDiabetesBook.startAnimation(anim1);
                    fabDiabetesBook.setClickable(true);
                    fabPharmacy.startAnimation(anim1);
                    fabPharmacy.setClickable(true);
                    fabLogout.startAnimation(anim1);
                    fabLogout.setClickable(true);
                    fab.startAnimation(anim3);
                    isOpen = true;
                }
            }
        });

        fabDiabetesBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,DiabetesBook.class);
                startActivity(intent);
            }
        });

        fabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                if (FirebaseAuth.getInstance().getCurrentUser()==null){
                    intent = new Intent(MainActivity.this,LoginSignUp.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(MainActivity.this,"Çıkış yapıldı", Toast.LENGTH_SHORT).show();
                }
            }
        });

        userInformationDAO.read(userInformationList, new InformationCallback() {
            @Override
            public void onInformationLoaded(List informationList) {
                textViewHeight.setText("Boy: "+userInformationList.get(0).getHeight() + " cm");
                textViewWeight.setText("Kilo: "+ userInformationList.get(0).getWeight()+ " kg" );
                textViewWaist.setText("Bel çevresi: "+ userInformationList.get(0).getWaist()+ " cm");
                textViewHbA1c.setText("HbA1c Değeri: "+ userInformationList.get(0).getHbA1cPercent()+ " %");
            }

            @Override
            public void onInformationNotLoaded() {
                textViewHeight.setText("Boy: Henüz bir değer girmediniz. ");
                textViewWeight.setText("Kilo: Henüz bir değer girmediniz" );
                textViewWaist.setText("Bel çevresi: Henüz bir değer girmediniz.");
                textViewHbA1c.setText("HbA1c Değeri: Hneüz bir değer girmediniz");
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

}