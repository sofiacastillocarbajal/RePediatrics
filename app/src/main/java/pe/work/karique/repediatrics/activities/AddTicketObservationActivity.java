package pe.work.karique.repediatrics.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.session.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AddTicketObservationActivity extends AppCompatActivity {

    public static int REQUEST_CODE = 9;

    private SessionManager sessionManager;
    private ImageButton backImageButton;
    private TextView titleTextView;
    private ProgressBar progressBar;

    private TextView patientValueTextView;
    private TextView resultValueTextView;
    private TextView commentsValueCustomEditText;
    private CardView confirmCardView;

    private String patientName;
    private String questionaryResult;
    private User patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket_observation);
        hide();

        patient = User.from(getIntent().getBundleExtra("patient"));
        patientName = getIntent().getStringExtra("patientName");
        questionaryResult = getIntent().getStringExtra("questionaryResult");

        sessionManager = SessionManager.getInstance(this);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_FIRST_USER);
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
        resultValueTextView = findViewById(R.id.resultValueTextView);
        commentsValueCustomEditText = findViewById(R.id.commentsValueCustomEditText);
        confirmCardView = findViewById(R.id.confirmCardView);
        confirmCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveComment();
            }
        });

        setData();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void setData(){
        patientValueTextView.setText(String.format("%s(Ver perfil)", patientName));
        resultValueTextView.setText(questionaryResult);
    }
    private void saveComment(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("commentToPatient", commentsValueCustomEditText.getText().toString());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
    private void seePatientDetails(){
        Intent intent = new Intent(this, PatientDetailsActivity.class);
        intent.putExtras(patient.toBundle());
        startActivity(intent);
    }
}
