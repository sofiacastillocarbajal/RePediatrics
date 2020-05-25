package pe.work.karique.repediatrics.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.cardview.widget.CardView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.util.FuncionesFecha;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
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

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static int REQUEST_CODE = 4;

    public final static int REGISTER_TYPE_PARENT = 1;
    public final static int REGISTER_TYPE_PEDIATRICIAN = 2;
    public final static int REGISTER_TYPE_SPECIALIST = 3;

    private static final int UI_ANIMATION_DELAY = 10;
    private final Handler mHideHandler = new Handler();
    private View mContentView;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    ImageButton backImageButton;

    int registerType = REGISTER_TYPE_PARENT;
    TextView titleTextView;

    CustomEditText usernameCustomEditText;
    CustomEditText passwordCustomEditText;
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
        setContentView(R.layout.activity_register);
        hideToolbar();

        registerType = getIntent().getIntExtra("registerType", REGISTER_TYPE_PARENT);
        selectedLocalDate = CalendarDay.today().getDate();

        mContentView = findViewById(R.id.fullscreenConstraintLayout);
        titleTextView = findViewById(R.id.titleTextView);

        usernameCustomEditText = findViewById(R.id.usernameCustomEditText);
        passwordCustomEditText = findViewById(R.id.passwordCustomEditText);
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
                hideToolbar();
            }
        });

        setTitle();
    }

    @Override
    protected void onResume() {
        hideToolbar();
        super.onResume();
    }

    private void setTitle(){
        switch (registerType){
            case REGISTER_TYPE_PARENT:
                titleTextView.setText("Apoderado | Datos");
                break;
            case REGISTER_TYPE_PEDIATRICIAN:
                titleTextView.setText("Pediatra | Datos");
                break;
            case REGISTER_TYPE_SPECIALIST:
                titleTextView.setText("Especialista | Datos");
                break;
        }
    }

    private void hideToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        selectedLocalDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
        birthDayCustomEditText.setText(String.valueOf(dayOfMonth));
        birthMonthCustomEditText.setText(String.valueOf(monthOfYear + 1));
        birthYearCustomEditText.setText(String.valueOf(year));
    }

    private void validateRegister() {
        if (usernameCustomEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Ingrese un nombre de usuario", Toast.LENGTH_LONG).show();
            return;
        }
        if (passwordCustomEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        if (passwordCustomEditText.getText().toString().length() <= 7 ||
                !checkPassword(passwordCustomEditText.getText().toString())) {
            Toast.makeText(this, "Ingrese una contraseña valida (Mas de 7 caracteres, Una mayuscula, una minuscula y un numero)", Toast.LENGTH_LONG).show();
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
    private boolean checkPassword(String password) {
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isDigit(ch))
                numberFlag = true;
            else if (Character.isUpperCase(ch))
                capitalFlag = true;
            else if (Character.isLowerCase(ch))
                lowerCaseFlag = true;
        }
        return numberFlag && capitalFlag && lowerCaseFlag;
    }

    private void register() {
        desableRegisterButton();
        JSONObject loginJsonObject = new JSONObject();

        try {
            loginJsonObject.put("name", nameCustomEditText.getText().toString());
            loginJsonObject.put("lastName", lastNameCustomEditText.getText().toString());
            loginJsonObject.put("userTypeId", getRegisterTypeId());
            loginJsonObject.put("gender", getGender());
            loginJsonObject.put("dni", dniCustomEditText.getText().toString());
            loginJsonObject.put("nationality", nationalityCustomEditText.getText().toString());
            loginJsonObject.put("birthDate", FuncionesFecha.formatDateLDApi(selectedLocalDate));
            loginJsonObject.put("birthPlace", birthPlaceCustomEditText.getText().toString());
            loginJsonObject.put("insuranceType", insuranceTypeMaterialSpinner.getItems().get(insuranceTypeMaterialSpinner.getSelectedIndex()));
            loginJsonObject.put("email", emailCustomEditText.getText().toString());
            loginJsonObject.put("rate", 5);
            loginJsonObject.put("speciality", getSpeciality());
            loginJsonObject.put("username", usernameCustomEditText.getText().toString());
            loginJsonObject.put("password", passwordCustomEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(RepediatricsApi.USERS_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(loginJsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RegisterActivity.this, "Registrado correctamente, ya puede iniciar sesión", Toast.LENGTH_LONG).show();
                        enableRegisterButton();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 409){
                            Toast.makeText(RegisterActivity.this, "El nombre de usuario que ingresó ya existe", Toast.LENGTH_LONG).show();
                            enableRegisterButton();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Hubo un error al crear el usuario", Toast.LENGTH_LONG).show();
                            enableRegisterButton();
                        }
                    }
                });
    }

    private int getRegisterTypeId(){
        return registerType == REGISTER_TYPE_PARENT ? 2 :
                registerType == REGISTER_TYPE_PEDIATRICIAN ? 1 : 1;
    }

    private String getGender(){
        return maleAppCompatRadioButton.isChecked() ? "Masculino" : "Femenino";
    }

    private String getSpeciality(){
        return registerType == REGISTER_TYPE_PARENT ? "Oncologia" :
                registerType == REGISTER_TYPE_PEDIATRICIAN ? "Pediatria" : "Oncologia";
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
}
