package pe.work.karique.repediatrics.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.MedicalAppointment;
import pe.work.karique.repediatrics.models.TimeTableByDoctor;
import pe.work.karique.repediatrics.util.FuncionesFecha;


public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.UserViewHolder> {
    private List<TimeTableByDoctor> timeTableByDoctors;

    public AppointmentsAdapter(List<TimeTableByDoctor> timeTableByDoctors) {
        this.timeTableByDoctors = timeTableByDoctors;
    }

    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_appointment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final TimeTableByDoctor timeTableByDoctor = timeTableByDoctors.get(position);

        Date startDate = FuncionesFecha.getDateWithHourFromString(timeTableByDoctor.getStartDateTime());
        String startTime = FuncionesFecha.formatDateToHour(startDate);

        Date endDate = FuncionesFecha.getDateWithHourFromString(timeTableByDoctor.getEndDateTime());
        String endTime = FuncionesFecha.formatDateToHour(endDate);

        holder.appointmentTextView.setText(String.format("%s   -   %s", startTime, endTime));
        holder.appointmentConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onAppointmentClickedListener != null)
                    onAppointmentClickedListener.OnAppointmentClicked(timeTableByDoctor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeTableByDoctors.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView appointmentTextView;
        ConstraintLayout appointmentConstraintLayout;

        public UserViewHolder(View itemView) {
            super(itemView);
            appointmentTextView = itemView.findViewById(R.id.appointmentTextView);
            appointmentConstraintLayout = itemView.findViewById(R.id.appointmentConstraintLayout);
        }
    }

    public interface OnAppointmentClickedListener {
        void OnAppointmentClicked(TimeTableByDoctor timeTableByDoctor);
    }
    private OnAppointmentClickedListener onAppointmentClickedListener;
    public void setOnAppointmentClicked(OnAppointmentClickedListener onAppointmentClickedListener) {
        this.onAppointmentClickedListener = onAppointmentClickedListener;
    }
}
