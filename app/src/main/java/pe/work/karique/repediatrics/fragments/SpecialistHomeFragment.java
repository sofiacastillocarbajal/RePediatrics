package pe.work.karique.repediatrics.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import pe.work.karique.repediatrics.activities.MedicalAppointmentDetailsActivity;
import pe.work.karique.repediatrics.adapters.MedicalAppointmentsAdapter;
import pe.work.karique.repediatrics.models.MedicalAppointment;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpecialistHomeFragment extends Fragment {
    private SessionManager sessionManager;
    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;

    private RecyclerView medicalAppointmentsRecyclerView;
    private MedicalAppointmentsAdapter medicalAppointmentsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MedicalAppointment> medicalAppointments;

    public SpecialistHomeFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_specialist_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = SessionManager.getInstance(getActivity());
        medicalAppointmentsRecyclerView = view.findViewById(R.id.medicalAppointmentsRecyclerView);
        messageConstraintLayout = view.findViewById(R.id.messageConstraintLayout);
        messageTextView = view.findViewById(R.id.messageTextView);

        medicalAppointments = new ArrayList<>();
        medicalAppointmentsAdapter = new MedicalAppointmentsAdapter(medicalAppointments);
        medicalAppointmentsAdapter.setOnMedicalAppointmentClicked(new MedicalAppointmentsAdapter.OnMedicalAppointmentClickedListener() {
            @Override
            public void OnMedicalAppointmentClicked(MedicalAppointment medicalAppointment) {
                showMedicalAppointmentDetails(medicalAppointment);
            }
        });
        layoutManager = new LinearLayoutManager(getActivity());
        medicalAppointmentsRecyclerView.setAdapter(medicalAppointmentsAdapter);
        medicalAppointmentsRecyclerView.setLayoutManager(layoutManager);
        getMedicalAppointments();
    }

    private void showMedicalAppointmentDetails(MedicalAppointment medicalAppointment){
        Intent intent = new Intent(getContext(), MedicalAppointmentDetailsActivity.class);
        intent.putExtras(medicalAppointment.toBundle());
        startActivityForResult(intent, MedicalAppointmentDetailsActivity.REQUEST_CODE);
    }

    public void getMedicalAppointments(){
        onProgressBarChanged.OnProgressBarVisible();

        AndroidNetworking.get(RepediatricsApi.MEDICAL_APPOINTMENTS_BY_DOCTORS(sessionManager.getid()))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        onProgressBarChanged.OnProgressBarHide();
                        messageConstraintLayout.setVisibility(View.GONE);

                        medicalAppointments.clear();
                        medicalAppointments.addAll(MedicalAppointment.from(response));
                        medicalAppointmentsAdapter.notifyDataSetChanged();

                        if (medicalAppointments.isEmpty()){
                            showNoDataMessage();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        onProgressBarChanged.OnProgressBarHide();
                        showNoDataMessage();
                    }
                });
    }

    private void showNoDataMessage(){
        messageTextView.setText(getResources().getString(R.string.no_data));
        messageConstraintLayout.setVisibility(View.VISIBLE);
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
