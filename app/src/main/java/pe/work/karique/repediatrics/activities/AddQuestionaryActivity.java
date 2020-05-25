package pe.work.karique.repediatrics.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.cardview.widget.CardView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.Ticket;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.libizo.CustomEditText;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class AddQuestionaryActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 5;

    private static int STATE_MODE_UNVALIDATE = 1;
    private static int STATE_MODE_VALIDATED = 2;
    private int STATE_MODE = STATE_MODE_UNVALIDATE;

    private SessionManager sessionManager;
    private ImageButton backImageButton;

    private TextView titleTextView;

    private CustomEditText usernameCustomEditText;
    private CustomEditText nameCustomEditText;
    private CustomEditText lastNameCustomEditText;
    private AppCompatRadioButton maleAppCompatRadioButton;
    private AppCompatRadioButton femaleAppCompatRadioButton;
    private CustomEditText dniCustomEditText;
    private CustomEditText nationalityCustomEditText;
    private CustomEditText birthDayCustomEditText;
    private CustomEditText birthMonthCustomEditText;
    private CustomEditText birthYearCustomEditText;
    private CustomEditText birthPlaceCustomEditText;
    private CustomEditText emailCustomEditText;
    private CustomEditText insuranceTypeCustomEditText;
    private CardView startCardView;
    private TextView startTextView;
    private RadioGroup genderRadioGroup;

    private ProgressBar registerProgressBar;

    private User patient;
    private String parentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_questionary);
        hide();

        sessionManager = SessionManager.getInstance(this);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleTextView = findViewById(R.id.titleTextView);

        usernameCustomEditText = findViewById(R.id.usernameCustomEditText);
        nameCustomEditText = findViewById(R.id.nameCustomEditText);
        lastNameCustomEditText = findViewById(R.id.lastNameCustomEditText);
        maleAppCompatRadioButton = findViewById(R.id.maleAppCompatRadioButton);
        femaleAppCompatRadioButton = findViewById(R.id.femaleAppCompatRadioButton);
        dniCustomEditText = findViewById(R.id.dniCustomEditText);
        nationalityCustomEditText = findViewById(R.id.nationalityCustomEditText);
        birthDayCustomEditText = findViewById(R.id.birthDayCustomEditText);
        birthMonthCustomEditText = findViewById(R.id.birthMonthCustomEditText);
        birthYearCustomEditText = findViewById(R.id.birthYearCustomEditText);
        birthPlaceCustomEditText = findViewById(R.id.birthPlaceCustomEditText);
        emailCustomEditText = findViewById(R.id.emailCustomEditText);
        insuranceTypeCustomEditText = findViewById(R.id.insuranceTypeCustomEditText);
        registerProgressBar = findViewById(R.id.registerProgressBar);
        startTextView = findViewById(R.id.startTextView);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);

        startCardView = findViewById(R.id.startCardView);
        startCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (STATE_MODE == STATE_MODE_UNVALIDATE){
                    validateUser(usernameCustomEditText.getText().toString());
                }
                else if (STATE_MODE == STATE_MODE_VALIDATED){
                    startResolveQuestionaryActivity();
                }
            }
        });

        setInvisibleControls();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void setVisibleControls(){
        setControlsVisibility(View.VISIBLE);
    }
    private void setInvisibleControls(){
        setControlsVisibility(View.GONE);
    }
    private void setControlsVisibility(int visibility) {
        nameCustomEditText.setVisibility(visibility);
        lastNameCustomEditText.setVisibility(visibility);
        maleAppCompatRadioButton.setVisibility(visibility);
        dniCustomEditText.setVisibility(visibility);
        nationalityCustomEditText.setVisibility(visibility);
        birthDayCustomEditText.setVisibility(visibility);
        birthMonthCustomEditText.setVisibility(visibility);
        birthYearCustomEditText.setVisibility(visibility);
        birthPlaceCustomEditText.setVisibility(visibility);
        emailCustomEditText.setVisibility(visibility);
        insuranceTypeCustomEditText.setVisibility(visibility);
        //startCardView.setVisibility(visibility);
        genderRadioGroup.setVisibility(visibility);
    }
    private void validateUser(String username){
        startTextView.setVisibility(View.INVISIBLE);
        registerProgressBar.setVisibility(View.VISIBLE);

        AndroidNetworking.get(RepediatricsApi.PATIENT_BY_USERNAME(username))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        startTextView.setVisibility(View.VISIBLE);
                        registerProgressBar.setVisibility(View.GONE);
                        try {
                            if (response.getBoolean("exists")){
                                patient = User.from(response.getJSONObject("user"));
                                parentId = response.getString("parentId");
                                setPatientData(patient);
                                STATE_MODE = STATE_MODE_VALIDATED;
                                startTextView.setText("Comenzar cuestionario");
                            }
                            else {
                                Toast.makeText(AddQuestionaryActivity.this, "No se encontró al paciente", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddQuestionaryActivity.this, "Error al hacer la busqueda", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        startTextView.setVisibility(View.VISIBLE);
                        registerProgressBar.setVisibility(View.GONE);
                        Toast.makeText(AddQuestionaryActivity.this, "Error al hacer la busqueda", Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void startResolveQuestionaryActivity(){
        Intent intent = new Intent(this, ResolveQuestionaryActivity.class);
        intent.putExtra("patient", patient.toBundle());
        intent.putExtra("parentId", parentId);
        startActivityForResult(intent, ResolveQuestionaryActivity.REQUEST_CODE);
    }
    private void setPatientData(User user) {
        setVisibleControls();
        nameCustomEditText.setText(user.getName());
        lastNameCustomEditText.setText(user.getLastName());
        maleAppCompatRadioButton.setText(user.getGender());
        dniCustomEditText.setText(user.getDni());
        nationalityCustomEditText.setText(user.getNationality());

        Date bd = FuncionesFecha.getDateFromString(user.getBirthDate());
        birthDayCustomEditText.setText(String.valueOf(FuncionesFecha.getDayFromDate(bd)));
        birthMonthCustomEditText.setText(String.valueOf(FuncionesFecha.getMonthFromDate(bd)));
        birthYearCustomEditText.setText(String.valueOf(FuncionesFecha.getYearFromDate(bd)));

        birthPlaceCustomEditText.setText(user.getBirthPlace());
        emailCustomEditText.setText(user.getEmail());
        insuranceTypeCustomEditText.setText(user.getInsuranceType());
        maleAppCompatRadioButton.setChecked(user.isMale());
        femaleAppCompatRadioButton.setChecked(user.isFemale());

        genderRadioGroup.setEnabled(false);
        maleAppCompatRadioButton.setEnabled(false);
        femaleAppCompatRadioButton.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Ticket agregado correctamente
        if (requestCode == ResolveQuestionaryActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK);
                finish();
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        }
    }
}
