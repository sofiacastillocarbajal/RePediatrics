package pe.work.karique.repediatrics.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.adapters.TicketsAdapter;
import pe.work.karique.repediatrics.models.Ticket;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChooseTicketForMedicalAppointmentActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 12;

    private SessionManager sessionManager;
    private ImageButton backImageButton;
    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;

    private ProgressBar progressBar;
    private RecyclerView ticketsRecyclerView;
    private TicketsAdapter ticketsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Ticket> tickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_ticket_for_medical_appointment);
        hide();

        sessionManager = SessionManager.getInstance(this);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressBar = findViewById(R.id.progressBar);

        ticketsRecyclerView = findViewById(R.id.ticketsRecyclerView);
        messageConstraintLayout = findViewById(R.id.messageConstraintLayout);
        messageTextView = findViewById(R.id.messageTextView);
        tickets = new ArrayList<>();
        ticketsAdapter = new TicketsAdapter(tickets);
        ticketsAdapter.setOnTicketClicked(new TicketsAdapter.OnTicketClickedListener() {
            @Override
            public void onTicketClicked(Ticket ticket) {
                validateChooseSpecialistForMedicalAppointment(ticket);
            }
        });
        layoutManager = new LinearLayoutManager(this);
        ticketsRecyclerView.setAdapter(ticketsAdapter);
        ticketsRecyclerView.setLayoutManager(layoutManager);

        getTickets();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void validateChooseSpecialistForMedicalAppointment(Ticket ticket){
        //validaciones
        if (ticket.getState().equals(Ticket.STATE_CANCELADO)){
            Toast.makeText(this, "Debe escoger un ticket aprobado", Toast.LENGTH_LONG).show();
            return;
        }
        else if (ticket.getState().equals(Ticket.STATE_EN_ESPERA)){
            Toast.makeText(this, "Debe escoger un ticket aprobado", Toast.LENGTH_LONG).show();
            return;
        }
        else if (ticket.getState().equals(Ticket.STATE_APROBADO)){
            getIsTicketUsed(ticket);
        }
    }
    private void chooseSpecialistForMedicalAppointment(Ticket ticket){
        Intent intent = new Intent(this, ChooseSpecialistForMedicalAppointmentActivity.class);
        intent.putExtra("ticket", ticket.toBundle());
        startActivityForResult(intent, ChooseSpecialistForMedicalAppointmentActivity.REQUEST_CODE);
    }
    private void getTickets(){
        ticketsRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        AndroidNetworking.get(RepediatricsApi.TICKETS_BY_PARENT(sessionManager.getid()))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ticketsRecyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        messageConstraintLayout.setVisibility(View.GONE);

                        tickets.clear();
                        tickets.addAll(Ticket.from(response));
                        ticketsAdapter.notifyDataSetChanged();

                        if (tickets.isEmpty()){
                            showNoDataMessage();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ticketsRecyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        showNoDataMessage();
                    }
                });
    }
    private void getIsTicketUsed(Ticket ticket){
        ticketsRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        AndroidNetworking.get(RepediatricsApi.TICKET_IS_USED(ticket.getId()))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("false")){
                                chooseSpecialistForMedicalAppointment(ticket);
                            }
                            else {
                                getTickets();
                                Toast.makeText(ChooseTicketForMedicalAppointmentActivity.this, "El ticket ya ha sido usado previamente", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ChooseTicketForMedicalAppointmentActivity.this, "Error de validación", Toast.LENGTH_LONG).show();
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
        if (requestCode == ChooseSpecialistForMedicalAppointmentActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_FIRST_USER) {
                setResult(Activity.RESULT_FIRST_USER);
                finish();
            }
        }
    }
}
