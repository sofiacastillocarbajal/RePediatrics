package pe.work.karique.repediatrics.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.adapters.MedicalAppointmentsAdapter;
import pe.work.karique.repediatrics.models.MedicalAppointment;
import pe.work.karique.repediatrics.models.Ticket;

public class MedicalAppointmentsListWidgetFragment extends Fragment {
    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;

    private RecyclerView medicalAppointmentsRecyclerView;
    private MedicalAppointmentsAdapter medicalAppointmentsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public  List<MedicalAppointment> medicalAppointments;
    private List<MedicalAppointment> medicalAppointmentsTodos;

    private CardView todosCardView;
    private CardView enEsperaCardView;
    private CardView aprobadosCardView;
    private CardView canceladosCardView;

    private String url;

    public MedicalAppointmentsListWidgetFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medical_appointments_list_widget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messageConstraintLayout = view.findViewById(R.id.messageConstraintLayout);
        messageTextView = view.findViewById(R.id.messageTextView);

        medicalAppointments = new ArrayList<>();
        medicalAppointmentsTodos = new ArrayList<>();

        medicalAppointmentsRecyclerView = view.findViewById(R.id.medicalAppointmentsRecyclerView);
        medicalAppointmentsAdapter = new MedicalAppointmentsAdapter(medicalAppointments);
        medicalAppointmentsAdapter.setOnMedicalAppointmentClicked(new MedicalAppointmentsAdapter.OnMedicalAppointmentClickedListener() {
            @Override
            public void OnMedicalAppointmentClicked(MedicalAppointment medicalAppointment) {
                if (onMedicalAppointmentClickedListener != null){
                    onMedicalAppointmentClickedListener.onMedicalAppointmentClicked(medicalAppointment);
                }
            }
        });
        layoutManager = new LinearLayoutManager(view.getContext());
        medicalAppointmentsRecyclerView.setAdapter(medicalAppointmentsAdapter);
        medicalAppointmentsRecyclerView.setLayoutManager(layoutManager);

        todosCardView = view.findViewById(R.id.todosCardView);
        todosCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTodos();
            }
        });
        enEsperaCardView = view.findViewById(R.id.enEsperaCardView);
        enEsperaCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEnEspera();
            }
        });
        aprobadosCardView = view.findViewById(R.id.aprobadosCardView);
        aprobadosCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAprobados();
            }
        });
        canceladosCardView = view.findViewById(R.id.canceladosCardView);
        canceladosCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelados();
            }
        });

        getMedicalAppointments();
    }

    public void getMedicalAppointments(){
        showNoDataMessage("Cargando...");
        medicalAppointmentsRecyclerView.setVisibility(View.GONE);
        onProgressBarChanged.OnProgressBarVisible();

        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        medicalAppointmentsRecyclerView.setVisibility(View.VISIBLE);
                        onProgressBarChanged.OnProgressBarHide();
                        messageConstraintLayout.setVisibility(View.GONE);

                        medicalAppointments.clear();
                        medicalAppointments.addAll(MedicalAppointment.from(response));
                        medicalAppointmentsTodos.addAll(MedicalAppointment.from(response));
                        medicalAppointmentsAdapter.notifyDataSetChanged();

                        if (medicalAppointments.isEmpty()){
                            showNoDataMessage("Sin datos");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        medicalAppointmentsRecyclerView.setVisibility(View.VISIBLE);
                        onProgressBarChanged.OnProgressBarHide();
                        showNoDataMessage("Error de carga");
                    }
                });
    }
    private void showNoDataMessage(String message){
        messageTextView.setText(message);
        messageConstraintLayout.setVisibility(View.VISIBLE);
    }
    public void setUrl(String url) {
        this.url = url;
    }
    private void showTodos(){
        medicalAppointments.clear();
        medicalAppointments.addAll(medicalAppointmentsTodos);
        medicalAppointmentsAdapter.notifyDataSetChanged();

        if (medicalAppointments.isEmpty()){
            showNoDataMessage("Sin datos");
        }
        else messageConstraintLayout.setVisibility(View.GONE);
    }
    private void showEnEspera(){
        medicalAppointments.clear();
        medicalAppointments.addAll(getFiltrados(MedicalAppointment.STATE_EN_ESPERA));
        medicalAppointmentsAdapter.notifyDataSetChanged();

        if (medicalAppointments.isEmpty()){
            showNoDataMessage("Sin datos");
        }
        else messageConstraintLayout.setVisibility(View.GONE);
    }
    private void showAprobados(){
        medicalAppointments.clear();
        medicalAppointments.addAll(getFiltrados(MedicalAppointment.STATE_APROBADO));
        medicalAppointmentsAdapter.notifyDataSetChanged();

        if (medicalAppointments.isEmpty()){
            showNoDataMessage("Sin datos");
        }
        else messageConstraintLayout.setVisibility(View.GONE);
    }
    private void showCancelados(){
        medicalAppointments.clear();
        medicalAppointments.addAll(getFiltrados(MedicalAppointment.STATE_CANCELADO));
        medicalAppointmentsAdapter.notifyDataSetChanged();

        if (medicalAppointments.isEmpty()){
            showNoDataMessage("Sin datos");
        }
        else messageConstraintLayout.setVisibility(View.GONE);
    }
    private List<MedicalAppointment> getFiltrados(String state){
        List<MedicalAppointment> medicalAppointmentsFiltrados = new ArrayList<>();
        for (int i = 0; i < medicalAppointmentsTodos.size(); i++) {
            MedicalAppointment ma = medicalAppointmentsTodos.get(i);
            if (ma.getState().equals(state)){
                medicalAppointmentsFiltrados.add(ma);
            }
        }
        return medicalAppointmentsFiltrados;
    }

    public interface OnMedicalAppointmentClickedListener {
        void onMedicalAppointmentClicked(MedicalAppointment medicalAppointment);
    }
    private OnMedicalAppointmentClickedListener onMedicalAppointmentClickedListener;
    public void setOnMedicalAppointmentClicked(OnMedicalAppointmentClickedListener onMedicalAppointmentClickedListener) {
        this.onMedicalAppointmentClickedListener = onMedicalAppointmentClickedListener;
    }

    public interface OnProgressBarChanged {
        void OnProgressBarVisible();
        void OnProgressBarHide();
    }
    private OnProgressBarChanged onProgressBarChanged;
    public void setOnProgressBarChanged(OnProgressBarChanged onProgressBarChanged) {
        this.onProgressBarChanged = onProgressBarChanged;
    }
}
