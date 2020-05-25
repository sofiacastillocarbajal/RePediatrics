package pe.work.karique.repediatrics.fragments;


import android.content.Context;
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
import pe.work.karique.repediatrics.adapters.UsersAdapter;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.repositories.UserRepositories;

public class ParentHomeFragment extends Fragment {
    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;

    private RecyclerView doctorsRecyclerView;
    private UsersAdapter usersAdapter;
    private RecyclerView.LayoutManager usersLayoutManager;
    private List<User> users;

    public ParentHomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_parent_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        doctorsRecyclerView = view.findViewById(R.id.doctorsRecyclerView);
        messageConstraintLayout = view.findViewById(R.id.messageConstraintLayout);
        messageTextView = view.findViewById(R.id.messageTextView);
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(users, new UsersAdapter.ContextProvider() {
            @Override
            public Context getContext() {
                return ParentHomeFragment.this.getContext();
            }
        });
        usersAdapter.setOnUserClicked(new UsersAdapter.OnUserClickedListener() {
            @Override
            public void OnUserClicked(User user) {
                if (onUserClickedListener != null){
                    onUserClickedListener.OnUserClicked(user);
                }
            }
        });
        usersLayoutManager = new LinearLayoutManager(ParentHomeFragment.this.getContext(), RecyclerView.HORIZONTAL, false);
        doctorsRecyclerView.setAdapter(usersAdapter);
        doctorsRecyclerView.setLayoutManager(usersLayoutManager);
        getUsers();
    }

    public void getUsers(){
        onProgressBarChanged.OnProgressBarVisible();

        AndroidNetworking.get(RepediatricsApi.DOCTORS_URL)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        onProgressBarChanged.OnProgressBarHide();
                        messageConstraintLayout.setVisibility(View.GONE);

                        users.clear();
                        users.addAll(User.from(response));
                        usersAdapter.notifyDataSetChanged();

                        if (users.isEmpty()){
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

    public interface OnUserClickedListener {
        void OnUserClicked(User user);
    }
    private OnUserClickedListener onUserClickedListener;
    public void setOnUserClickedListener(OnUserClickedListener onUserClickedListener) {
        this.onUserClickedListener = onUserClickedListener;
    }
}
