package pe.work.karique.repediatrics.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.DoctorComment;
import pe.work.karique.repediatrics.models.Notification;
import pe.work.karique.repediatrics.util.FuncionesFecha;


public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.UserViewHolder> {
    private List<Notification> notifications;

    public NotificationsAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final Notification notification = notifications.get(position);

        holder.titleNameTextView.setText(notification.getText());
        Date date = FuncionesFecha.getDateWithHourFromString(notification.getDateOfSending());
        String sd = FuncionesFecha.formatDateForAPIWithHour(date);
        holder.stateTextView.setText(sd);
        holder.okConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onNotificationClickedListener != null){
                    onNotificationClickedListener.OnNotificationClicked(notification);
                }
            }
        });
        holder.notificationCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onNotificationLongClickedListener != null){
                    onNotificationLongClickedListener.OnNotificationLongClicked(notification);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        CardView notificationCardView;
        TextView titleNameTextView;
        TextView stateTextView;
        ConstraintLayout okConstraintLayout;

        public UserViewHolder(View itemView) {
            super(itemView);
            notificationCardView = itemView.findViewById(R.id.notificationCardView);
            titleNameTextView = itemView.findViewById(R.id.titleNameTextView);
            stateTextView = itemView.findViewById(R.id.stateTextView);
            okConstraintLayout = itemView.findViewById(R.id.okConstraintLayout);
        }
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
}
