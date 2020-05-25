package pe.work.karique.repediatrics.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.util.FuncionesFecha;

public class DoctorDetailsFragment extends Fragment {

    private User doctor;
    private TextView genderValueTextView;
    private TextView dniValueTextView;
    private TextView nationalityValueTextView;
    private TextView birthDateValueTextView;
    private TextView birthPlaceValueTextView;
    private TextView insuranceValueTextView;

    public DoctorDetailsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        genderValueTextView = view.findViewById(R.id.genderValueTextView);
        dniValueTextView = view.findViewById(R.id.dniValueTextView);
        nationalityValueTextView = view.findViewById(R.id.nationalityValueTextView);
        birthDateValueTextView = view.findViewById(R.id.birthDateValueTextView);
        birthPlaceValueTextView = view.findViewById(R.id.birthPlaceValueTextView);
        insuranceValueTextView = view.findViewById(R.id.insuranceValueTextView);
        setData();
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }
    private void setData(){
        genderValueTextView.setText(doctor.getGender());
        dniValueTextView.setText(doctor.getDni());
        nationalityValueTextView.setText(doctor.getNationality());
        birthDateValueTextView.setText(FuncionesFecha.formatDate(FuncionesFecha.getDateFromString(doctor.getBirthDate())) + " (" +FuncionesFecha.getYearsFromNow(FuncionesFecha.getDateFromString(doctor.getBirthDate())) + " AÑOS)");
        birthPlaceValueTextView.setText(doctor.getBirthPlace());
        insuranceValueTextView.setText(doctor.getInsuranceType());
    }
}
