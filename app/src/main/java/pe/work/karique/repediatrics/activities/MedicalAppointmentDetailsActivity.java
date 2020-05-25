package pe.work.karique.repediatrics.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.MedicalAppointment;
import pe.work.karique.repediatrics.models.Ticket;
import pe.work.karique.repediatrics.session.SessionManager;
import pe.work.karique.repediatrics.util.FuncionesFecha;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;

public class MedicalAppointmentDetailsActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 1;

    private SessionManager sessionManager;
    private ImageButton backImageButton;

    private TextView stateTextView;
    private TextView dateValueTextView;
    private TextView placeValueTextView;
    private TextView addressValueTextView;
    private TextView doctorValueTextView;
    private TextView patientValueTextView;
    private TextView recomendationValueTextView;
    private TextView seeTicketTitleTextView;
    private TextView seeTicketValueTextView;
    private CardView reserveCardView;
    private Button rejectMAMoreButton;

    private MedicalAppointment medicalAppointment;
    private AppCompatImageButton historyAppCompatImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_appointment_details);
        hide();

        sessionManager = SessionManager.getInstance(this);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        stateTextView = findViewById(R.id.stateTextView);
        dateValueTextView = findViewById(R.id.dateValueTextView);
        placeValueTextView = findViewById(R.id.placeValueTextView);
        addressValueTextView = findViewById(R.id.addressValueTextView);
        doctorValueTextView = findViewById(R.id.doctorValueTextView);
        patientValueTextView = findViewById(R.id.patientValueTextView);
        recomendationValueTextView = findViewById(R.id.recomendationValueTextView);
        seeTicketTitleTextView = findViewById(R.id.seeTicketTitleTextView);
        seeTicketValueTextView = findViewById(R.id.seeTicketValueTextView);

        reserveCardView = findViewById(R.id.reserveCardView);
        reserveCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        rejectMAMoreButton = findViewById(R.id.rejectMAMoreButton);
        rejectMAMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        historyAppCompatImageButton = findViewById(R.id.historyAppCompatImageButton);
        historyAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMedicalAppointmentsStatesActivity();
            }
        });

        medicalAppointment = MedicalAppointment.from(getIntent().getExtras());
        setData();
        setControlsVisibility();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void setData(){
        if (medicalAppointment.getState().equals(MedicalAppointment.STATE_EN_ESPERA)){
            stateTextView.setTextColor(Color.parseColor("#FFC107"));
        }
        else if (medicalAppointment.getState().equals(MedicalAppointment.STATE_APROBADO)){
            stateTextView.setTextColor(Color.parseColor("#3ED362"));
        }
        else if (medicalAppointment.getState().equals(MedicalAppointment.STATE_CANCELADO)){
            stateTextView.setTextColor(Color.parseColor("#F76E8E"));
        }
        stateTextView.setText(String.format("Estado: %s", medicalAppointment.getState()));

        Date date = FuncionesFecha.getDateFromString(medicalAppointment.getTimeTableByDoctor().getStartDateTime());
        String dateF = FuncionesFecha.formatDate(date);
        String timeF = FuncionesFecha.formatDateToHour(date);
        dateValueTextView.setText(String.format("%s - %s", dateF, timeF));

        placeValueTextView.setText(medicalAppointment.getHospital().getName());
        addressValueTextView.setText(medicalAppointment.getHospital().getAddress());
        doctorValueTextView.setText(medicalAppointment.getSpecialist().getDrName());
        patientValueTextView.setText(medicalAppointment.getPatient().getName());
        seeTicketTitleTextView.setText(String.format("TICKET #%s", medicalAppointment.getTicketId()));
        seeTicketValueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTicketDetails(medicalAppointment.getTicket());
            }
        });
    }
    private void setControlsVisibility(){
        if (sessionManager.isAdmin()){
            reserveCardView.setVisibility(View.GONE);
            rejectMAMoreButton.setVisibility(View.GONE);
        }
        if (sessionManager.isParent()){
            reserveCardView.setVisibility(View.GONE);
            rejectMAMoreButton.setVisibility(View.GONE);
        }
        if (sessionManager.isDoctor()){
            reserveCardView.setVisibility(View.GONE);
            rejectMAMoreButton.setVisibility(View.GONE);
        }
    }
    private void showTicketDetails(Ticket ticket){
        Intent intent = new Intent(this, TicketDetailsActivity.class);
        intent.putExtras(ticket.toBundle());
        startActivityForResult(intent, TicketDetailsActivity.REQUEST_CODE);
    }
    private void showMedicalAppointmentsStatesActivity(){
        Intent intent = new Intent(this, MedicalAppointmentStatesActivity.class);
        intent.putExtras(medicalAppointment.toBundle());
        startActivity(intent);
    }
}
