package pe.work.karique.repediatrics.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.cardview.widget.CardView;
import pe.work.karique.repediatrics.R;
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
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.libizo.CustomEditText;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;

import java.util.regex.Pattern;

public class AddPatientActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    public static int REQUEST_CODE = 6;

    private SessionManager sessionManager;
    private ImageButton backImageButton;

    TextView titleTextView;

    CustomEditText usernameCustomEditText;
    CustomEditText nameCustomEditText;
    CustomEditText lastNameCustomEditText;
    AppCompatRadioButton maleAppCompatRadioButton;
    AppCompatRadioButton femaleAppCompatRadioButton;
    CustomEditText dniCustomEditText;
    CustomEditText nationalityCustomEditText;
    CustomEditText birthPlaceCustomEditText;
    CustomEditText birthDayCustomEditText;
    CustomEditText birthMonthCustomEditText;
    CustomEditText birthYearCustomEditText;
    CustomEditText emailCustomEditText;
    MaterialSpinner insuranceTypeMaterialSpinner;
    CardView registerCardView;
    ProgressBar registerProgressBar;
    TextView registerTextView;

    LocalDate selectedLocalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        hide();

        sessionManager = SessionManager.getInstance(this);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        selectedLocalDate = CalendarDay.today().getDate();
        titleTextView = findViewById(R.id.titleTextView);

        usernameCustomEditText = findViewById(R.id.usernameCustomEditText);
        nameCustomEditText = findViewById(R.id.nameCustomEditText);
        lastNameCustomEditText = findViewById(R.id.lastNameCustomEditText);
        maleAppCompatRadioButton = findViewById(R.id.maleAppCompatRadioButton);
        femaleAppCompatRadioButton = findViewById(R.id.femaleAppCompatRadioButton);
        dniCustomEditText = findViewById(R.id.dniCustomEditText);
        nationalityCustomEditText = findViewById(R.id.nationalityCustomEditText);
        birthPlaceCustomEditText = findViewById(R.id.birthPlaceCustomEditText);
        birthDayCustomEditText = findViewById(R.id.birthDayCustomEditText);
        birthMonthCustomEditText = findViewById(R.id.birthMonthCustomEditText);
        birthYearCustomEditText = findViewById(R.id.birthYearCustomEditText);
        emailCustomEditText = findViewById(R.id.emailCustomEditText);
        insuranceTypeMaterialSpinner = findViewById(R.id.insuranceTypeMaterialSpinner);
        registerCardView = findViewById(R.id.registerCardView);
        registerProgressBar = findViewById(R.id.registerProgressBar);
        registerTextView = findViewById(R.id.registerTextView);
        backImageButton = findViewById(R.id.backImageButton);

        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        registerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateRegister();
            }
        });
        birthDayCustomEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(selectedLocalDate);
            }
        });
        birthMonthCustomEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(selectedLocalDate);
            }
        });
        birthYearCustomEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(selectedLocalDate);
            }
        });

        birthDayCustomEditText.setText(String.valueOf(selectedLocalDate.getDayOfMonth()));
        birthMonthCustomEditText.setText(String.valueOf(selectedLocalDate.getMonthValue()));
        birthYearCustomEditText.setText(String.valueOf(selectedLocalDate.getYear()));

        insuranceTypeMaterialSpinner.setItems(
                "SIS",
                "EsSalud",
                "Seguros de las Fuerzas Armadas",
                "Seguro de Salud de la Policía",
                "Mapfre",
                "La Positiva"
        );
        insuranceTypeMaterialSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });
    }

    private void validateRegister() {
        if (usernameCustomEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Ingrese un nombre de usuario", Toast.LENGTH_LONG).show();
            return;
        }
        if (nameCustomEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Ingrese un nombre valido", Toast.LENGTH_LONG).show();
            return;
        }
        if (lastNameCustomEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Ingrese un apellido valido", Toast.LENGTH_LONG).show();
            return;
        }
        if (dniCustomEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Ingrese un DNI valido", Toast.LENGTH_LONG).show();
            return;
        }
        if (nationalityCustomEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Ingrese una nacionalidad valida", Toast.LENGTH_LONG).show();
            return;
        }
        if (emailCustomEditText.getText().toString().isEmpty() ||
                !isValidEmail(emailCustomEditText.getText().toString())) {
            Toast.makeText(this, "Ingrese un correo valida", Toast.LENGTH_LONG).show();
            return;
        }

        register();
    }

    private void showDatePickerDialog(LocalDate localDate){
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                localDate.getYear(),
                localDate.getMonthValue() - 1,
                localDate.getDayOfMonth()
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.showYearPickerFirst(true);
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private String getGender(){
        return maleAppCompatRadioButton.isChecked() ? "Masculino" : "Femenino";
    }

    private void register() {
        desableRegisterButton();
        JSONObject patientJsonObject = new JSONObject();

        try {
            patientJsonObject.put("name", nameCustomEditText.getText().toString());
            patientJsonObject.put("lastName", lastNameCustomEditText.getText().toString());
            patientJsonObject.put("userTypeId", 4);
            patientJsonObject.put("gender", getGender());
            patientJsonObject.put("dni", dniCustomEditText.getText().toString());
            patientJsonObject.put("nationality", nationalityCustomEditText.getText().toString());
            patientJsonObject.put("birthDate", FuncionesFecha.formatDateLDApi(selectedLocalDate));
            patientJsonObject.put("birthPlace", birthPlaceCustomEditText.getText().toString());
            patientJsonObject.put("insuranceType", insuranceTypeMaterialSpinner.getItems().get(insuranceTypeMaterialSpinner.getSelectedIndex()));
            patientJsonObject.put("email", emailCustomEditText.getText().toString());
            patientJsonObject.put("rate", 5);
            patientJsonObject.put("active", true);
            patientJsonObject.put("speciality", "Oncologia");
            patientJsonObject.put("username", usernameCustomEditText.getText().toString());
            patientJsonObject.put("password", "123456");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject parentPatientJsonObject = new JSONObject();
        try {
            parentPatientJsonObject.put("parentId", sessionManager.getid());
            parentPatientJsonObject.putOpt("patient", patientJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(RepediatricsApi.PARENTS_PATIENT_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(parentPatientJsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(AddPatientActivity.this, "Su hijo fue registrado correctamente", Toast.LENGTH_LONG).show();
                        enableRegisterButton();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 409){
                            Toast.makeText(AddPatientActivity.this, "El nombre de usuario que ingresó ya existe", Toast.LENGTH_LONG).show();
                            enableRegisterButton();
                        }
                        else {
                            Toast.makeText(AddPatientActivity.this, "Hubo un error al crear el usuario", Toast.LENGTH_LONG).show();
                            enableRegisterButton();
                        }
                    }
                });
    }

    private void desableRegisterButton(){
        registerCardView.setEnabled(false);
        registerTextView.setVisibility(View.INVISIBLE);
        registerProgressBar.setVisibility(View.VISIBLE);
    }

    private void enableRegisterButton(){
        registerCardView.setEnabled(true);
        registerTextView.setVisibility(View.VISIBLE);
        registerProgressBar.setVisibility(View.GONE);
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        selectedLocalDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
        birthDayCustomEditText.setText(String.valueOf(dayOfMonth));
        birthMonthCustomEditText.setText(String.valueOf(monthOfYear + 1));
        birthYearCustomEditText.setText(String.valueOf(year));
    }
}
