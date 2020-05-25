package pe.work.karique.repediatrics.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import pe.work.karique.repediatrics.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;

import static pe.work.karique.repediatrics.activities.RegisterActivity.REGISTER_TYPE_PARENT;
import static pe.work.karique.repediatrics.activities.RegisterActivity.REGISTER_TYPE_PEDIATRICIAN;
import static pe.work.karique.repediatrics.activities.RegisterActivity.REGISTER_TYPE_SPECIALIST;

public class ChooseRegisterTypeActivity extends AppCompatActivity {

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
    CardView registerPediatricianCardView;
    CardView registerSpecialistCardView;
    CardView registerParentCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_register_type);
        hideToolbar();

        mContentView = findViewById(R.id.fullscreenConstraintLayout);

        registerPediatricianCardView = findViewById(R.id.registerPediatricianCardView);
        registerSpecialistCardView = findViewById(R.id.registerSpecialistCardView);
        registerParentCardView = findViewById(R.id.registerParentCardView);
        backImageButton = findViewById(R.id.backImageButton);

        registerPediatricianCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegisterActivity(REGISTER_TYPE_PEDIATRICIAN);
            }
        });
        registerSpecialistCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegisterActivity(REGISTER_TYPE_SPECIALIST);
            }
        });
        registerParentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegisterActivity(REGISTER_TYPE_PARENT);
            }
        });
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        hideToolbar();
        super.onResume();
    }

    private void hideToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void startRegisterActivity(int registerType){
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("registerType", registerType);
        startActivityForResult(intent, RegisterActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //user agregado correctamente
        if (requestCode == RegisterActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                finish();
            }
        }
    }
}
