package pe.work.karique.repediatrics.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.Questionary;
import pe.work.karique.repediatrics.models.Ticket;
import pe.work.karique.repediatrics.models.TimeTableByDoctor;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;
import pe.work.karique.repediatrics.util.FuncionesFecha;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmAppointmentActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 15;

    private SessionManager sessionManager;
    private ImageButton backImageButton;
    private ProgressBar progressBar;

    private Ticket ticket;
    private User specialist;
    private TimeTableByDoctor timeTableByDoctor;

    private AppCompatImageView doctorPictureAppCompatImageView;
    private TextView fullNameTextView;
    private TextView specialityTextView;
    private TextView dateValueMessageTextView;
    private TextView timeValueMessageTextView;
    private TextView patientValueMessageTextView;
    private TextView dniValueMessageTextView;
    private TextView hospitalValueMessageTextView;
    private TextView emailValueMessageTextView;
    private ConstraintLayout cancelConstraintLayout;
    private CardView reserveCardView;
    private ProgressBar reserveProgressBar;
    private TextView reserveTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_appointment);
        hide();

        ticket = Ticket.from(getIntent().getBundleExtra("ticket"));
        specialist = User.from(getIntent().getBundleExtra("specialist"));
        timeTableByDoctor = TimeTableByDoctor.from(getIntent().getBundleExtra("timeTableByDoctor"));

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
        dateValueMessageTextView = findViewById(R.id.dateValueMessageTextView);
        timeValueMessageTextView = findViewById(R.id.timeValueMessageTextView);
        patientValueMessageTextView = findViewById(R.id.patientValueMessageTextView);
        dniValueMessageTextView = findViewById(R.id.dniValueMessageTextView);
        hospitalValueMessageTextView = findViewById(R.id.hospitalValueMessageTextView);
        emailValueMessageTextView = findViewById(R.id.emailValueMessageTextView);
        cancelConstraintLayout = findViewById(R.id.cancelConstraintLayout);
        cancelConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reserveCardView = findViewById(R.id.reserveCardView);
        reserveCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmMedicalAppointment();
            }
        });
        reserveProgressBar = findViewById(R.id.reserveProgressBar);
        reserveTextView = findViewById(R.id.reserveTextView);

        setData();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void setData(){
        doctorPictureAppCompatImageView.setImageResource(specialist.getDoctorPictureId());
        fullNameTextView.setText(specialist.getFullName());
        specialityTextView.setText(specialist.getSpecialty());
        dateValueMessageTextView.setText(FuncionesFecha.formatLDateToText(FuncionesFecha.getLocalDateFromString(timeTableByDoctor.getStartDateTime())));
        timeValueMessageTextView.setText(FuncionesFecha.formatDateToHour(FuncionesFecha.getDateFromString(timeTableByDoctor.getStartDateTime())));
        patientValueMessageTextView.setText(ticket.getPatient().getFullName());
        dniValueMessageTextView.setText(ticket.getPatient().getDni());
        hospitalValueMessageTextView.setText(ticket.getHospital().getName());
        emailValueMessageTextView.setText(ticket.getPatient().getEmail());
    }
    private void disableReserveCardView() {
        reserveCardView.setEnabled(false);
        reserveTextView.setVisibility(View.INVISIBLE);
        reserveProgressBar.setVisibility(View.VISIBLE);
    }
    private void enableReserveCardView() {
        reserveCardView.setEnabled(true);
        reserveTextView.setVisibility(View.VISIBLE);
        reserveProgressBar.setVisibility(View.GONE);
    }
    private void confirmMedicalAppointment() {
        disableReserveCardView();

        JSONObject medicalAppointment = new JSONObject();
        try {
            medicalAppointment.put("specialistId", specialist.getId());
            medicalAppointment.put("parentId", ticket.getParentId());
            medicalAppointment.put("patientId", ticket.getPatientId());
            medicalAppointment.put("timeTableByDoctorId", timeTableByDoctor.getId());
            medicalAppointment.put("hospitalId", ticket.getHospitalId());
            medicalAppointment.put("state", "En espera");
            medicalAppointment.put("ticketId", ticket.getId());
            medicalAppointment.put("questionaryId", ticket.getQuestionaryId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(RepediatricsApi.MEDICAL_APPOINTMENTS_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(medicalAppointment)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ConfirmAppointmentActivity.this, "Cita médica registrada", Toast.LENGTH_LONG).show();
                        enableReserveCardView();

                        //confirmada la cita agregada correctamente
                        setResult(Activity.RESULT_FIRST_USER);
                        finish();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ConfirmAppointmentActivity.this, "Error al agregar cita", Toast.LENGTH_LONG).show();
                        enableReserveCardView();
                    }
                });
    }

}
