package pe.work.karique.repediatrics.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.adapters.SpecialistsAdapter;
import pe.work.karique.repediatrics.models.Ticket;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ChooseSpecialistForMedicalAppointmentActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 13;

    private SessionManager sessionManager;
    private ImageButton backImageButton;
    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;
    private ProgressBar progressBar;

    private RecyclerView specialistRecyclerView;
    private SpecialistsAdapter specialistsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<User> specialists;

    private Ticket ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_specialist_for_medical_appointment);
        hide();

        ticket = Ticket.from(getIntent().getBundleExtra("ticket"));

        sessionManager = SessionManager.getInstance(this);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressBar = findViewById(R.id.progressBar);

        specialistRecyclerView = findViewById(R.id.specialistRecyclerView);
        messageConstraintLayout = findViewById(R.id.messageConstraintLayout);
        messageTextView = findViewById(R.id.messageTextView);
        specialists = new ArrayList<>();
        specialistsAdapter = new SpecialistsAdapter(specialists);
        specialistsAdapter.setOnUserClicked(new SpecialistsAdapter.OnUserClickedListener() {
            @Override
            public void OnUserClicked(User specialist) {
                //especialista seleccionado
                chooseAppointmentForMedicalAppointment(ticket, specialist);
            }
        });
        specialistsAdapter.setOnSeeProfileClicked(new SpecialistsAdapter.OnSeeProfileClickedListener() {
            @Override
            public void OnSeeProfileClicked(User user) {
                //ver detalles del perfil del doc
            }
        });
        layoutManager = new LinearLayoutManager(this);
        specialistRecyclerView.setAdapter(specialistsAdapter);
        specialistRecyclerView.setLayoutManager(layoutManager);

        getSpecialists();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void chooseAppointmentForMedicalAppointment(Ticket ticket, User specialist){
        Intent intent = new Intent(this, ChooseAppointmentActivity.class);
        intent.putExtra("ticket", ticket.toBundle());
        intent.putExtra("specialist", specialist.toBundle());
        startActivityForResult(intent, ChooseAppointmentActivity.REQUEST_CODE);
    }
    private void getSpecialists(){
        specialistRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        AndroidNetworking.get(RepediatricsApi.SPECIALISTS_BY_HOSPITAL(ticket.getHospitalId()))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        specialistRecyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        messageConstraintLayout.setVisibility(View.GONE);

                        specialists.clear();
                        specialists.addAll(User.from(response));
                        specialistsAdapter.notifyDataSetChanged();

                        if (specialists.isEmpty()){
                            showNoDataMessage();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        specialistRecyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        showNoDataMessage();
                    }
                });
    }
    private void showNoDataMessage(){
        messageTextView.setText(getResources().getString(R.string.no_data));
        messageConstraintLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Cita medica agregada correctamente
        if (requestCode == ChooseAppointmentActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_FIRST_USER) {
                setResult(Activity.RESULT_FIRST_USER);
                finish();
            }
        }
    }
}
