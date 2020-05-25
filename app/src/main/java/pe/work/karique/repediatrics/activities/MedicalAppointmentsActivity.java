package pe.work.karique.repediatrics.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.adapters.MedicalAppointmentsAdapter;
import pe.work.karique.repediatrics.fragments.ParentHomeFragment;
import pe.work.karique.repediatrics.models.MedicalAppointment;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MedicalAppointmentsActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 11;

    private SessionManager sessionManager;
    private ImageButton backImageButton;
    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;

    private ProgressBar progressBar;
    private RecyclerView medicalAppointmentsRecyclerView;
    private MedicalAppointmentsAdapter medicalAppointmentsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MedicalAppointment> medicalAppointments;

    private CardView reserveCardView;
    private ProgressBar reserveProgressBar;
    private TextView reserveTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_appointments);
        hide();

        sessionManager = SessionManager.getInstance(this);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressBar = findViewById(R.id.progressBar);

        medicalAppointmentsRecyclerView = findViewById(R.id.medicalAppointmentsRecyclerView);
        messageConstraintLayout = findViewById(R.id.messageConstraintLayout);
        messageTextView = findViewById(R.id.messageTextView);
        medicalAppointments = new ArrayList<>();
        medicalAppointmentsAdapter = new MedicalAppointmentsAdapter(medicalAppointments);
        medicalAppointmentsAdapter.setOnMedicalAppointmentClicked(new MedicalAppointmentsAdapter.OnMedicalAppointmentClickedListener() {
            @Override
            public void OnMedicalAppointmentClicked(MedicalAppointment medicalAppointment) {
                showMedicalAppointmentDetails(medicalAppointment);
            }
        });
        layoutManager = new LinearLayoutManager(this);
        medicalAppointmentsRecyclerView.setAdapter(medicalAppointmentsAdapter);
        medicalAppointmentsRecyclerView.setLayoutManager(layoutManager);

        reserveCardView = findViewById(R.id.reserveCardView);
        reserveCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooseTicketForMedicalAppointment();
            }
        });
        reserveProgressBar = findViewById(R.id.reserveProgressBar);
        reserveTextView = findViewById(R.id.reserveTextView);

        getMedicalAppointments();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void showMedicalAppointmentDetails(MedicalAppointment medicalAppointment){
        Intent intent = new Intent(this, MedicalAppointmentDetailsActivity.class);
        intent.putExtras(medicalAppointment.toBundle());
        startActivityForResult(intent, MedicalAppointmentDetailsActivity.REQUEST_CODE);
    }
    public void getMedicalAppointments(){
        medicalAppointmentsRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        AndroidNetworking.get(RepediatricsApi.MEDICAL_APPOINTMENTS_BY_PARENT(sessionManager.getid()))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        medicalAppointmentsRecyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        messageConstraintLayout.setVisibility(View.GONE);

                        medicalAppointments.clear();
                        medicalAppointments.addAll(MedicalAppointment.from(response));
                        medicalAppointmentsAdapter.notifyDataSetChanged();

                        if (medicalAppointments.isEmpty()){
                            showNoDataMessage();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        medicalAppointmentsRecyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        showNoDataMessage();
                    }
                });
    }
    private void showNoDataMessage(){
        messageTextView.setText(getResources().getString(R.string.no_data));
        messageConstraintLayout.setVisibility(View.VISIBLE);
    }
    private void showChooseTicketForMedicalAppointment(){
        Intent intent = new Intent(this, ChooseTicketForMedicalAppointmentActivity.class);
        startActivityForResult(intent, ChooseTicketForMedicalAppointmentActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Cita medica agregada correctamente
        if (requestCode == ChooseTicketForMedicalAppointmentActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_FIRST_USER) {
                getMedicalAppointments();
            }
        }
    }
}




