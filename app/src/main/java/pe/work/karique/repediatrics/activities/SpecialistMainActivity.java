package pe.work.karique.repediatrics.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.fragments.AccountFragment;
import pe.work.karique.repediatrics.fragments.NotificationsFragment;
import pe.work.karique.repediatrics.fragments.SpecialistHomeFragment;
import pe.work.karique.repediatrics.fragments.SpecialistTicketsFragment;
import pe.work.karique.repediatrics.fragments.SpecialistTimeTableFragment;
import pe.work.karique.repediatrics.models.DoctorComment;
import pe.work.karique.repediatrics.models.Notification;
import pe.work.karique.repediatrics.models.Ticket;
import pe.work.karique.repediatrics.models.TimeTableByDoctor;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class SpecialistMainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private static int TIME_MODE_FECHA_INICIO = 1;
    private static int TIME_MODE_FECHA_FIN = 2;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return navigateAccordingTo(item.getItemId());
        }
    };

    private BottomNavigationView navigation;
    private ProgressBar progressBar;

    private SessionManager sessionManager;

    private SpecialistHomeFragment specialistHomeFragment;
    private SpecialistTicketsFragment specialistTicketsFragment;
    private SpecialistTimeTableFragment specialistTimeTableFragment;
    private NotificationsFragment notificationsFragment;

    private FloatingActionButton speedDialFloatingActionButton;
    private int TIME_MODE = TIME_MODE_FECHA_INICIO;
    private String startTime = "";
    private String endTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialist_main);
        hide();

        sessionManager = SessionManager.getInstance(this);

        speedDialFloatingActionButton = findViewById(R.id.speedDialFloatingActionButton);
        speedDialFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });
        speedDialFloatingActionButton.hide();

        progressBar = findViewById(R.id.progressBar);
        navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigateAccordingTo(R.id.navigation_home_medical_appointment);
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
        if (id == R.id.navigation_home_medical_appointment) {
            speedDialFloatingActionButton.hide();
            return getSpecialistHomeFragment();
        }
        else if (id == R.id.navigation_specialist_tickets) {
            speedDialFloatingActionButton.hide();
            return getSpecialistTicketsFragment();
        }
        else if (id == R.id.navigation_specialist_timetables) {
            speedDialFloatingActionButton.show();
            return getSpecialistTimeTableFragment();
        }
        else if (id == R.id.navigation_my_account_specialist) {
            speedDialFloatingActionButton.hide();
            return getAccountFragment();
        }
        else if (id == R.id.navigation_specialist_notifications) {
            speedDialFloatingActionButton.hide();
            return getNotificationsFragment();
        }
        return null;
    }
    private void showTicketDetails(Ticket ticket){
        Intent intent = new Intent(this, TicketDetailsActivity.class);
        intent.putExtras(ticket.toBundle());
        startActivityForResult(intent, TicketDetailsActivity.REQUEST_CODE);
    }
    private void showTimePickerDialog(){
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                SpecialistMainActivity.this,
                mHour,
                mMinute,
                true
        );
        String titleSelection = TIME_MODE == TIME_MODE_FECHA_INICIO ? "Seleccione hora de inicio" : "Seleccione hora de fin";
        timePickerDialog.setTitle(titleSelection);
        timePickerDialog.setAccentColor(getResources().getColor(R.color.green));
        timePickerDialog.show(getSupportFragmentManager(), "Datepickerdialog");
    }
    private void showAlertDeleteTimeTable(TimeTableByDoctor timeTableByDoctor){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Seguro que desea eliminar este horario?")
                .setCancelable(true)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteTimeTable(timeTableByDoctor);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void deleteTimeTable(TimeTableByDoctor timeTableByDoctor){
        progressBar.setVisibility(View.VISIBLE);

        AndroidNetworking.delete(RepediatricsApi.TIMETABLEBYDOCTOR_URL + "/" + timeTableByDoctor.getId())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(
                                SpecialistMainActivity.this,
                                "Horario eliminado correctamente",
                                Toast.LENGTH_SHORT
                        ).show();
                        specialistTimeTableFragment.getTimeTables();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(
                                SpecialistMainActivity.this,
                                "Eliminar horario" + " - " + "Error de conexión",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
    private void showNotificationAction(Notification notification){
        if (notification.getText().contains("ticket")){
            navigation.setSelectedItemId(R.id.navigation_specialist_tickets);
            navigateAccordingTo(R.id.navigation_specialist_tickets);
        }
        else if (notification.getText().contains("cita medica")){
            navigation.setSelectedItemId(R.id.navigation_home_medical_appointment);
            navigateAccordingTo(R.id.navigation_home_medical_appointment);
        }
        else {
            Toast.makeText(this, "No se encontró vista", Toast.LENGTH_LONG).show();
        }
    }

    private SpecialistHomeFragment getSpecialistHomeFragment(){
        specialistHomeFragment = new SpecialistHomeFragment();
        specialistHomeFragment.setOnProgressBarChanged(new SpecialistHomeFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnProgressBarHide() {
                progressBar.setVisibility(View.GONE);
            }
        });
        return specialistHomeFragment;
    }
    private SpecialistTicketsFragment getSpecialistTicketsFragment(){
        specialistTicketsFragment = new SpecialistTicketsFragment();
        specialistTicketsFragment.setOnProgressBarChanged(new SpecialistTicketsFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnProgressBarHide() {
                progressBar.setVisibility(View.GONE);
            }
        });
        specialistTicketsFragment.setOnTicketClickedListener(new SpecialistTicketsFragment.OnTicketClickedListener() {
            @Override
            public void OnTicketClicked(Ticket ticket) {
                showTicketDetails(ticket);
            }
        });

        return specialistTicketsFragment;
    }
    private AccountFragment getAccountFragment(){
        return new AccountFragment();
    }
    private SpecialistTimeTableFragment getSpecialistTimeTableFragment(){
        specialistTimeTableFragment = new SpecialistTimeTableFragment();
        specialistTimeTableFragment.setOnProgressBarChanged(new SpecialistTimeTableFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnProgressBarHide() {
                progressBar.setVisibility(View.GONE);
            }
        });
        specialistTimeTableFragment.setOnTimeTableClickedListener(new SpecialistTimeTableFragment.OnTimeTableClickedListener() {
            @Override
            public void OnTimeTableClicked(TimeTableByDoctor ticketTableByDoctor) {

            }
        });
        specialistTimeTableFragment.setOnTimeTableByDoctorLongClicked(new SpecialistTimeTableFragment.OnTimeTableByDoctorLongClickedListener() {
            @Override
            public void OnTimeTableByDoctorLongClicked(TimeTableByDoctor timeTableByDoctor) {
                showAlertDeleteTimeTable(timeTableByDoctor);
            }
        });

        return specialistTimeTableFragment;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Ticket detalle acción realizada correctamente
        if (requestCode == TicketDetailsActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (specialistTicketsFragment != null){
                    specialistTicketsFragment.getTickets();
                }
            }
        }
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        if (TIME_MODE == TIME_MODE_FECHA_INICIO){
            TIME_MODE = TIME_MODE_FECHA_FIN;
            startTime = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second);
            showTimePickerDialog();
        }
        else if (TIME_MODE == TIME_MODE_FECHA_FIN){
            TIME_MODE = TIME_MODE_FECHA_INICIO;
            endTime = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second);
            addTimeTable(
                    sessionManager.getid(),
                    specialistTimeTableFragment.getSelectedHospital().getId(),
                    specialistTimeTableFragment.getCurrentDateApiFormat(),
                    startTime,
                    endTime
            );
        }
    }

    private void addTimeTable(String specialistId, String hospitalId, String dateInApiFormat, String startTime, String endTime){
        String startDateTime = dateInApiFormat + "T" + startTime;
        String endDateTime = dateInApiFormat + "T" + endTime;

        JSONObject timeTableJsonObject = new JSONObject();
        try {
            timeTableJsonObject.put("startDateTime", startDateTime);
            timeTableJsonObject.put("endDateTime", endDateTime);
            timeTableJsonObject.put("specialistId", specialistId);
            timeTableJsonObject.put("hospitalId", hospitalId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(SpecialistMainActivity.this, "Agregando horario...", Toast.LENGTH_SHORT).show();
        AndroidNetworking.post(RepediatricsApi.TIMETABLEBYDOCTOR_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(timeTableJsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(SpecialistMainActivity.this, "Horario registrado", Toast.LENGTH_LONG).show();
                        if (specialistTimeTableFragment != null){
                            specialistTimeTableFragment.getTimeTables();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(SpecialistMainActivity.this, "Horario error", Toast.LENGTH_LONG).show();
                    }
                });
    }
}































