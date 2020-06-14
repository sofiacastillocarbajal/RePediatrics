package pe.work.karique.repediatrics.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.models.UserType;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

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

    SessionManager sessionManager;

    private ImageButton backImageButton;
    private CardView loginCardView;
    private TextView loginTextView;
    private ProgressBar loginProgressBar;
    private AppCompatEditText usernameCustomEditText;
    private AppCompatEditText passwordCustomEditText;
    private TextView resetPasswordEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = SessionManager.getInstance(this);
        if (sessionManager.getUserIsLogged()){
            startActivityOnUser();
        }

        mContentView = findViewById(R.id.fullscreenConstraintLayout);
        hideToolbar();

        backImageButton = findViewById(R.id.backImageButton);
        loginCardView = findViewById(R.id.loginCardView);
        loginTextView = findViewById(R.id.loginTextView);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        usernameCustomEditText = findViewById(R.id.usernameCustomEditText);
        passwordCustomEditText = findViewById(R.id.passwordCustomEditText);
        resetPasswordEmailTextView = findViewById(R.id.resetPasswordEmailTextView);

        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loginCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameCustomEditText.getText().toString();
                String password = passwordCustomEditText.getText().toString();
                login(username, password);
            }
        });
        resetPasswordEmailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSendResetPasswordEmailActivity();
            }
        });
    }

    private void login(String username,String password){
        disableLoginButton();
        JSONObject loginJsonObject = new JSONObject();

        try {
            loginJsonObject.put("username", username);
            loginJsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(RepediatricsApi.LOGIN_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(loginJsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(LoginActivity.this, "Login exitoso", Toast.LENGTH_LONG).show();
                        User userLogin = User.from(response);
                        sessionManager.saveUserSession(userLogin);
                        updateToken();
                        startMainActivity(userLogin.getUserTypeId(), userLogin.getSpecialty());
                        enableLoginButton();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(LoginActivity.this, "Error de ingreso", Toast.LENGTH_LONG).show();
                        enableLoginButton();
                    }
                });
    }
    private void startMainActivity(String userTypeId, String speciality){
        if (userTypeId.equals(UserType.TYPE_DOCTOR)) {
            if (speciality.equals("Pediatria")){
                Intent mainIntent = new Intent(this, DoctorMainActivity.class);
                startActivity(mainIntent);
                finishAffinity();
            }
            else if (speciality.equals("Oncologia")){
                Intent mainIntent = new Intent(this, SpecialistMainActivity.class);
                startActivity(mainIntent);
                finishAffinity();
            }
        }
        else if (userTypeId.equals(UserType.TYPE_PARENT)) {
            Intent intent = new Intent(this, ParentMainActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        else if (userTypeId.equals(UserType.TYPE_ADMIN)) {
            Intent intent = new Intent(this, AdminMainActivity.class);
            startActivity(intent);
            finishAffinity();
        }
    }
    private void disableLoginButton(){
        loginCardView.setEnabled(false);
        loginTextView.setVisibility(View.INVISIBLE);
        loginProgressBar.setVisibility(View.VISIBLE);
    }
    private void enableLoginButton(){
        loginCardView.setEnabled(true);
        loginTextView.setVisibility(View.VISIBLE);
        loginProgressBar.setVisibility(View.GONE);
    }
    private void startActivityOnUser(){
        startMainActivity(sessionManager.getusertypeid(), sessionManager.getspeciality());
    }
    private void hideToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }
    private void startSendResetPasswordEmailActivity(){
        Intent intent = new Intent(this, SendResetPasswordEmailActivity.class);
        startActivity(intent);
    }
    private void updateToken(){
        SessionManager sessionManager = SessionManager.getInstance(this);
        if (sessionManager.getid() == null){
            return;
        }
        if (sessionManager.getid().equals("")){
            return;
        }

        JSONObject newCourseJsonObject = new JSONObject();
        try {
            newCourseJsonObject.put("userId", sessionManager.getid());
            newCourseJsonObject.put("token", FirebaseInstanceId.getInstance().getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(RepediatricsApi.TOKEN_FCM_URL)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .addJSONObjectBody(newCourseJsonObject)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    @Override
    protected void onResume() {
        hideToolbar();
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

}
