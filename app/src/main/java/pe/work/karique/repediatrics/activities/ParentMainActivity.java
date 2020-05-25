package pe.work.karique.repediatrics.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.fragments.AccountFragment;
import pe.work.karique.repediatrics.fragments.NotificationsFragment;
import pe.work.karique.repediatrics.fragments.ParentHomeFragment;
import pe.work.karique.repediatrics.fragments.ParentPatientsFragment;
import pe.work.karique.repediatrics.models.Notification;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.session.SessionManager;

public class ParentMainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return navigateAccordingTo(item.getItemId());
        }
    };

    private BottomNavigationView navigation;
    private ProgressBar progressBar;

    private CardView medicalDateCardView;
    private CardView advicesCardView;
    private CardView requestCardView;
    private FloatingActionButton speedDialFloatingActionButton;

    private ParentHomeFragment parentHomeFragment;
    private AccountFragment parentAccountFragment;
    private ParentPatientsFragment parentPatientsFragment;
    private NotificationsFragment notificationsFragment;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main);
        hide();

        sessionManager = SessionManager.getInstance(this);
        progressBar = findViewById(R.id.progressBar);
        speedDialFloatingActionButton = findViewById(R.id.speedDialFloatingActionButton);
        speedDialFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivityAddPatient();
            }
        });
        speedDialFloatingActionButton.hide();

        navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigateAccordingTo(R.id.navigation_home_parent);

        medicalDateCardView = findViewById(R.id.medicalDateCardView);
        advicesCardView = findViewById(R.id.advicesCardView);
        requestCardView = findViewById(R.id.requestCardView);

        medicalDateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivityMedicalAppointments();
            }
        });
        advicesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivityAdvices();
            }
        });
        requestCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivityTickets();
            }
        });
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    boolean navigateAccordingTo(int id){
        try
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content,getFragmentFor(id))
                    .commit();
            return true;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }
    Fragment getFragmentFor(int id) {
        if (id == R.id.navigation_home_parent) {
            speedDialFloatingActionButton.hide();
            return getParentHomeFragment();
        }
        else if (id == R.id.navigation_patients_parent) {
            speedDialFloatingActionButton.show();
            return getParentPatientsFragment();
        }
        else if (id == R.id.navigation_my_account_parent) {
            speedDialFloatingActionButton.hide();
            return getParentAccountFragment();
        }
        else if (id == R.id.navigation_notifications_parent) {
            speedDialFloatingActionButton.hide();
            return getNotificationsFragment();
        }
        return null;
    }

    private ParentHomeFragment getParentHomeFragment(){
        parentHomeFragment = new ParentHomeFragment();
        parentHomeFragment.setOnProgressBarChanged(new ParentHomeFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnProgressBarHide() {
                progressBar.setVisibility(View.GONE);
            }
        });
        parentHomeFragment.setOnUserClickedListener(new ParentHomeFragment.OnUserClickedListener() {
            @Override
            public void OnUserClicked(User user) {
                showDoctorDetails(user);
            }
        });
        return parentHomeFragment;
    }
    private AccountFragment getParentAccountFragment(){
        parentAccountFragment = new AccountFragment();
        return parentAccountFragment;
    }
    private ParentPatientsFragment getParentPatientsFragment(){
        parentPatientsFragment = new ParentPatientsFragment();
        parentPatientsFragment.setUserId(sessionManager.getid());
        parentPatientsFragment.setOnProgressBarChanged(new ParentPatientsFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnProgressBarHide() {
                progressBar.setVisibility(View.GONE);
            }
        });
        parentPatientsFragment.setOnUserClicked(new ParentPatientsFragment.OnUserClickedListener() {
            @Override
            public void OnUserClicked(User user) {
                showPatientDetails(user);
            }
        });

        return parentPatientsFragment;
    }
    private NotificationsFragment getNotificationsFragment(){
        notificationsFragment = new NotificationsFragment();
        notificationsFragment.setOnProgressBarChanged(new NotificationsFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnProgressBarHide() {
                progressBar.setVisibility(View.GONE);
            }
        });
        notificationsFragment.setOnNotificationClicked(new NotificationsFragment.OnNotificationClickedListener() {
            @Override
            public void OnNotificationClicked(Notification notification) {
                showNotificationAction(notification);
            }
        });
        notificationsFragment.setOnNotificationLongClicked(new NotificationsFragment.OnNotificationLongClickedListener() {
            @Override
            public void OnNotificationLongClicked(Notification notification) {

            }
        });

        return notificationsFragment;
    }

    private void showActivityAdvices(){
        Intent intent = new Intent(this, AdvicesActivity.class);
        startActivity(intent);
    }
    private void showActivityMedicalAppointments(){
        Intent intent = new Intent(this, MedicalAppointmentsActivity.class);
        startActivity(intent);
    }
    private void showActivityTickets(){
        Intent intent = new Intent(this, TicketsActivity.class);
        startActivity(intent);
    }
    private void showActivityAddPatient(){
        Intent intent = new Intent(this, AddPatientActivity.class);
        startActivityForResult(intent, AddPatientActivity.REQUEST_CODE);
    }
    private void showPatientDetails(User user){
        Intent intent = new Intent(this, PatientDetailsActivity.class);
        intent.putExtras(user.toBundle());
        startActivityForResult(intent, PatientDetailsActivity.REQUEST_CODE);
    }
    private void showDoctorDetails(User doctor){
        Intent intent = new Intent(this, DoctorDetailsActivity.class);
        intent.putExtras(doctor.toBundle());
        startActivityForResult(intent, DoctorDetailsActivity.REQUEST_CODE);
    }
    private void showNotificationAction(Notification notification){
        if (notification.getText().contains("ticket")){
            showActivityTickets();
        }
        else if (notification.getText().contains("cita medica")){
            showActivityMedicalAppointments();
        }
        else {
            Toast.makeText(this, "No se encontró vista", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Patient agregado correctamente
        if (requestCode == AddPatientActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (parentPatientsFragment != null)
                    parentPatientsFragment.getPatientsByParent();
            }
        }
    }
}
