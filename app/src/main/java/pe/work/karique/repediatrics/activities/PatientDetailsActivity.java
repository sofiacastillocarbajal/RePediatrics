package pe.work.karique.repediatrics.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.MedicalAppointment;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.session.SessionManager;
import pe.work.karique.repediatrics.util.FuncionesFecha;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class PatientDetailsActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 3;

    private SessionManager sessionManager;
    private ImageButton backImageButton;

    private TextView usernameValueTextView;
    private TextView fullNameValueTextView;
    private TextView genderValueTextView;
    private TextView dniValueTextView;
    private TextView nationalityValueTextView;
    private TextView birthDateValueTextView;
    private TextView birthPlaceValueTextView;
    private TextView insuranceValueTextView;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        hide();

        sessionManager = SessionManager.getInstance(this);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        usernameValueTextView = findViewById(R.id.usernameValueTextView);
        fullNameValueTextView = findViewById(R.id.fullNameValueTextView);
        genderValueTextView = findViewById(R.id.genderValueTextView);
        dniValueTextView = findViewById(R.id.dniValueTextView);
        nationalityValueTextView = findViewById(R.id.nationalityValueTextView);
        birthDateValueTextView = findViewById(R.id.birthDateValueTextView);
        birthPlaceValueTextView = findViewById(R.id.birthPlaceValueTextView);
        insuranceValueTextView = findViewById(R.id.insuranceValueTextView);

        user = User.from(getIntent().getExtras());
        setData();
    }

    private void setData(){
        usernameValueTextView.setText(user.getUsername());
        fullNameValueTextView.setText(user.getFullName());
        genderValueTextView.setText(user.getGender());
        dniValueTextView.setText(String.format("%s (DNI)", user.getDni()));
        nationalityValueTextView.setText(user.getNationality());
        birthDateValueTextView.setText(FuncionesFecha.formatDate(FuncionesFecha.getDateFromString(user.getBirthDate())) + " (" +FuncionesFecha.getYearsFromNow(FuncionesFecha.getDateFromString(user.getBirthDate())) + " AÑOS)");
        birthPlaceValueTextView.setText(user.getBirthPlace());
        insuranceValueTextView.setText(user.getInsuranceType());
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}
