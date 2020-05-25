package pe.work.karique.repediatrics.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.adapters.MedicalAppointmentStateAdapter;
import pe.work.karique.repediatrics.models.MedicalAppointment;
import pe.work.karique.repediatrics.models.MedicalAppointmentState;
import pe.work.karique.repediatrics.models.TicketState;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;

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

public class MedicalAppointmentStatesActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private ImageButton backImageButton;
    private ProgressBar progressBar;

    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;

    private RecyclerView statesRecyclerView;
    private MedicalAppointmentStateAdapter medicalAppointmentStateAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MedicalAppointmentState> medicalAppointmentStates;

    private MedicalAppointment medicalAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_appointment_states);
        hide();

        medicalAppointment = MedicalAppointment.from(getIntent().getExtras());
        sessionManager = SessionManager.getInstance(this);
        progressBar = findViewById(R.id.progressBar);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        statesRecyclerView = findViewById(R.id.statesRecyclerView);
        messageConstraintLayout = findViewById(R.id.messageConstraintLayout);
        messageTextView = findViewById(R.id.messageTextView);

        medicalAppointmentStates = new ArrayList<>();
        medicalAppointmentStateAdapter = new MedicalAppointmentStateAdapter(medicalAppointmentStates);
        layoutManager = new LinearLayoutManager(this);
        statesRecyclerView.setAdapter(medicalAppointmentStateAdapter);
        statesRecyclerView.setLayoutManager(layoutManager);

        getStates();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    public void getStates(){
        progressBar.setVisibility(View.VISIBLE);

        AndroidNetworking.get(RepediatricsApi.STATES_BY_MEDICAL_APPOINTMENT(medicalAppointment.getId()))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);
                        messageConstraintLayout.setVisibility(View.GONE);

                        medicalAppointmentStates.clear();
                        medicalAppointmentStates.addAll(MedicalAppointmentState.from(response));
                        medicalAppointmentStateAdapter.notifyDataSetChanged();

                        if (medicalAppointmentStates.isEmpty()){
                            showNoDataMessage("Sin datos");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        showNoDataMessage("Error de carga");
                    }
                });
    }
    private void showNoDataMessage(String message){
        messageTextView.setText(message);
        messageConstraintLayout.setVisibility(View.VISIBLE);
    }
}
