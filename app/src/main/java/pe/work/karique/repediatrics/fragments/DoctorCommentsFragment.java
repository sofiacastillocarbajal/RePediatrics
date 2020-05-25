package pe.work.karique.repediatrics.fragments;


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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.adapters.DoctorCommentsAdapter;
import pe.work.karique.repediatrics.adapters.PatientsAdapter;
import pe.work.karique.repediatrics.models.DoctorComment;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;

public class DoctorCommentsFragment extends Fragment {
    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;

    private RecyclerView doctorCommentsRecyclerView;
    private DoctorCommentsAdapter doctorCommentsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<DoctorComment> doctorComments;

    private User doctor;

    private FloatingActionButton speedDialFloatingActionButton;
    private SessionManager sessionManager;

    public DoctorCommentsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = SessionManager.getInstance(getContext());
        doctorCommentsRecyclerView = view.findViewById(R.id.doctorCommentsRecyclerView);
        messageConstraintLayout = view.findViewById(R.id.messageConstraintLayout);
        messageTextView = view.findViewById(R.id.messageTextView);

        doctorComments = new ArrayList<>();
        doctorCommentsAdapter = new DoctorCommentsAdapter(doctorComments);
        doctorCommentsAdapter.setOnDoctorCommentClicked(new DoctorCommentsAdapter.OnDoctorCommentClickedListener() {
            @Override
            public void OnDoctorCommentClicked(DoctorComment doctorComment) {
                if (onDoctorCommentClickedListener != null){
                    onDoctorCommentClickedListener.OnDoctorCommentClicked(doctorComment);
                }
            }
        });
        doctorCommentsAdapter.setOnDoctorCommentLongClicked(new DoctorCommentsAdapter.OnDoctorCommentLongClickedListener() {
            @Override
            public void OnDoctorCommentLongClicked(DoctorComment doctorComment) {
                if (onDoctorCommentLongClickedListener != null){
                    onDoctorCommentLongClickedListener.OnDoctorCommentLongClicked(doctorComment);
                }
            }
        });
        layoutManager = new LinearLayoutManager(getActivity());
        doctorCommentsRecyclerView.setAdapter(doctorCommentsAdapter);
        doctorCommentsRecyclerView.setLayoutManager(layoutManager);

        speedDialFloatingActionButton = view.findViewById(R.id.speedDialFloatingActionButton);
        speedDialFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onAddCommentClickedListener != null){
                    onAddCommentClickedListener.OnAddCommentClicked();
                }
            }
        });

        if (sessionManager.isParent()){
            speedDialFloatingActionButton.show();
        }
        else speedDialFloatingActionButton.hide();

        getComments();
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }
    public void getComments(){
        onProgressBarChanged.OnProgressBarVisible();

        AndroidNetworking.get(RepediatricsApi.COMMENTS_BY_DOCTOR(doctor.getId()))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        onProgressBarChanged.OnProgressBarHide();
                        messageConstraintLayout.setVisibility(View.GONE);

                        doctorComments.clear();
                        doctorComments.addAll(DoctorComment.from(response));
                        doctorCommentsAdapter.notifyDataSetChanged();

                        if (doctorComments.isEmpty()){
                            showNoDataMessage("Sin datos");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        onProgressBarChanged.OnProgressBarHide();
                        showNoDataMessage("Error de carga");
                    }
                });
    }
    private void showNoDataMessage(String message){
        messageTextView.setText(message);
        messageConstraintLayout.setVisibility(View.VISIBLE);
    }

    public interface OnDoctorCommentClickedListener {
        void OnDoctorCommentClicked(DoctorComment doctorComment);
    }
    private OnDoctorCommentClickedListener onDoctorCommentClickedListener;
    public void setOnDoctorCommentClicked(OnDoctorCommentClickedListener onDoctorCommentClickedListener) {
        this.onDoctorCommentClickedListener = onDoctorCommentClickedListener;
    }

    public interface OnAddCommentClickedListener {
        void OnAddCommentClicked();
    }
    private OnAddCommentClickedListener onAddCommentClickedListener;
    public void setOnAddCommentClicked(OnAddCommentClickedListener onAddCommentClickedListener) {
        this.onAddCommentClickedListener = onAddCommentClickedListener;
    }

    public interface OnDoctorCommentLongClickedListener {
        void OnDoctorCommentLongClicked(DoctorComment doctorComment);
    }
    private OnDoctorCommentLongClickedListener onDoctorCommentLongClickedListener;
    public void setOnDoctorCommentLongClicked(OnDoctorCommentLongClickedListener onDoctorCommentLongClickedListener) {
        this.onDoctorCommentLongClickedListener = onDoctorCommentLongClickedListener;
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
