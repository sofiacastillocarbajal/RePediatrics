package pe.work.karique.repediatrics.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.session.SessionManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AddTicketAtachActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 10;

    private SessionManager sessionManager;
    private ImageButton backImageButton;
    private TextView titleTextView;
    private ProgressBar progressBar;

    private TextView patientValueTextView;
    private TextView hospitalValueTextView;
    private CardView confirmCardView;

    private User patient;
    private String patientName = "";
    private String hospitalName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket_atach);
        hide();

        patient = User.from(getIntent().getBundleExtra("patient"));
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

        patientValueTextView = findViewById(R.id.patientValueTextView);
        patientValueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seePatientDetails();
            }
        });
        hospitalValueTextView = findViewById(R.id.hospitalValueTextView);
        confirmCardView = findViewById(R.id.confirmCardView);
        confirmCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        patientName = getIntent().getStringExtra("patientName");
        hospitalName = getIntent().getStringExtra("hospitalName");

        setData();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void setData(){
        patientValueTextView.setText(String.format("%s (Ver perfil)", patientName));
        hospitalValueTextView.setText(hospitalName);
    }
    private void seePatientDetails(){
        Intent intent = new Intent(this, PatientDetailsActivity.class);
        intent.putExtras(patient.toBundle());
        startActivity(intent);
    }
}
