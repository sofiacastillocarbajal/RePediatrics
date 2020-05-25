package pe.work.karique.repediatrics.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.UserType;
import pe.work.karique.repediatrics.session.SessionManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class InitialActionActivity extends AppCompatActivity {

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
    private CardView loginCardView;
    private CardView registerCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_action);

        sessionManager = SessionManager.getInstance(this);
        if (sessionManager.getUserIsLogged()){
            startActivityOnUser();
        }

        mContentView = findViewById(R.id.fullscreenConstraintLayout);
        hideToolbar();

        loginCardView = findViewById(R.id.loginCardView);
        loginCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginActivity();
            }
        });

        registerCardView = findViewById(R.id.registerCardView);
        registerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChooseRegisterType();
            }
        });
    }

    private void startActivityOnUser(){
        startMainActivity(sessionManager.getusertypeid(), sessionManager.getspeciality());
    }

    private void startMainActivity(String userTypeId, String speciality){
        if (userTypeId.equals(UserType.TYPE_DOCTOR)) {
            if (speciality.equals("Pediatria")){
                Intent mainIntent = new Intent(this, DoctorMainActivity.class);
                startActivity(mainIntent);
                finish();
            }
            else if (speciality.equals("Oncologia")){
                Intent mainIntent = new Intent(this, SpecialistMainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }
        else if (userTypeId.equals(UserType.TYPE_PARENT)) {
            Intent intent = new Intent(this, ParentMainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (userTypeId.equals(UserType.TYPE_ADMIN)) {
            Intent intent = new Intent(this, AdminMainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void startChooseRegisterType(){
        Intent intent = new Intent(this, ChooseRegisterTypeActivity.class);
        startActivity(intent);
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

    private void hideToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }
}
