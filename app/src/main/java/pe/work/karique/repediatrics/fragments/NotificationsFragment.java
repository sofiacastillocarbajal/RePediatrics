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
import pe.work.karique.repediatrics.adapters.DoctorCommentsAdapter;
import pe.work.karique.repediatrics.adapters.NotificationsAdapter;
import pe.work.karique.repediatrics.models.DoctorComment;
import pe.work.karique.repediatrics.models.Notification;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;

public class NotificationsFragment extends Fragment {
    private SessionManager sessionManager;
    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;

    private RecyclerView notificationsRecyclerView;
    private NotificationsAdapter notificationsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Notification> notifications;

    public NotificationsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = SessionManager.getInstance(getContext());
        notificationsRecyclerView = view.findViewById(R.id.notificationsRecyclerView);
        messageConstraintLayout = view.findViewById(R.id.messageConstraintLayout);
        messageTextView = view.findViewById(R.id.messageTextView);

        notifications = new ArrayList<>();
        notificationsAdapter = new NotificationsAdapter(notifications);
        notificationsAdapter.setOnNotificationClicked(new NotificationsAdapter.OnNotificationClickedListener() {
            @Override
            public void OnNotificationClicked(Notification notification) {
                if (onNotificationClickedListener != null){
                    onNotificationClickedListener.OnNotificationClicked(notification);
                }
            }
        });
        notificationsAdapter.setOnNotificationLongClicked(new NotificationsAdapter.OnNotificationLongClickedListener() {
            @Override
            public void OnNotificationLongClicked(Notification notification) {
                if (onNotificationLongClickedListener != null){
                    onNotificationLongClickedListener.OnNotificationLongClicked(notification);
                }
            }
        });
        layoutManager = new LinearLayoutManager(getActivity());
        notificationsRecyclerView.setAdapter(notificationsAdapter);
        notificationsRecyclerView.setLayoutManager(layoutManager);

        getNotifications();
    }

    private void getNotifications(){
        onProgressBarChanged.OnProgressBarVisible();

        AndroidNetworking.get(RepediatricsApi.NOTIFICATIONS_BY_USERS(sessionManager.getid()))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        onProgressBarChanged.OnProgressBarHide();
                        messageConstraintLayout.setVisibility(View.GONE);

                        notifications.clear();
                        notifications.addAll(Notification.from(response));
                        notificationsAdapter.notifyDataSetChanged();

                        if (notifications.isEmpty()){
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

    public interface OnNotificationClickedListener {
        void OnNotificationClicked(Notification notification);
    }
    private OnNotificationClickedListener onNotificationClickedListener;
    public void setOnNotificationClicked(OnNotificationClickedListener onNotificationClickedListener) {
        this.onNotificationClickedListener = onNotificationClickedListener;
    }

    public interface OnNotificationLongClickedListener {
        void OnNotificationLongClicked(Notification notification);
    }
    private OnNotificationLongClickedListener onNotificationLongClickedListener;
    public void setOnNotificationLongClicked(OnNotificationLongClickedListener onNotificationLongClickedListener) {
        this.onNotificationLongClickedListener = onNotificationLongClickedListener;
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
