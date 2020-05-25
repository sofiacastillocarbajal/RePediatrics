package pe.work.karique.repediatrics.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.Ticket;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class TicketDetailsActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 2;

    private SessionManager sessionManager;
    private ImageButton backImageButton;
    private ProgressBar progressBar;

    private TextView ticketNameTextView;
    private TextView stateTextView;
    private TextView pediatricianValueTextView;
    private TextView patientValueTextView;
    private TextView resultValueTextView;
    private TextView documentValueTextView;
    private TextView commentsValueTextView;
    private TextView hospitalValueTextView;

    private CardView reserveCardView;
    private Button rejectMoreButton;

    private Ticket ticket;
    private AppCompatImageButton historyAppCompatImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details);
        hide();

        ticket = Ticket.from(getIntent().getExtras());
        sessionManager = SessionManager.getInstance(this);
        progressBar = findViewById(R.id.progressBar);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ticketNameTextView = findViewById(R.id.ticketNameTextView);
        stateTextView = findViewById(R.id.stateTextView);
        pediatricianValueTextView = findViewById(R.id.pediatricianValueTextView);
        patientValueTextView = findViewById(R.id.patientValueTextView);
        patientValueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seePatientDetails();
            }
        });
        resultValueTextView = findViewById(R.id.resultValueTextView);
        documentValueTextView = findViewById(R.id.documentValueTextView);
        commentsValueTextView = findViewById(R.id.commentsValueTextView);
        hospitalValueTextView = findViewById(R.id.hospitalValueTextView);

        reserveCardView = findViewById(R.id.reserveCardView);
        reserveCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTicketState(ticket.getId(), Ticket.STATE_APROBADO);
            }
        });
        rejectMoreButton = findViewById(R.id.rejectMoreButton);
        rejectMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTicketState(ticket.getId(), Ticket.STATE_CANCELADO);
            }
        });
        historyAppCompatImageButton = findViewById(R.id.historyAppCompatImageButton);
        historyAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTicketStatesActivity();
            }
        });

        if (ticket.getState().equals(Ticket.STATE_EN_ESPERA)){
            stateTextView.setTextColor(Color.parseColor("#FFC107"));
        }
        else if (ticket.getState().equals(Ticket.STATE_APROBADO)){
            stateTextView.setTextColor(Color.parseColor("#3ED362"));
        }
        else if (ticket.getState().equals(Ticket.STATE_CANCELADO)){
            stateTextView.setTextColor(Color.parseColor("#F76E8E"));
        }

        setData();
        setVisibilities();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void setData(){
        stateTextView.setText(String.format("Estado: %s", ticket.getState()));
        ticketNameTextView.setText(String.format("Ticket #%s", ticket.getId()));
        pediatricianValueTextView.setText(ticket.getPediatrician().getDrName());
        patientValueTextView.setText(String.format("%s (Ver perfil)", ticket.getPatient().getFullName()));
        resultValueTextView.setText(ticket.getQuestionary().getPercentage() + "%");
        documentValueTextView.setText("Revisar adjunto");
        commentsValueTextView.setText(ticket.getCommentFromPediatrician());
        hospitalValueTextView.setText(ticket.getHospital().getName());
    }
    private void setVisibilities(){
        if (sessionManager.isSpecialist()){
            if (ticket.getState().equals(Ticket.STATE_EN_ESPERA)) {
                reserveCardView.setVisibility(View.VISIBLE);
                rejectMoreButton.setVisibility(View.VISIBLE);
            }
        }

        if (ticket.getState().equals(Ticket.STATE_CANCELADO)){
            rejectMoreButton.setVisibility(View.GONE);
        }

        if (sessionManager.isAdmin()) {
            reserveCardView.setVisibility(View.GONE);
            rejectMoreButton.setVisibility(View.GONE);
        }
        if (sessionManager.isParent()) {
            reserveCardView.setVisibility(View.GONE);
            rejectMoreButton.setVisibility(View.GONE);
        }
        if (sessionManager.isDoctor()) {
            reserveCardView.setVisibility(View.GONE);
            rejectMoreButton.setVisibility(View.GONE);
        }
    }
    private void updateTicketState(String ticketId, String state){
        disableRegisterButton();

        JSONObject ticketJsonObject = new JSONObject();
        try {
            ticketJsonObject.put("id", ticketId);
            ticketJsonObject.put("userId", sessionManager.getid());
            ticketJsonObject.put("state", state);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.put(RepediatricsApi.UPDATE_TICKET_STATE_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(ticketJsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(TicketDetailsActivity.this, "Estado actualizado", Toast.LENGTH_LONG).show();
                        enableRegisterButton();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(TicketDetailsActivity.this, "Error al actualizar los datos", Toast.LENGTH_LONG).show();
                        enableRegisterButton();
                    }
                });
    }
    private void seePatientDetails(){
        Intent intent = new Intent(this, PatientDetailsActivity.class);
        intent.putExtras(ticket.getParent().toBundle());
        startActivity(intent);
    }
    private void showTicketStatesActivity(){
        Intent intent = new Intent(this, TicketStatesActivity.class);
        intent.putExtras(ticket.toBundle());
        startActivity(intent);
    }

    private void enableRegisterButton() {
        rejectMoreButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
    private void disableRegisterButton() {
        rejectMoreButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }
}
