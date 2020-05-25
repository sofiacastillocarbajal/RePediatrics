package pe.work.karique.repediatrics.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.Hospital;
import pe.work.karique.repediatrics.models.Questionary;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;

import android.app.Activity;
import android.content.Intent;
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
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.libizo.CustomEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddTicketActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 8;

    private SessionManager sessionManager;
    private ImageButton backImageButton;
    private TextView titleTextView;
    private ProgressBar progressBar;

    private User patient;
    private Questionary questionary;
    private String parentId;
    private String commentToPatient = "";
    private String percentage = "";
    private String textResult = "bajo";

    private List<Hospital> hospitals;
    private MaterialSpinner hospitalValueMaterialSpinner;
    private ProgressBar hospitalValueProgressBar;

    private TextView ticketNumberValueTextView;
    private TextView pediatricianValueTextView;
    private TextView patientValueTextView;
    private TextView resultValueTextView;
    private TextView documentValueTextView;
    private CustomEditText commentsValueTextView;
    private CardView confirmCardView;
    private Button cancelButton;
    private TextView confirmTextView;
    private ProgressBar confirmProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);
        hide();

        patient = User.from(getIntent().getBundleExtra("patient"));
        questionary = Questionary.from(getIntent().getBundleExtra("questionary"));
        parentId = getIntent().getStringExtra("parentId");
        commentToPatient = getIntent().getStringExtra("commentToPatient");
        percentage = getIntent().getStringExtra("percentage");
        textResult = getIntent().getStringExtra("textResult");

        sessionManager = SessionManager.getInstance(this);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleTextView = findViewById(R.id.titleTextView);
        progressBar = findViewById(R.id.progressBar);

        confirmTextView = findViewById(R.id.confirmTextView);
        confirmProgressBar = findViewById(R.id.confirmProgressBar);

        hospitals = new ArrayList<>();
        hospitalValueMaterialSpinner = findViewById(R.id.hospitalValueMaterialSpinner);
        hospitalValueProgressBar = findViewById(R.id.hospitalValueProgressBar);

        ticketNumberValueTextView = findViewById(R.id.ticketNumberValueTextView);
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
        documentValueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddTicketAtach();
            }
        });
        commentsValueTextView = findViewById(R.id.commentsValueTextView);
        confirmCardView = findViewById(R.id.confirmCardView);
        confirmCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuestionary();
            }
        });
        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        setData();
        getTicketNumber();
        getHospitals();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void getHospitals(){
        hospitalValueProgressBar.setVisibility(View.VISIBLE);

        AndroidNetworking.get(RepediatricsApi.HOSPITALS_URL)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hospitalValueProgressBar.setVisibility(View.GONE);
                        hospitals.clear();
                        hospitals.addAll(Hospital.from(response));
                        hospitalValueMaterialSpinner.setItems(hospitals);
                        hospitalValueMaterialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                            }
                        });
                    }

                    @Override
                    public void onError(ANError anError) {
                        hospitalValueProgressBar.setVisibility(View.GONE);
                        Toast.makeText(AddTicketActivity.this, "Hospitales - Error al traer los hospitales", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getTicketNumber(){
        AndroidNetworking.get(RepediatricsApi.LAST_TICKET_NUMBER_URL)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String ticketNumber = "";
                        try {
                            ticketNumber = response.getString("ticketNumber");
                            ticketNumberValueTextView.setText(String.format("#%s", ticketNumber));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(AddTicketActivity.this, "Error obteniendo el ultimo numero de ticket", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void setData(){
        ticketNumberValueTextView.setText("Cargando...");
        pediatricianValueTextView.setText(sessionManager.getDrName());
        patientValueTextView.setText(String.format("%s (Ver perfil)", patient.getFullName()));
        resultValueTextView.setText(String.format("%s%% (%s)", percentage, textResult));
        commentsValueTextView.setText(String.format("El paciente tiene una %s probabilidad de poseer la enfermedad. Con un %s%% en el test.", textResult, percentage));
    }
    private void startAddTicketAtach(){
        Intent intent = new Intent(this, AddTicketAtachActivity.class);
        intent.putExtra("patient", patient.toBundle());
        intent.putExtra("patientName", patient.getFullName());
        intent.putExtra("hospitalName", hospitals.get(hospitalValueMaterialSpinner.getSelectedIndex()).getName());
        startActivityForResult(intent, AddTicketAtachActivity.REQUEST_CODE);
    }
    private void addQuestionary(){
        disableRegisterButton();
        AndroidNetworking.post(RepediatricsApi.QUESTIONARIES_RESPONSES_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(questionary.toPostJsonObject())
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(AddTicketActivity.this, "Cuestionario registrado", Toast.LENGTH_LONG).show();
                        enableRegisterButton();
                        Questionary questionary = null;
                        try {
                            questionary = Questionary.from(response.getJSONObject("questionary"));
                            addTicket(questionary);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(AddTicketActivity.this, "Cuestionario error", Toast.LENGTH_LONG).show();
                        enableRegisterButton();
                    }
                });
    }
    private void addTicket(Questionary questionary){
        disableRegisterButton();

        JSONObject newticket = new JSONObject();
        try {
            newticket.put("pediatricianId", sessionManager.getid());
            newticket.put("parentId", parentId);
            newticket.put("patientId", patient.getId());
            newticket.put("hospitalId", hospitals.get(hospitalValueMaterialSpinner.getSelectedIndex()).getId());
            newticket.put("questionaryId", questionary.getId());
            newticket.put("commentFromPediatrician", commentsValueTextView.getText().toString());
            newticket.put("commentToPatient", commentToPatient);
            newticket.put("state", "En espera");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(RepediatricsApi.TICKETS_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(newticket)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(AddTicketActivity.this, "Ticket registrado", Toast.LENGTH_LONG).show();
                        enableRegisterButton();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(AddTicketActivity.this, "Error al registrar ticket", Toast.LENGTH_LONG).show();
                        enableRegisterButton();
                    }
                });
    }
    private void seePatientDetails(){
        Intent intent = new Intent(this, PatientDetailsActivity.class);
        intent.putExtras(patient.toBundle());
        startActivity(intent);
    }

    private void disableRegisterButton(){
        confirmCardView.setEnabled(false);
        confirmTextView.setVisibility(View.INVISIBLE);
        confirmProgressBar.setVisibility(View.VISIBLE);
    }
    private void enableRegisterButton(){
        confirmCardView.setEnabled(true);
        confirmTextView.setVisibility(View.VISIBLE);
        confirmProgressBar.setVisibility(View.GONE);
    }
}
