package pe.work.karique.repediatrics.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.network.RepediatricsApi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.libizo.CustomEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class SendResetPasswordEmailActivity extends AppCompatActivity {

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

    private ProgressBar sendProgressBar;
    private TextView sendTextView;

    private AppCompatImageButton backImageButton;
    private CustomEditText usernameCustomEditText;
    private CustomEditText emailEditText;
    private CardView sendEmailCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_reset_password_email);

        mContentView = findViewById(R.id.fullscreenConstraintLayout);
        hideToolbar();

        sendProgressBar = findViewById(R.id.sendProgressBar);
        sendTextView = findViewById(R.id.sendTextView);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        usernameCustomEditText = findViewById(R.id.usernameCustomEditText);
        emailEditText = findViewById(R.id.emailEditText);
        sendEmailCardView = findViewById(R.id.sendEmailCardView);
        sendEmailCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameCustomEditText.getText().toString();
                String email = emailEditText.getText().toString();
                sendResetPasswordEmail(username, email);
            }
        });
    }
    private void disableLoginButton(){
        sendEmailCardView.setEnabled(false);
        sendTextView.setVisibility(View.INVISIBLE);
        sendProgressBar.setVisibility(View.VISIBLE);
    }
    private void enableLoginButton(){
        sendEmailCardView.setEnabled(true);
        sendTextView.setVisibility(View.VISIBLE);
        sendProgressBar.setVisibility(View.GONE);
    }
    private void sendResetPasswordEmail(String username, String email){
        disableLoginButton();
        JSONObject loginJsonObject = new JSONObject();

        try {
            loginJsonObject.put("username", username);
            loginJsonObject.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(RepediatricsApi.SEND_RESET_PASSWORD_EMAIL_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(loginJsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")){
                                Toast.makeText(SendResetPasswordEmailActivity.this, "Se envió un mensaje a su correo", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            Toast.makeText(SendResetPasswordEmailActivity.this, "Error en el sistema al enviar correo", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        enableLoginButton();
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 409){
                            Toast.makeText(SendResetPasswordEmailActivity.this, "Usuario/correo no encontrado", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(SendResetPasswordEmailActivity.this, "Error al enviar el mensaje", Toast.LENGTH_LONG).show();
                        }

                        enableLoginButton();
                    }
                });
    }

    private void hideToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @Override
    protected void onResume() {
        hideToolbar();
        super.onResume();
    }
}
