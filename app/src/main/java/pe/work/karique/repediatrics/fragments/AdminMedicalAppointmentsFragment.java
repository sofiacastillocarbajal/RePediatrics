package pe.work.karique.repediatrics.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pe.work.karique.repediatrics.R;

public class AdminMedicalAppointmentsFragment extends Fragment {


    public AdminMedicalAppointmentsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_medical_appointments, container, false);
    }

}
