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
import pe.work.karique.repediatrics.activities.PatientDetailsActivity;
import pe.work.karique.repediatrics.adapters.PatientsAdapter;
import pe.work.karique.repediatrics.models.MedicalAppointment;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;

public class ParentPatientsFragment extends Fragment {
    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;

    private RecyclerView patientsRecyclerView;
    private PatientsAdapter patientsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<User> patients;

    private String userId;

    public ParentPatientsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_parent_patients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        patientsRecyclerView = view.findViewById(R.id.patientsRecyclerView);
        messageConstraintLayout = view.findViewById(R.id.messageConstraintLayout);
        messageTextView = view.findViewById(R.id.messageTextView);

        patients = new ArrayList<>();
        patientsAdapter = new PatientsAdapter(patients);
        patientsAdapter.setOnUserClicked(new PatientsAdapter.OnUserClickedListener() {
            @Override
            public void OnUserClicked(User user) {
                if (onUserClickedListener != null){
                    onUserClickedListener.OnUserClicked(user);
                }
            }
        });
        layoutManager = new LinearLayoutManager(getActivity());
        patientsRecyclerView.setAdapter(patientsAdapter);
        patientsRecyclerView.setLayoutManager(layoutManager);
        getPatientsByParent();
    }

    public void getPatientsByParent(){
        onProgressBarChanged.OnProgressBarVisible();

        AndroidNetworking.get(RepediatricsApi.PATIENTS_BY_PARENT(userId))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        onProgressBarChanged.OnProgressBarHide();
                        messageConstraintLayout.setVisibility(View.GONE);

                        patients.clear();
                        patients.addAll(User.from(response));
                        patientsAdapter.notifyDataSetChanged();

                        if (patients.isEmpty()){
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
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public interface OnUserClickedListener {
        void OnUserClicked(User user);
    }
    private OnUserClickedListener onUserClickedListener;
    public void setOnUserClicked(OnUserClickedListener onUserClickedListener) {
        this.onUserClickedListener = onUserClickedListener;
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
