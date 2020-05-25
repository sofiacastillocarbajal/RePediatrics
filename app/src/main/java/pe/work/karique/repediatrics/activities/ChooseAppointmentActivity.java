package pe.work.karique.repediatrics.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.adapters.AppointmentsAdapter;
import pe.work.karique.repediatrics.adapters.SpecialistsAdapter;
import pe.work.karique.repediatrics.models.Ticket;
import pe.work.karique.repediatrics.models.TimeTableByDoctor;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;
import pe.work.karique.repediatrics.util.FuncionesFecha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChooseAppointmentActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 14;

    private SessionManager sessionManager;
    private ImageButton backImageButton;
    private ProgressBar progressBar;

    private Ticket ticket;
    private User specialist;

    private AppCompatImageView doctorPictureAppCompatImageView;
    private TextView fullNameTextView;
    private TextView specialityTextView;
    private CardView backDayDateCardView;
    private CardView nextDayDateCardView;
    private TextView dayNameTextView;
    private ConstraintLayout giveCommentConstraintLayout;
    private CardView reserveCardView;
    private TextView noAppointmentsMessageTextView;
    private CardView nextAvailableDayCardView;
    private TextView oTextView;
    private CardView nextDayDateBigCardView;
    private ConstraintLayout noAppointmentsConstraintLayout;
    private ConstraintLayout appointmentsConstraintLayout;
    private TextView nextDateAvailableTextView;
    private ProgressBar nextDateAvailableProgressBar;

    private RecyclerView appointmentsRecyclerView;
    private AppointmentsAdapter appointmentsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<TimeTableByDoctor> timeTableByDoctors;

    LocalDate currentDate;
    LocalDate firstDate;
    LocalDate nextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_appointment);
        hide();

        ticket = Ticket.from(getIntent().getBundleExtra("ticket"));
        specialist = User.from(getIntent().getBundleExtra("specialist"));

        sessionManager = SessionManager.getInstance(this);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressBar = findViewById(R.id.progressBar);

        doctorPictureAppCompatImageView = findViewById(R.id.doctorPictureAppCompatImageView);
        fullNameTextView = findViewById(R.id.fullNameTextView);
        specialityTextView = findViewById(R.id.specialityTextView);
        backDayDateCardView = findViewById(R.id.backDayDateCardView);
        backDayDateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackDayDate();
            }
        });
        nextDayDateCardView = findViewById(R.id.nextDayDateCardView);
        nextDayDateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNextDayDate();
            }
        });
        dayNameTextView = findViewById(R.id.dayNameTextView);
        giveCommentConstraintLayout = findViewById(R.id.giveCommentConstraintLayout);
        giveCommentConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        reserveCardView = findViewById(R.id.reserveCardView);
        reserveCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChooseAppointmentActivity.this, "Haga click en un horario para reservarlo", Toast.LENGTH_LONG).show();
            }
        });
        noAppointmentsMessageTextView = findViewById(R.id.noAppointmentsMessageTextView);
        nextAvailableDayCardView = findViewById(R.id.nextAvailableDayCardView);
        nextAvailableDayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDate = nextDate;
                dayNameTextView.setText(FuncionesFecha.formatLDateToText(currentDate));
                getSpecialists(FuncionesFecha.formatDateLDApi(currentDate));
            }
        });
        oTextView = findViewById(R.id.oTextView);
        nextDayDateBigCardView = findViewById(R.id.nextDayDateBigCardView);
        nextDayDateBigCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNextDayDate();
            }
        });
        noAppointmentsConstraintLayout = findViewById(R.id.noAppointmentsConstraintLayout);
        appointmentsConstraintLayout = findViewById(R.id.appointmentsConstraintLayout);
        nextDateAvailableTextView = findViewById(R.id.nextDateAvailableTextView);
        nextDateAvailableProgressBar = findViewById(R.id.nextDateAvailableProgressBar);

        appointmentsRecyclerView = findViewById(R.id.appointmentsRecyclerView);
        timeTableByDoctors = new ArrayList<>();
        appointmentsAdapter = new AppointmentsAdapter(timeTableByDoctors);
        appointmentsAdapter.setOnAppointmentClicked(new AppointmentsAdapter.OnAppointmentClickedListener() {
            @Override
            public void OnAppointmentClicked(TimeTableByDoctor timeTableByDoctor) {
                startConfirmAppointmentActivity(timeTableByDoctor);
            }
        });
        layoutManager = new GridLayoutManager(this, 2);
        appointmentsRecyclerView.setAdapter(appointmentsAdapter);
        appointmentsRecyclerView.setLayoutManager(layoutManager);

        firstDate = currentDate = CalendarDay.today().getDate();

        backDayDateCardView.setVisibility(View.INVISIBLE);
        setData();
        getSpecialists(FuncionesFecha.formatDateLDApi(currentDate));
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void setData(){
        fullNameTextView.setText(specialist.getDrName());
        specialityTextView.setText(specialist.getSpecialty());
        doctorPictureAppCompatImageView.setImageResource(specialist.getDoctorPictureId());
        dayNameTextView.setText(FuncionesFecha.formatLDateToText(currentDate));
    }
    private void getSpecialists(String date){
        noAppointmentsConstraintLayout.setVisibility(View.GONE);
        appointmentsConstraintLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        String url = RepediatricsApi.TIMETABLE_BY_DOCTOR_BY_HOSPITAL_BY_DATE(specialist.getId(), ticket.getHospitalId(), date);

        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        appointmentsConstraintLayout.setVisibility(View.VISIBLE);
                        noAppointmentsConstraintLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                        timeTableByDoctors.clear();
                        timeTableByDoctors.addAll(TimeTableByDoctor.from(response));
                        appointmentsAdapter.notifyDataSetChanged();

                        if (timeTableByDoctors.isEmpty()) {
                            showNoDataMessage();
                            searchNextAvailableDate();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ChooseAppointmentActivity.this, "Error obteniendo los horarios", Toast.LENGTH_LONG).show();
                        showNoDataMessage();
                    }
                });
    }
    private void showNoDataMessage(){
        appointmentsConstraintLayout.setVisibility(View.GONE);
        noAppointmentsConstraintLayout.setVisibility(View.VISIBLE);
    }
    private void setBackDayDate(){
        currentDate = currentDate.minusDays(1);
        dayNameTextView.setText(FuncionesFecha.formatLDateToText(currentDate));
        getSpecialists(FuncionesFecha.formatDateLDApi(currentDate));

        //para que no aparezca la primera flecha a atras
        if (currentDate.isEqual(firstDate)) backDayDateCardView.setVisibility(View.INVISIBLE);
        else backDayDateCardView.setVisibility(View.VISIBLE);
    }
    private void setNextDayDate(){
        currentDate = currentDate.plusDays(1);
        dayNameTextView.setText(FuncionesFecha.formatLDateToText(currentDate));
        getSpecialists(FuncionesFecha.formatDateLDApi(currentDate));
        backDayDateCardView.setVisibility(View.VISIBLE);
    }
    private void disableNextDateCardView(){
        nextAvailableDayCardView.setEnabled(false);
        nextDateAvailableTextView.setVisibility(View.INVISIBLE);
        nextDateAvailableProgressBar.setVisibility(View.VISIBLE);
    }
    private void enableNextDateCardView(){
        nextAvailableDayCardView.setEnabled(true);
        nextDateAvailableTextView.setVisibility(View.VISIBLE);
        nextDateAvailableProgressBar.setVisibility(View.GONE);
    }
    private void searchNextAvailableDate(){
        disableNextDateCardView();

        String dateApi = FuncionesFecha.formatDateLDApi(currentDate);
        String url = RepediatricsApi.TIMETABLE_BY_DOCTOR_BY_HOSPITAL_BY_NEXTAVAILABLEDATE(specialist.getId(), ticket.getHospitalId(), dateApi);

        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        enableNextDateCardView();

                        try {
                            String nextDateAvailable = response.getString("nextAvailableDate");
                            //si la siguiente fecha es vacia, entonces no hay
                            if (nextDateAvailable.equals("")){
                                nextDateAvailableTextView.setText("No hay una siguiente fecha");
                            }
                            //si encuentra una siguiente fecha
                            else {
                                nextDate = FuncionesFecha.getLocalDateFromString(nextDateAvailable);
                                nextDateAvailableTextView.setText("Siguiente día disponible, " + FuncionesFecha.formatLDateToText(nextDate));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        enableNextDateCardView();
                        nextDateAvailableTextView.setText("No hay una siguiente fecha");
                        Toast.makeText(ChooseAppointmentActivity.this, "Error al obtener la siguiente fecha", Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void startConfirmAppointmentActivity(TimeTableByDoctor timeTableByDoctor){
        Intent intent = new Intent(this, ConfirmAppointmentActivity.class);
        intent.putExtra("ticket", ticket.toBundle());
        intent.putExtra("specialist", specialist.toBundle());
        intent.putExtra("timeTableByDoctor", timeTableByDoctor.toBundle());
        startActivityForResult(intent, ConfirmAppointmentActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Cita medica agregada correctamente
        if (requestCode == ConfirmAppointmentActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_FIRST_USER) {
                setResult(Activity.RESULT_FIRST_USER);
                finish();
            }
        }
    }
}
