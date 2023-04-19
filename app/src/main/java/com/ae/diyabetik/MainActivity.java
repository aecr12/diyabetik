package com.ae.diyabetik;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ae.DAO.IDAO;
import com.ae.Models.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Diyabetik");
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView userPhoto = headerView.findViewById(R.id.user_photo);
        TextView userName = headerView.findViewById(R.id.user_name);

        // Kullanıcı fotoğrafını ve adını burada değiştirecek
        userPhoto.setImageResource(R.drawable.app_logo);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.nav_home:
                        intent = new Intent(MainActivity.this, Signup.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_treatment_choices:
                        intent = new Intent(MainActivity.this, TreatmentChoiceTracking.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_meal_tracker:
                        intent = new Intent(MainActivity.this,MealTracker.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_medicine_tracker:
                        intent = new Intent(MainActivity.this,MedicineTracker.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_pharmacy:
                        // En yakın eczaneyi bulmak için yapılacak işlemler
                        break;
                    case R.id.nav_hospital:
                        intent = new Intent(MainActivity.this,LoginSignUp.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        ImageView image1 = findViewById(R.id.image_1);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, UserInformationTracking.class);
                startActivity(intent1);
            }
        });

        ImageView image3 = findViewById(R.id.image_3);
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this,BloodSugarTracker.class);
                startActivity(intent1);
            }
        });

        ImageView image2 = findViewById(R.id.image_2);
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this,StepCounter.class);
                startActivity(intent1);
            }
        });

        ImageView image4 = findViewById(R.id.image_4);
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this,DiabetesManual.class);
                startActivity(intent1);
            }
        });
    }
}