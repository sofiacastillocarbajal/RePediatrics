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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONArray;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.adapters.TimeTablesAdapter;
import pe.work.karique.repediatrics.models.Hospital;
import pe.work.karique.repediatrics.models.MedicalAppointment;
import pe.work.karique.repediatrics.models.TimeTableByDoctor;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;
import pe.work.karique.repediatrics.util.FuncionesFecha;

public class SpecialistTimeTableFragment extends Fragment {

    private SessionManager sessionManager;

    private List<Hospital> hospitals;
    private MaterialSpinner hospitalValueMaterialSpinner;
    private ProgressBar hospitalValueProgressBar;

    private CardView backDayDateCardView;
    private CardView nextDayDateCardView;
    private TextView dayNameTextView;

    private LocalDate currentDate;

    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;

    private RecyclerView timeTableRecyclerView;
    private TimeTablesAdapter timeTablesAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<TimeTableByDoctor> timeTableByDoctors;

    public SpecialistTimeTableFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_specialist_time_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = SessionManager.getInstance(getContext());

        hospitals = new ArrayList<>();
        hospitalValueMaterialSpinner = view.findViewById(R.id.hospitalValueMaterialSpinner);
        hospitalValueProgressBar = view.findViewById(R.id.hospitalValueProgressBar);

        messageConstraintLayout = view.findViewById(R.id.messageConstraintLayout);
        messageTextView = view.findViewById(R.id.messageTextView);
        timeTableRecyclerView = view.findViewById(R.id.timeTableRecyclerView);

        timeTableByDoctors = new ArrayList<>();
        timeTablesAdapter = new TimeTablesAdapter(timeTableByDoctors);
        timeTablesAdapter.setOnTimeTableByDoctorClicked(new TimeTablesAdapter.OnTimeTableByDoctorClickedListener() {
            @Override
            public void OnTimeTableByDoctorClicked(TimeTableByDoctor timeTableByDoctor) {
                if (onTimeTableClickedListener != null){
                    onTimeTableClickedListener.OnTimeTableClicked(timeTableByDoctor);
                }
            }
        });
        timeTablesAdapter.setOnTimeTableByDoctorLongClicked(new TimeTablesAdapter.OnTimeTableByDoctorLongClickedListener() {
            @Override
            public void OnTimeTableByDoctorLongClicked(TimeTableByDoctor timeTableByDoctor) {
                if (onTimeTableByDoctorLongClickedListener != null){
                    onTimeTableByDoctorLongClickedListener.OnTimeTableByDoctorLongClicked(timeTableByDoctor);
                }
            }
        });
        layoutManager = new GridLayoutManager(getActivity(), 2);
        timeTableRecyclerView.setAdapter(timeTablesAdapter);
        timeTableRecyclerView.setLayoutManager(layoutManager);

        backDayDateCardView = view.findViewById(R.id.backDayDateCardView);
        backDayDateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDate = currentDate.minusDays(1);
                dayNameTextView.setText(FuncionesFecha.formatLDateToText(currentDate));
                getTimeTables();
            }
        });
        nextDayDateCardView = view.findViewById(R.id.nextDayDateCardView);
        nextDayDateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDate = currentDate.plusDays(1);
                dayNameTextView.setText(FuncionesFecha.formatLDateToText(currentDate));
                getTimeTables();
            }
        });
        dayNameTextView = view.findViewById(R.id.dayNameTextView);

        currentDate = CalendarDay.today().getDate();
        dayNameTextView.setText(FuncionesFecha.formatLDateToText(currentDate));
        getHospitals();
    }

    private void getHospitals(){
        hospitalValueProgressBar.setVisibility(View.VISIBLE);

        AndroidNetworking.get(RepediatricsApi.HOSPITALS_BY_DOCTOR(sessionManager.getid()))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hospitalValueProgressBar.setVisibility(View.GONE);
                        hospitals.clear();
                        hospitals.addAll(Hospital.from(response));
                        hospitalValueMaterialSpinner.setItems(hospitals);
                        hospitalValueMaterialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {}
                        });

                        getTimeTables();
                    }
                    @Override
                    public void onError(ANError anError) {
                        hospitalValueProgressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Hospitales - Error al traer los hospitales", Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void getTimeTables(){
        String doctorId = sessionManager.getid();
        String hospitalId = hospitals.get(hospitalValueMaterialSpinner.getSelectedIndex()).getId();
        String dateInApiFormat = FuncionesFecha.formatDateLDApi(currentDate);

        timeTableRecyclerView.setVisibility(View.GONE);
        onProgressBarChanged.OnProgressBarVisible();

        AndroidNetworking.get(RepediatricsApi.TIMETABLE_BY_DOCTOR_BY_HOSPITAL_BY_DATE(doctorId, hospitalId, dateInApiFormat))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        timeTableRecyclerView.setVisibility(View.VISIBLE);
                        onProgressBarChanged.OnProgressBarHide();
                        messageConstraintLayout.setVisibility(View.GONE);

                        timeTableByDoctors.clear();
                        timeTableByDoctors.addAll(TimeTableByDoctor.from(response));
                        timeTablesAdapter.notifyDataSetChanged();

                        if (timeTableByDoctors.isEmpty()){
                            showNoDataMessage();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        timeTableRecyclerView.setVisibility(View.VISIBLE);
                        onProgressBarChanged.OnProgressBarHide();
                        showNoDataMessage();
                    }
                });
    }
    private void showNoDataMessage(){
        messageTextView.setText(getResources().getString(R.string.no_data));
        messageConstraintLayout.setVisibility(View.VISIBLE);
    }
    public Hospital getSelectedHospital(){
        return hospitals.get(hospitalValueMaterialSpinner.getSelectedIndex());
    }
    public String getCurrentDateApiFormat() {
        return FuncionesFecha.formatDateLDApi(currentDate);
    }

    public interface OnProgressBarChanged {
        void OnProgressBarVisible();
        void OnProgressBarHide();
    }
    private OnProgressBarChanged onProgressBarChanged;
    public void setOnProgressBarChanged(OnProgressBarChanged onProgressBarChanged) {
        this.onProgressBarChanged = onProgressBarChanged;
    }

    public interface OnTimeTableClickedListener {
        void OnTimeTableClicked(TimeTableByDoctor ticketTableByDoctor);
    }
    private OnTimeTableClickedListener onTimeTableClickedListener;
    public void setOnTimeTableClickedListener(OnTimeTableClickedListener onTimeTableClickedListener) {
        this.onTimeTableClickedListener = onTimeTableClickedListener;
    }

    public interface OnTimeTableByDoctorLongClickedListener {
        void OnTimeTableByDoctorLongClicked(TimeTableByDoctor timeTableByDoctor);
    }
    private OnTimeTableByDoctorLongClickedListener onTimeTableByDoctorLongClickedListener;
    public void setOnTimeTableByDoctorLongClicked(OnTimeTableByDoctorLongClickedListener onTimeTableByDoctorLongClickedListener) {
        this.onTimeTableByDoctorLongClickedListener = onTimeTableByDoctorLongClickedListener;
    }
}
