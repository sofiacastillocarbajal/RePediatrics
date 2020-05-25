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

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.adapters.AdminDoctorAdapter;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.network.RepediatricsApi;

public class AdminDoctorsPediatricianFragment extends Fragment {
    private RecyclerView specialistRecyclerView;
    private AdminDoctorAdapter specialistsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<User> specialists;

    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;

    public AdminDoctorsPediatricianFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_doctors_pediatrician, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        specialistRecyclerView = view.findViewById(R.id.specialistRecyclerView);
        messageConstraintLayout = view.findViewById(R.id.messageConstraintLayout);
        messageTextView = view.findViewById(R.id.messageTextView);
        specialists = new ArrayList<>();
        specialistsAdapter = new AdminDoctorAdapter(specialists);
        specialistsAdapter.setOnUserClicked(new AdminDoctorAdapter.OnUserClickedListener() {
            @Override
            public void OnUserClicked(User user) {
                if (onUserClickedListener != null){
                    onUserClickedListener.OnUserClicked(user);
                }
            }
        });
        layoutManager = new LinearLayoutManager(getContext());
        specialistRecyclerView.setAdapter(specialistsAdapter);
        specialistRecyclerView.setLayoutManager(layoutManager);

        getSpecialists();
    }

    private void getSpecialists(){
        specialistRecyclerView.setVisibility(View.GONE);
        onProgressBarChanged.OnProgressBarVisible();

        AndroidNetworking.get(RepediatricsApi.DOCTORS_URL)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        specialistRecyclerView.setVisibility(View.VISIBLE);
                        onProgressBarChanged.OnProgressBarHide();
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
                        onProgressBarChanged.OnProgressBarHide();
                        showNoDataMessage();
                    }
                });
    }
    private void showNoDataMessage(){
        messageTextView.setText(getResources().getString(R.string.no_data));
        messageConstraintLayout.setVisibility(View.VISIBLE);
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
