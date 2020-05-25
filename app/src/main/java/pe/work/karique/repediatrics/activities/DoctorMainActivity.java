package pe.work.karique.repediatrics.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.fragments.DoctorHomeFragment;
import pe.work.karique.repediatrics.fragments.AccountFragment;
import pe.work.karique.repediatrics.fragments.NotificationsFragment;
import pe.work.karique.repediatrics.models.Notification;
import pe.work.karique.repediatrics.models.Ticket;
import pe.work.karique.repediatrics.session.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DoctorMainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return navigateAccordingTo(item.getItemId());
        }
    };

    BottomNavigationView navigation;
    ProgressBar progressBar;
    FloatingActionButton speedDialFloatingActionButton;

    DoctorHomeFragment doctorHomeFragment;
    NotificationsFragment notificationsFragment;
    AccountFragment accountFragment;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);
        hide();

        sessionManager = SessionManager.getInstance(this);
        progressBar = findViewById(R.id.progressBar);

        speedDialFloatingActionButton = findViewById(R.id.speedDialFloatingActionButton);
        speedDialFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAddQuestionary();
            }
        });

        navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigateAccordingTo(R.id.navigation_home_doctor);
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private boolean navigateAccordingTo(int id){
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
    private Fragment getFragmentFor(int id) {
        if (id == R.id.navigation_home_doctor) {
            speedDialFloatingActionButton.show();
            return getDoctorHomeFragment();
        }
        else if (id == R.id.navigation_my_account_doctor) {
            speedDialFloatingActionButton.hide();
            return getAccountFragment();
        }
        else if (id == R.id.navigation_notifications_doctor) {
            speedDialFloatingActionButton.hide();
            return getNotificationsFragment();
        }
        return null;
    }
    private void startActivityAddQuestionary(){
        Intent intent = new Intent(this, AddQuestionaryActivity.class);
        startActivityForResult(intent, AddQuestionaryActivity.REQUEST_CODE);
    }
    private void showTicketDetails(Ticket ticket){
        Intent intent = new Intent(this, TicketDetailsActivity.class);
        intent.putExtras(ticket.toBundle());
        startActivityForResult(intent, TicketDetailsActivity.REQUEST_CODE);
    }
    private void showNotificationAction(Notification notification){
        if (notification.getText().contains("ticket")){
            navigation.setSelectedItemId(R.id.navigation_home_doctor);
            navigateAccordingTo(R.id.navigation_home_doctor);
        }
        //else if (notification.getText().contains("cita medica")){
        //    showActivityMedicalAppointments();
        //}
        else {
            Toast.makeText(this, "No se encontró vista", Toast.LENGTH_LONG).show();
        }
    }

    private DoctorHomeFragment getDoctorHomeFragment(){
        doctorHomeFragment = new DoctorHomeFragment();
        doctorHomeFragment.setOnProgressBarChanged(new DoctorHomeFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnProgressBarHide() {
                progressBar.setVisibility(View.GONE);
            }
        });
        doctorHomeFragment.setOnTicketClickedListener(new DoctorHomeFragment.OnTicketClickedListener() {
            @Override
            public void OnTicketClicked(Ticket ticket) {
                showTicketDetails(ticket);
            }
        });
        return doctorHomeFragment;
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
    private AccountFragment getAccountFragment(){
        accountFragment = new AccountFragment();
        return accountFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Ticket agregado correctamente
        if (requestCode == AddQuestionaryActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (doctorHomeFragment != null){
                    doctorHomeFragment.getTickets();
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Operación cancelada", Toast.LENGTH_LONG).show();
            }
        }

        //Ticket detalle acción realizada correctamente
        if (requestCode == TicketDetailsActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (doctorHomeFragment != null){
                    doctorHomeFragment.getTickets();
                }
            }
        }
    }
}
